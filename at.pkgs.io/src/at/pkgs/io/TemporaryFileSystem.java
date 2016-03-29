/*
 * Copyright (c) 2009-2016, Architector Inc., Japan
 * All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package at.pkgs.io;

import java.security.SecureRandom;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public final class TemporaryFileSystem {

	public static interface ErrorHandler {

		public boolean handle(IOException cause);

	}

	public static final String NAME_PREFIX = TemporaryFileSystem.class.getName() + ".";

	public static final String LOCK_SUFFIX = ".lock";

	public static final String DIRECTORY_PREFIX = "directory.";

	public static final String IDENTITY_SUFFIX = ".id";

	public static final long DEFAULT_INTERVAL = 3600L * 1000L;

	private final SecureRandom random;

	private final LockFile lock;

	private final File root;

	private Thread thread;

	private TemporaryFileSystem() {
		File temporary;
		String name;
		LockFile lock;

		this.random = new SecureRandom();
		temporary = new File(System.getProperty("java.io.tmpdir"));
		this.cleanup(temporary);
		name = null;
		lock = null;
		do {
			File file;

			name = String.format("%s%016x", TemporaryFileSystem.NAME_PREFIX, this.random.nextLong());
			file = new File(temporary, name + TemporaryFileSystem.LOCK_SUFFIX);
			if (!this.createNewFile(file)) continue;
			lock = LockFile.lockOrClose(file);
		}
		while (lock == null);
		this.lock = lock;
		this.lock.getFile().deleteOnExit();
		this.root = new File(temporary, name);
		this.createDirectory(this.root);
		this.thread = null;
	}

	private boolean createNewFile(File file) {
		try {
			return file.createNewFile();
		}
		catch (IOException cause) {
			throw new RuntimeException(cause);
		}
	}

	private void createDirectory(File directory) {
		if (directory.exists())
			throw new RuntimeException("Directory already exists: " + this.root);
		if (directory.mkdirs())
			throw new RuntimeException("Cannot make directory: " + this.root);
	}

	private void delete(File file) {
		if (!file.exists()) return;
		if (file.isDirectory()) {
			for (File child : file.listFiles()) this.delete(child);
		}
		file.delete();
	}

	private void check(File root) {
		File file;
		LockFile lock;

		file = new File(root.getParent(), root.getName() + TemporaryFileSystem.LOCK_SUFFIX);
		lock = null;
		if (file.exists()) {
			lock = LockFile.lockOrClose(file);
			if (lock == null) return;
		}
		this.delete(root);
		if (lock == null) return;
		lock.delete();
	}

	private void cleanup(File temporary) {
		for (File root : temporary.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith(TemporaryFileSystem.NAME_PREFIX);
			}

		})) {
			this.check(root);
		}
	}

	public File getLockFile() {
		return this.lock.getFile();
	}

	public File getRoot() {
		return this.root;
	}

	public TemporaryFileSystem keepalive(final long interval, final ErrorHandler handler) {
		synchronized (this) {
			if (this.thread != null) return this;
			this.thread = new Thread(new Runnable() {

				protected void update() throws IOException {
					long time;

					time = System.currentTimeMillis();
					TemporaryFileSystem.this.getRoot().setLastModified(time);
					TemporaryFileSystem.this.getLockFile().setLastModified(time);
				}

				@Override
				public void run() {
					while (true) {
						try {
							this.update();
						}
						catch (IOException cause) {
							if (!handler.handle(cause)) break;
						}
						try {
							Thread.sleep(interval);
						}
						catch (InterruptedException ignored) {
							// do nothing
						}
					}
					synchronized (TemporaryFileSystem.this) {
						TemporaryFileSystem.this.thread = null;
					}
				}

			});
		}
		this.thread.setName(TemporaryFileSystem.class.getName()+ ".keepalive");
		this.thread.setDaemon(true);
		this.thread.run();
		return this;
	}

	public TemporaryFileSystem keepalive(ErrorHandler handler) {
		this.keepalive(TemporaryFileSystem.DEFAULT_INTERVAL, handler);
		return this;
	}

	public TemporaryFileSystem keepalive(long interval) {
		this.keepalive(interval, new ErrorHandler() {

			@Override
			public boolean handle(IOException cause) {
				return true;
			}

		});
		return this;
	}

	public TemporaryFileSystem keepalive() {
		this.keepalive(TemporaryFileSystem.DEFAULT_INTERVAL);
		return this;
	}

	public TemporaryDirectory makeDirectory() {
		String name;
		File identity;
		final File file;
		final File directory;

		do {
			name = String.format("%s%016x", TemporaryFileSystem.DIRECTORY_PREFIX, this.random.nextLong());
			identity = new File(this.root, name + TemporaryFileSystem.IDENTITY_SUFFIX);
		}
		while (!this.createNewFile(identity));
		file = identity;
		file.deleteOnExit();
		directory = new File(this.root, name);
		this.createDirectory(directory);
		return new TemporaryDirectory() {

			@Override
			public File getRoot() {
				return directory;
			}

			@Override
			public void delete() {
				TemporaryFileSystem.this.delete(directory);
				file.delete();
			}

		};
	}

	private static TemporaryFileSystem instance = null;

	public static TemporaryFileSystem instance() {
		if (TemporaryFileSystem.instance == null) {
			synchronized (TemporaryFileSystem.class) {
				if (TemporaryFileSystem.instance == null)
					TemporaryFileSystem.instance = new TemporaryFileSystem();
			}
		}
		return TemporaryFileSystem.instance;
	}

	public static void main(String... arguments) {
		System.out.println(TemporaryFileSystem.instance().getLockFile());
	}

}

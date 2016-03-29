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

import java.io.Closeable;
import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileLock;

public class LockFile implements Closeable {

	private boolean destroyed;

	private final File file;

	private final OutputStream stream;

	private final FileLock lock;

	public LockFile(File file) {
		FileOutputStream stream;
		FileLock lock;

		this.destroyed = false;
		this.file = file;
		stream = null;
		lock = null;
		try {
			stream = new FileOutputStream(this.file);
			lock = stream.getChannel().tryLock();
		}
		catch (IOException ignored) {
			// do nothing
		}
		this.stream = stream;
		this.lock = lock;
	}

	public File getFile() {
		return this.file;
	}

	public boolean isValid() {
		return !this.destroyed && this.lock != null && this.lock.isValid();
	}

	protected void release() throws IOException {
		if (this.isValid()) this.lock.release();
		if (this.stream != null) this.stream.close();
	}

	@Override
	public void close() {
		if (this.destroyed) return;
		try {
			this.release();
		}
		catch (IOException ignored) {
			// do nothing
		}
		finally {
			this.destroyed = true;
		}
	}

	public void delete() {
		if (this.destroyed) return;
		try {
			this.release();
			this.file.delete();
		}
		catch (IOException ignored) {
			// do nothing
		}
		finally {
			this.destroyed = true;
		}
	}

	public boolean isValidOrClose() {
		if (this.isValid()) return true;
		this.close();
		return false;
	}

	public boolean isValidOrDelete() {
		if (this.isValid()) return true;
		this.delete();
		return false;
	}

	public static LockFile lockOrClose(File file) {
		LockFile lock;

		lock = new LockFile(file);
		return lock.isValidOrClose() ? lock : null;
	}

	public static LockFile lockOrDelete(File file) {
		LockFile lock;

		lock = new LockFile(file);
		return lock.isValidOrDelete() ? lock : null;
	}

}

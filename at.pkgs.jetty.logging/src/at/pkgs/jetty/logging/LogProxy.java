/*
 * Copyright (c) 2009-2014, Architector Inc., Japan
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

package at.pkgs.jetty.logging;

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.log.AbstractLogger;

public class LogProxy extends AbstractLogger {

	public static interface LoggerFactory {

		public Logger newLogger(String name);

	}

	private final Logger logger;

	public LogProxy(String name) {
		this.logger = LogProxy.getRealLogger(name);
	}

	public LogProxy() {
		this("org.eclipse.jetty.util.log");
	}

	@Override
	protected Logger newLogger(String name) {
		return new LogProxy(name);
	}

	@Override
	public String getName() {
		return this.logger.getName();
	}

	@Override
	public void warn(String message, Object... arguments) {
		this.logger.warn(message, arguments);
	}

	@Override
	public void warn(String message, Throwable thrown) {
		this.logger.warn(message, thrown);
	}

	@Override
	public void warn(Throwable thrown) {
		this.logger.warn(thrown);
	}

	@Override
	public void info(String message, Object... arguments) {
		this.logger.info(message, arguments);
	}

	@Override
	public void info(String message, Throwable thrown) {
		this.logger.info(message, thrown);
	}

	@Override
	public void info(Throwable thrown) {
		this.logger.info(thrown);
	}

	@Override
	public boolean isDebugEnabled() {
		return this.logger.isDebugEnabled();
	}

	@Override
	public void setDebugEnabled(boolean value) {
		this.logger.setDebugEnabled(value);
	}

	@Override
	public void debug(String message, Object... arguments) {
		this.logger.debug(message, arguments);
	}

	@Override
	public void debug(String message, long value) {
		this.logger.debug(message, value);
	}

	@Override
	public void debug(String message, Throwable thrown) {
		this.logger.debug(message, thrown);
	}

	@Override
	public void debug(Throwable thrown) {
		this.logger.debug(thrown);
	}

	@Override
	public void ignore(Throwable ignored) {
		this.logger.ignore(ignored);
	}

	private static final LoggerFactory factory;

	@SuppressWarnings("resource")
	private static LoggerFactory initialize() throws Exception {
		List<URL> list;
		String library;
		String resource;
		ClassLoader loader;
		Class<?> factory;

		list = new ArrayList<URL>();
		library = System.getProperty(
				LogProxy.class.getName() + ".library");
		if (library != null) {
			File[] files;

			files = new File(library).listFiles();
			if (files == null)
				throw new RuntimeException("invalid LogProxy.library path");
			for (File file : files) {
				if (!file.isFile()) continue;
				if (!file.getName().endsWith(".jar")) continue;
				list.add(file.toURI().toURL());
			}
		}
		resource = System.getProperty(
				LogProxy.class.getName() + ".resource");
		if (resource != null)
			list.add(new File(resource).toURI().toURL());
		loader = new URLClassLoader(
				list.toArray(new URL[list.size()]),
				LogProxy.class.getClassLoader());
		factory = loader.loadClass(
				System.getProperty(
						LogProxy.class.getName() + ".factory"));
		return (LoggerFactory)factory.newInstance();
	}

	static {
		try {
			factory = LogProxy.initialize();
		}
		catch (RuntimeException throwable) {
			throwable.printStackTrace();
			throw throwable;
		}
		catch (Throwable throwable) {
			throwable.printStackTrace();
			throw new RuntimeException(throwable);
		}
	}

	private static Logger getRealLogger(String name) {
		return LogProxy.factory.newLogger(name);
	}

}

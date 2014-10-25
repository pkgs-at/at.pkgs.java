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

package at.pkgs.jetty.logging.log4j;

import org.eclipse.jetty.util.log.Logger;
import at.pkgs.jetty.logging.LogProxy;

public final class Log4jLog implements Logger {

	public static class Factory implements LogProxy.LoggerFactory {

		@Override
		public Logger newLogger(String name) {
			return new Log4jLog(name);
		}

	}

	private final org.apache.logging.log4j.Logger logger;

	private Log4jLog(String name) {
		Thread thread;
		ClassLoader loader;

		thread = Thread.currentThread();
		loader = thread.getContextClassLoader();
		thread.setContextClassLoader(this.getClass().getClassLoader());
		try {
			this.logger = org.apache.logging.log4j.LogManager.getLogger(name);
		}
		finally {
			thread.setContextClassLoader(loader);
		}
	}

	@Override
	public Logger getLogger(String name) {
		return new Log4jLog(name);
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
		this.logger.warn("", thrown);
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
		this.logger.info("", thrown);
	}

	@Override
	public boolean isDebugEnabled() {
		return this.logger.isDebugEnabled();
	}

	@Override
	public void setDebugEnabled(boolean value) {
		this.warn("setDebugEnabled not implemented");
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
		this.logger.debug("", thrown);
	}

	@Override
	public void ignore(Throwable ignored) {
		this.logger.trace("", ignored);
	}

}

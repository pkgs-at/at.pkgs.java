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

package at.pkgs.logging;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

public final class LoggerFactory {

	private static class Solid {

		private final String factory;

		private final String[] requireds;

		private Solid(String factory, String... requireds) {
			this.factory = factory;
			this.requireds = requireds;
		}

		private SinkFactory load() {
			try {
				for (String required : this.requireds)
					Class.forName(required);
				return (SinkFactory)Class.forName(this.factory).newInstance();
			}
			catch (Exception ignored) {
				return null;
			}
		}

	}

	private static final Solid[] SOLIDS = {
		new Solid(
				"at.pkgs.logging.log4j.SolidSinkFactory",
				"org.apache.log4j.LogManager"),
		new Solid(
				"at.pkgs.logging.slf4j.SolidSinkFactory",
				"org.slf4j.LoggerFactory"),
	};

	private static final LoggerFactory INSTANCE = new LoggerFactory();

	private final SinkFactory factory;

	private final ConcurrentMap<String, Logger> logger;

	int depth;

	private LoggerFactory() {
		SinkFactory factory;

		factory = null;
		for (Solid solid : LoggerFactory.SOLIDS) {
			factory = solid.load();
			if (factory != null) break;
		}
		if (factory == null)
			factory = new at.pkgs.logging.pseudo.SolidSinkFactory();
		this.factory = factory;
		this.logger = new ConcurrentHashMap<String, Logger>();
		this.depth = 0;
		this.logger(LoggerFactory.class.getName()).debug(
				"logging by %s",
				this.factory.getClass().getName());
	}

	public LoggerFactory depth(int depth) {
		this.depth = depth;
		return this;
	}

	public Logger logger(String name) {
		Logger logger;

		logger = this.logger.get(name);
		if (logger != null) return logger;
		synchronized (this.logger) {
			if (!this.logger.containsKey(name))
				this.logger.put(
						name,
						new Logger(this.factory.sink(name)));
			return this.logger.get(name);
		}
	}

	public static LoggerFactory get() {
		return LoggerFactory.INSTANCE;
	}

	public static Logger get(String name) {
		return LoggerFactory.get().logger(name);
	}

	public static Logger get(Class<?> type) {
		return LoggerFactory.get(type.getName());
	}

	public static Logger get(Object object) {
		return LoggerFactory.get(object.getClass());
	}

}

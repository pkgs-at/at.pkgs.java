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

	private static final String CLASS_FOR_SLF4J =
			"org.slf4j.LoggerFactory";

	private static final String FACTORY_FOR_SLF4J =
			"at.pkgs.logging.slf4j.SolidSinkFactory";

	private static final LoggerFactory instance = new LoggerFactory();

	private final SinkFactory factory;

	private final ConcurrentMap<String, Logger> logger;

	int depth;

	private LoggerFactory() {
		SinkFactory factory;

		try {
			Class.forName(CLASS_FOR_SLF4J);
			factory = (SinkFactory)Class
					.forName(FACTORY_FOR_SLF4J)
					.newInstance();
		}
		catch (Exception ignored) {
			factory = new at.pkgs.logging.pseudo.SolidSinkFactory();
		}
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
		return instance;
	}

}

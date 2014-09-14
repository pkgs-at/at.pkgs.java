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

package at.pkgs.logging.slf4j;

import org.slf4j.Logger;
import at.pkgs.logging.Level;
import at.pkgs.logging.Sink;

public class SolidSink implements Sink {

	private final Logger logger;

	public SolidSink(Logger logger) {
		this.logger = logger;
	}

	@Override
	public boolean enabled(Level level) {
		switch (level) {
		case Trace :
			return this.logger.isTraceEnabled();
		case Debug :
			return this.logger.isDebugEnabled();
		case Information :
			return this.logger.isInfoEnabled();
		case Warning :
			return this.logger.isWarnEnabled();
		case Error :
			return true;
		case Fatal :
			return true;
		}
		return false;
	}

	@Override
	public void write(Level level, String message, Throwable throwable) {
		switch (level) {
		case Trace :
			this.logger.trace(message, throwable);
			break;
		case Debug :
			this.logger.debug(message, throwable);
			break;
		case Information :
			this.logger.info(message, throwable);
			break;
		case Warning :
			this.logger.warn(message, throwable);
			break;
		case Error :
			this.logger.error(message, throwable);
			break;
		case Fatal :
			this.logger.error(message, throwable);
			break;
		}
	}

}

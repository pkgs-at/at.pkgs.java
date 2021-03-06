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

package at.pkgs.web.duet;

import javax.servlet.ServletContext;
import at.pkgs.logging.Logger;

public abstract class AbstractManager implements Loggable {

	private final Logger logger = Logger.of(this).skip(1);

	private final AbstractApplication application;

	protected AbstractManager(AbstractApplication application) {
		this.application = application;
	}

	@Override
	public Logger logger() {
		return this.logger;
	}

	public AbstractApplication getApplication() {
		return this.application;
	}

	public ServletContext getServletContext() {
		return this.getApplication().getServletContext();
	}

}

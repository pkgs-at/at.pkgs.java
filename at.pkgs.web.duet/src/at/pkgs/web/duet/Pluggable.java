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

import at.pkgs.logging.Logger;
import at.pkgs.web.page.PageHandler;

public class Pluggable implements Loggable {

	private final Logger logger = Logger.of(this).skip(1);

	protected final AbstractPlugin plugin;

	public Pluggable(AbstractPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public Logger logger() {
		return this.logger;
	}

	public String getName() {
		if (this.plugin == null) return "default";
		return this.plugin.getName();
	}

	public Class<? extends PageHandler> getHandlerType(
			Class<? extends PageHandler> base) {
		if (this.plugin == null) return base;
		return this.plugin.getHandlerType(base);
	}

	public String getMasterPage(String base) {
		if (this.plugin == null) return base;
		return this.plugin.getMasterPage(base);
	}

}

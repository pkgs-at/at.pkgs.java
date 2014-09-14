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

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import at.pkgs.logging.Logger;
import at.pkgs.web.page.PageHandler;

public abstract class AbstractPlugin implements Loggable {

	public static abstract class Listener
	implements ServletContextListener, Loggable {

		private final Logger logger = Logger.of(this).skip(1);

		@Override
		public Logger logger() {
			return this.logger;
		}

		protected abstract AbstractApplication getContext();

		protected abstract Class<? extends AbstractPlugin> getPluginType();

		@Override
		public void contextInitialized(ServletContextEvent event) {
			AbstractPlugin plugin;

			try {
				this.getContext().initialize(event.getServletContext());
			}
			catch (ServletException throwable) {
				this.error(throwable, "failed on context initialize");
				throw new RuntimeException(throwable);
			}
			try {
				plugin = this.getPluginType().newInstance();
			}
			catch (InstantiationException | IllegalAccessException throwable) {
				this.error(throwable, "failed on create plugin instance");
				throw new RuntimeException(throwable);
			}
			this.getContext().getPluginManager().register(plugin);
		}

		@Override
		public void contextDestroyed(ServletContextEvent event) {
			// do nothing
		}

	}

	public static class Mapping<Type> {

		private final Map<Type, Type> raw;

		private Map<Type, Type> frozen;

		public Mapping() {
			this.raw = new HashMap<Type, Type>();
			this.frozen = null;
		}

		public void map(Type base, Type override) {
			if (this.frozen != null) throw new IllegalStateException();
			this.raw.put(base, override);
		}

		public void freeze() {
			if (this.frozen != null) throw new IllegalStateException();
			this.frozen = Collections.unmodifiableMap(this.raw);
		}

		public Type get(Type base) {
			if (base == null || !this.frozen.containsKey(base)) return base;
			else return this.frozen.get(base);
		}

	}

	private final Logger logger = Logger.of(this).skip(1);

	private final Mapping <Class<? extends PageHandler>> handlers;

	private final Mapping <String> masterPages;

	public AbstractPlugin() {
		this.handlers = new Mapping<Class<? extends PageHandler>>();
		this.masterPages = new Mapping<String>();
		this.configure();
		this.handlers.freeze();
		this.masterPages.freeze();
	}

	@Override
	public Logger logger() {
		return this.logger;
	}

	protected abstract void configure();

	public abstract String getName();

	public Class<? extends PageHandler> getHandlerType(
			Class<? extends PageHandler> base) {
		return this.handlers.get(base);
	}

	public String getMasterPage(String base) {
		return this.masterPages.get(base);
	}

}

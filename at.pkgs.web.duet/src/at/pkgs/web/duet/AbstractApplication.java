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

import java.io.InputStream;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractApplication implements Loggable {

	private final Logger logger = LogManager.getLogger(this);

	private boolean initialized;

	private ServletContext context;

	private ConfigurationManager configuration;

	private LoggingManager logging;

	private TemplateManager template;

	private PluginManager plugin;

	private ServletManager servlet;

	protected AbstractApplication() {
		this.initialized = false;
	}

	@Override
	public Logger logger() {
		return this.logger;
	}

	protected TemplateManager createTemplateManager()
			throws ServletException {
		return new TemplateManager(this);
	}

	protected PluginManager createPluginManager()
			throws ServletException {
		return new PluginManager(this);
	}

	protected ServletManager createServletManager()
			throws ServletException {
		return new ServletManager(this);
	}

	protected void initialize()
			throws ServletException {
		// do nothing
	}

	public final void initialize(ServletContext context)
			throws ServletException {
		synchronized (this) {
			if (this.initialized) return;
			this.info("Application initializing");
			this.context = context;
			try {
				this.configuration = new ConfigurationManager(this);
				this.logging = new LoggingManager(this);
				this.template = this.createTemplateManager();
				this.plugin = this.createPluginManager();
				this.servlet = this.createServletManager();
				this.initialize();
			}
			catch (ServletException throwable) {
				this.fatal(throwable, "failed on Application initialize");
				throw throwable;
			}
			catch (Throwable throwable) {
				this.fatal(throwable, "failed on Application initialize");
				throw new ServletException(throwable);
			}
			finally {
				this.initialized = true;
			}
			this.info("Application initialized");
		}
	}

	public ServletContext getServletContext() {
		return this.context;
	};

	public String getProperty(String name, String... namesAndDefault) {
		int length;
		StringBuilder builder;

		length = namesAndDefault.length - 1;
		builder = new StringBuilder();
		builder.append(this.getClass().getPackage().getName());
		builder.append('.');
		builder.append(name);
		for (int index = 0; index < length; index ++) {
			builder.append('.');
			builder.append(namesAndDefault[index]);
		}
		return System.getProperty(builder.toString(), namesAndDefault[length]);
	}

	public InputStream getResourceAsStream(String name) {
		return this.getClass().getResourceAsStream(name);
	}

	public ConfigurationManager getConfigurationManager() {
		return this.configuration;
	}

	public LoggingManager getLoggingManager() {
		return this.logging;
	}

	public TemplateManager getTemplateManager() {
		return this.template;
	}

	public PluginManager getPluginManager() {
		return this.plugin;
	}

	public ServletManager getServletManager() {
		return this.servlet;
	}

}

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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.CompositeConfiguration;
import at.pkgs.logging.Logger;
import at.pkgs.web.page.PageHandler;
import at.pkgs.web.page.PageRequest;
import at.pkgs.web.page.PageResponse;
import net.arnx.jsonic.JSON;

public abstract class AbstractPage<ModelType>
extends PageHandler implements Loggable {

	public static abstract class Servlet
	extends PageHandler.Servlet implements Loggable {

		private static final long serialVersionUID = 1L;

		protected abstract AbstractApplication getApplication();

		private final Logger logger = Logger.of(this).skip(1);

		@Override
		public Logger logger() {
			return this.logger;
		}

		@Override
		public void init() throws ServletException {
			super.init();
			this.getApplication().initialize(this.getServletContext());
		}

		protected abstract Class<? extends PageHandler> getBaseHandlerType();

		protected abstract String getPluginName(
				PageRequest request,
				PageResponse response)
						throws ServletException, IOException;

		@Override
		protected Class<? extends PageHandler> getHandlerType(
				PageRequest request,
				PageResponse response)
						throws ServletException, IOException {
			Pluggable pluggable;

			pluggable = this.getApplication()
					.getPluginManager()
					.get(this.getPluginName(request, response));
			request.setAttribute("pluggable", pluggable);
			return pluggable.getHandlerType(this.getBaseHandlerType());
		}

		@Override
		protected void service(
				PageRequest request,
				PageResponse response)
						throws ServletException, IOException {
			try {
				super.service(request, response);
			}
			catch (Throwable throwable) {
				this.error(
						throwable,
						"error on request %s", request.getRequestURI());
				throw throwable;
			}
		}

	}

	private final Logger logger = Logger.of(this).skip(1);

	private Pluggable pluggable;

	private ModelType model;

	@Override
	public Logger logger() {
		return this.logger;
	}

	@Override
	public Servlet getServlet() {
		return (Servlet)super.getServlet();
	}

	protected AbstractApplication getApplication() {
		return this.getServlet().getApplication();
	}

	public Pluggable getPluggable() {
		return this.pluggable;
	}

	public Configuration getConfiguration(String prefix) {
		CompositeConfiguration configuration;

		configuration = new CompositeConfiguration();
		configuration.addConfiguration(
				this.getApplication().getConfigurationManager().get(
						prefix + "." + this.pluggable.getName()));
		configuration.addConfiguration(
				this.getApplication().getConfigurationManager().get(
						prefix));
		return configuration;
	}

	@SuppressWarnings("unchecked")
	protected Class<? extends ModelType> getModelType() {
		return (Class<? extends ModelType>)Void.class;
	}

	public ModelType getModel() {
		return this.model;
	}

	public String formatModel() {
		return JSON.encode(this.getModel(), true);
	}

	protected abstract String getMasterPage();

	protected abstract String getPage();

	@Override
	protected void initialize()
			throws ServletException, IOException {
		super.initialize();
		if (this.finished()) return;
		this.pluggable =
				(Pluggable)this.getRequest().getAttribute("pluggable");
		this.model = null;
		if (this.getModelType() != Void.class) {
			String data;

			data = this.getRequest().getParameter("data");
			if (data != null) {
				this.model = JSON.decode(data, this.getModelType());
			}
			else {
				try {
					this.model = this.getModelType().newInstance();
				}
				catch (Throwable throwable) {
					this.error(throwable, "failed on create model instance");
					throw new ServletException(throwable);
				}
			}
		}
		this.getResponse().setParameter("handler", this);
	}

	protected void render()
			throws ServletException, IOException {
		String master;
		String page;

		master = this.getPluggable().getMasterPage(this.getMasterPage());
		if (master != null) {
			this.getResponse().setParameter(
					"master_content",
					this.getApplication()
							.getTemplateManager()
							.get(this.getPage())
							.render(this.getResponse().getParameterMap()));
			page = master;
		}
		else {
			page = this.getPage();
		}
		this.getResponse().sendResponse(
				"text/html; charset=UTF-8",
				this.getApplication()
						.getTemplateManager()
						.get(page)
						.render(this.getResponse().getParameterMap()),
				StandardCharsets.UTF_8);
	}

	protected void service()
			throws ServletException, IOException {
		throw new UnsupportedOperationException();
	}

	protected void doGet()
			throws ServletException, IOException {
		this.service();
	}

	protected void doPost()
			throws ServletException, IOException {
		this.service();
	}

	@Override
	protected void handle()
			throws ServletException, IOException {
		switch (this.getRequest().getMethod().toUpperCase()) {
		case "GET":
			this.doGet();
			break;
		case "POST":
			this.doPost();
			break;
		default:
			this.service();
			break;
		}
	}

}

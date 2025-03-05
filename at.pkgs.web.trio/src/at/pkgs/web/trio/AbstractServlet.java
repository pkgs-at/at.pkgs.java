/*
 * Copyright (c) 2009-2015, Architector Inc., Japan
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

package at.pkgs.web.trio;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import at.pkgs.template.Template;
import at.pkgs.template.TemplateFactory;
import at.pkgs.template.ResourceProvider;
import at.pkgs.template.ResourceProviderResolver;
import at.pkgs.web.http.HttpRequest;
import at.pkgs.web.http.HttpResponse;

public abstract class AbstractServlet
extends HttpServlet implements ContextHolder {

	public static class Resolver extends ResourceProviderResolver {

		public Resolver(final ServletContext context, Charset charset) {
			super(new ResourceProvider() {

				@Override
				public URL getResource(String path) {
					try {
						return context.getResource(path);
					}
					catch (MalformedURLException cause) {
						throw new RuntimeException(cause);
					}
				}

			}, charset);
		}

		public Resolver(ServletContext context) {
			this(context, null);
		}

	}

	private static final long serialVersionUID = 1L;

	private static volatile TemplateFactory templateFactory;

	private ServletContext context;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.context = config.getServletContext();
	}

	@Override
	public ServletContext getContext() {
		return this.context;
	}

	protected TemplateFactory newTemplateFactory() {
		try {
			TemplateFactory factory;

			factory = new TemplateFactory(
					new Resolver(this.getContext()),
					ContextParameter.get().getTemplateCache());
			for (String include : ContextParameter.get().getTemplateIncludes())
				factory.getTemplateEngine().include(include);
			return factory;
		}
		catch (IOException cause) {
			throw new RuntimeException(cause);
		}
	}

	private TemplateFactory getTemplateFactory() {
		if (AbstractServlet.templateFactory == null) {
			synchronized (this.getClass()) {
				if (AbstractServlet.templateFactory == null) {
					AbstractServlet.templateFactory =
							this.newTemplateFactory();
				}
			}
		}
		return AbstractServlet.templateFactory;
	}

	protected Template getTemplate(String path)
			throws IOException {
		return this.getTemplateFactory().getTemplate(path);
	}

	protected AbstractHandler newDefaultHandler(
			final HttpRequest request,
			final HttpResponse response)
					throws ServletException, IOException {
		return new AbstractHandler() {

			@Override
			protected void handle()
					throws ServletException, IOException {
				// do nothing
			}

		};
	}

	protected void service(
			final HttpRequest request,
			final HttpResponse response)
					throws ServletException, IOException {
		String path;
		String pathInfo;
		Template template;
		String content;
		String contentType;

		if (response.getParameter("$") == null) {
			AbstractHandler handler;

			handler = this.newDefaultHandler(request, response);
			response.setParameter("$", handler);
			try {
				handler.initialize(this, request, response);
				if (handler.finished()) return;
				handler.handle();
				if (handler.finished()) return;
				handler.complete();
				if (handler.finished()) return;
			}
			catch (Exception cause) {
				handler.trap(cause);
				if (handler.finished()) return;
			}
		}
		path = request.getServletPath();
		pathInfo = request.getPathInfo();
		if (pathInfo != null) path += pathInfo;
		try {
			template = this.getTemplate(path);
		}
		catch (FileNotFoundException ignored) {
			response.sendError(HttpResponse.SC_NOT_FOUND);
			return;
		}
		content = template.render(response.getParameterMap());
		contentType = response.getContentType();
		if (contentType == null) contentType = "text/html";
		response.sendResponse(contentType, content);
	}

	@Override
	protected void service(
			HttpServletRequest request,
			HttpServletResponse response)
					throws ServletException, IOException {
		HttpRequest wrapped;

		if (request instanceof HttpRequest)
			wrapped = (HttpRequest)request;
		else
			wrapped = new HttpRequest(request);
		this.service(
				wrapped,
				response instanceof HttpResponse ?
						(HttpResponse)response :
						new HttpResponse(wrapped, response));
	}

}

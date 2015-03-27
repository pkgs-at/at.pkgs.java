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

import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import at.pkgs.template.TemplateResource;
import at.pkgs.template.TemplateResolver;
import at.pkgs.template.InputStreamResource;
import at.pkgs.template.TemplateEngine;
import at.pkgs.template.Template;
import at.pkgs.web.http.HttpRequest;
import at.pkgs.web.http.HttpResponse;

public abstract class AbstractServlet
extends HttpServlet implements ContextHolder {

	public class Resolver implements TemplateResolver {

		@Override
		public TemplateResource getResource(
				final String path)
						throws IOException {
			final URL url;

			url = AbstractServlet.this.context.getResource(path);
			if (url == null)
				throw new FileNotFoundException(
						"resource not found: " +
						path);
			return new InputStreamResource(
					AbstractServlet.this.getTemplateCharset()) {

				@Override
				public String getLocation()
						throws IOException {
					return url.toString();
				}

				@Override
				protected InputStream open() throws IOException {
					return url.openStream();
				}

			};
		}

	}

	private static final long serialVersionUID = 1L;

	private ServletContext context;

	private TemplateEngine engine;

	protected void include(String path) throws IOException {
		URL url;

		url = this.context.getResource(path);
		if (url == null)
			throw new FileNotFoundException(
					"resource not found: " +
					path);
		try (InputStream input = url.openStream()) {
			this.engine.include(input);
		}
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		String includes;

		super.init(config);
		this.context = config.getServletContext();
		includes = this.context.getInitParameter(
				this.getClass().getPackage().getName() +
				".template.includes");
		try {
			this.engine = new TemplateEngine(new Resolver());
			if (includes != null) {
				for (String path : includes.split("\\s+")) {
					if (path.length() <= 0) continue;
					this.include(path);
				}
			}
			// TODO debug no-cache
		}
		catch (IOException cause) {
			throw new ServletException(cause);
		}
	}

	@Override
	public ServletContext getContext() {
		return this.context;
	}

	protected Charset getTemplateCharset() {
		return StandardCharsets.UTF_8;
	}

	protected Template getTemplate(String path)
			throws IOException {
		// TODO cache
		return this.engine.template(path);
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
			handler.initialize(this, request, response);
			if (handler.finished()) return;
			handler.handle();
			if (handler.finished()) return;
			handler.complete();
			if (handler.finished()) return;
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

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
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import at.pkgs.template.Template;
import at.pkgs.web.http.HttpRequest;
import at.pkgs.web.http.HttpResponse;

public abstract class AbstractServlet
extends HttpServlet implements ContextHolder {

	private static final long serialVersionUID = 1L;

	private ServletContext context;

	private volatile TemplateFactory templateFactory;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.context = config.getServletContext();
	}

	@Override
	public ServletContext getContext() {
		return this.context;
	}

	protected TemplateFactory getTemplateFactory() {
		if (this.templateFactory == null) {
			synchronized (this) {
				if (this.templateFactory == null)
					this.templateFactory = new TemplateFactory(this.context);
			}
		}
		return this.templateFactory;
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

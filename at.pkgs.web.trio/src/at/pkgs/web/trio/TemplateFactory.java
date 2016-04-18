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

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import javax.servlet.ServletContext;
import at.pkgs.template.TemplateResolver;
import at.pkgs.template.TemplateResource;
import at.pkgs.template.InputStreamResource;
import at.pkgs.template.TemplateEngine;
import at.pkgs.template.Template;

public class TemplateFactory {

	public class Resolver implements TemplateResolver {

		@Override
		public TemplateResource getResource(
				final String path)
						throws IOException {
			final URL url;

			url = TemplateFactory.this.getResource(path);
			if (url == null)
				throw new FileNotFoundException(
						"resource not found: " +
						path);
			return new InputStreamResource(
					TemplateFactory.this.getTemplateCharset()) {

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

	private final ServletContext context;

	private final boolean cache;

	private volatile TemplateEngine engine;

	private final ConcurrentMap<String, Template> templates;

	public TemplateFactory(
			ServletContext context,
			boolean cache) {
		this.context = context;
		this.cache = cache;
		this.engine = null;
		this.templates = new ConcurrentHashMap<String, Template>();
	}

	public TemplateFactory(
			ServletContext context) {
		this(context, true);
	}

	protected ServletContext getServletContext() {
		return this.context;
	}

	protected URL getResource(String path) throws IOException {
		return this.context.getResource(path);
	}

	protected Charset getTemplateCharset() {
		return StandardCharsets.UTF_8;
	}

	protected TemplateEngine createTemplateEngine() throws IOException {
		return new TemplateEngine(new Resolver());
	}

	protected TemplateEngine getTemplateEngine() throws IOException {
		if (!this.cache) return this.createTemplateEngine();
		if (this.engine == null) {
			synchronized (this) {
				if (this.engine == null)
					this.engine = this.createTemplateEngine();
			}
		}
		return this.engine;
	}

	protected Template createTemplate(String path) throws IOException {
		return this.getTemplateEngine().template(path);
	}

	public Template getTemplate(String path) throws IOException {
		if (!this.cache) return this.createTemplate(path);
		if (!this.templates.containsKey(path))
			this.templates.putIfAbsent(path, TemplateFactory.this.createTemplate(path));
		return this.templates.get(path);
	}

}

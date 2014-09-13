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

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.io.InputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.commons.configuration.Configuration;
import at.pkgs.template.TemplateEngine;
import at.pkgs.template.Template;
import at.pkgs.template.ClasspathResolver;

public class TemplateManager extends AbstractManager {

	private final class Cache {

		private final String path;

		private Template value;

		private Cache(String path) {
			this.path = path;
			this.value = null;
		}

		private Template get() throws IOException {
			if (this.value != null) return this.value;
			synchronized (this) {
				if (this.value != null) return this.value;
				this.value = TemplateManager.this.engine.template(path);
			}
			return this.value;
		}

	}

	private final Configuration configuration;

	private final TemplateEngine engine;

	private final ConcurrentMap<String, Cache> cache;

	public TemplateManager(AbstractApplication application)
			throws ServletException {
		super(application);
		this.configuration = this.getApplication()
				.getConfigurationManager()
				.get("TemplateManager");
		try {
			ClasspathResolver resolver;

			resolver = new ClasspathResolver(
					this.getApplication()
							.getClass()
							.getPackage()
							.getName()
							.replace('.', '/'),
					this.getClass().getClassLoader());
			this.engine = new TemplateEngine(resolver);
			try (InputStream input =
					this.getApplication().getResourceAsStream(
							"Template.js")) {
				this.engine.include(input);
			}
		}
		catch (IOException throwable) {
			this.error(throwable, "failed on initialize template context");
			throw new ServletException(throwable);
		}
		if (this.configuration.getBoolean("disableCache", false))
			this.cache = null;
		else
			this.cache = new ConcurrentHashMap<String, Cache>();
	}

	protected String getProperty(String name, String value) {
		return this.getApplication().getProperty(
				"TemplateManager",
				name,
				value);
	}

	public Template get(String path) throws IOException {
		if (this.cache != null && this.cache.containsKey(path))
			return this.cache.get(path).get();
		this.debug("prepare template %s", path);
		if (this.cache == null)
			return this.engine.template(path);
		this.cache.putIfAbsent(path, new Cache(path));
		return this.cache.get(path).get();
	}

}

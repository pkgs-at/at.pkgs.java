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

package at.pkgs.template;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.io.IOException;

public class TemplateFactory {

	private final TemplateEngine engine;

	private final boolean cache;

	private final ConcurrentMap<String, Template> templates;

	public TemplateFactory(
			TemplateResolver resolver,
			boolean cache)
					throws IOException {
		this.engine = new TemplateEngine(resolver);
		this.cache = cache;
		this.templates = new ConcurrentHashMap<String, Template>();
	}

	public TemplateFactory(
			TemplateResolver resolver)
					throws IOException {
		this(resolver, true);
	}

	public TemplateEngine getTemplateEngine () {
		return this.engine;
	}

	public Template getTemplate(String path) throws IOException {
		if (!this.cache) return this.engine.template(path);
		if (!this.templates.containsKey(path))
			this.templates.putIfAbsent(path, this.engine.template(path));
		return this.templates.get(path);
	}

}

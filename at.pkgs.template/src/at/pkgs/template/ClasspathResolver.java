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

package at.pkgs.template;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ClasspathResolver implements TemplateResolver {

	private final String[] roots;

	private final ClassLoader loader;

	private final Charset charset;

	public ClasspathResolver(
			String[] roots,
			ClassLoader loader,
			Charset charset) {
		this.roots = new String[roots.length];
		for (int index = 0; index < roots.length; index ++) {
			StringBuilder builder;

			builder = new StringBuilder(roots[index]);
			if (!roots[index].endsWith("/"))
				builder.append('/');
			while (builder.length() > 0 && builder.charAt(0) == '/')
				builder.deleteCharAt(0);
			this.roots[index] = builder.toString();
		}
		if (loader == null)
			loader = Thread.currentThread().getContextClassLoader();
		this.loader = loader;
		if (charset == null)
			charset = StandardCharsets.UTF_8;
		this.charset = charset;
	}

	public ClasspathResolver(
			String root,
			ClassLoader loader,
			Charset charset) {
		this(new String[] {root}, loader, charset);
	}

	public ClasspathResolver(
			String[] roots,
			ClassLoader loader) {
		this(roots, loader, null);
	}

	public ClasspathResolver(
			String root,
			ClassLoader loader) {
		this(new String[] {root}, loader, null);
	}

	public ClasspathResolver(
			String[] roots,
			Charset charset) {
		this(roots, null, charset);
	}

	public ClasspathResolver(
			String root,
			Charset charset) {
		this(new String[] {root}, null, charset);
	}

	public ClasspathResolver(
			String[] roots) {
		this(roots, null, null);
	}

	public ClasspathResolver(
			String root) {
		this(new String[] {root}, null, null);
	}

	@Override
	public TemplateResource getResource(String path) throws IOException {
		if (path.startsWith("/")) path = path.substring(1);
		for (String root : this.roots) {
			String resource;

			resource = root + path;
			if (this.loader.getResource(resource) != null)
				return new ClasspathResource(
						resource,
						this.loader,
						this.charset);
		}
		return null;
	}

}

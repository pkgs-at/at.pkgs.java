/*
 * Copyright (c) 2009-2016, Architector Inc., Japan
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
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ResourceProviderResolver implements TemplateResolver {

	private final ResourceProvider provider;

	private final Charset charset;

	public ResourceProviderResolver(
			ResourceProvider provider,
			Charset charset) {
		this.provider = provider;
		if (charset == null)
			charset = StandardCharsets.UTF_8;
		this.charset = charset;
	}

	public ResourceProviderResolver(
			ResourceProvider provider) {
		this(provider, null);
	}

	@Override
	public TemplateResource getResource(String path) throws IOException {
		final URL resource;

		resource = this.provider.getResource(path);
		if (resource == null) return null;
		return new InputStreamResource(this.charset) {

			@Override
			protected InputStream open() throws IOException {
				return resource.openStream();
			}

			@Override
			public String getLocation() throws IOException {
				return resource.toString();
			}

		};
	}

}

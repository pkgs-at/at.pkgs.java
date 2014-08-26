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

import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class ClasspathResource extends InputStreamResource {

	private final String path;

	private final ClassLoader loader;

	public ClasspathResource(
			String path,
			ClassLoader loader,
			Charset charset) {
		super(charset);
		this.path = path;
		this.loader = loader;
	}

	@Override
	protected InputStream open() throws IOException {
		return this.loader.getResourceAsStream(this.path);
	}

	@Override
	public String getLocation() throws IOException {
		return "classpath:/" + this.path;
	}

}

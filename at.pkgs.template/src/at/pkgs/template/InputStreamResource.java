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
import java.io.BufferedInputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public abstract class InputStreamResource implements TemplateResource {

	private final Charset charset;

	public InputStreamResource(Charset charset) {
		this.charset = charset;
	}

	protected abstract InputStream open() throws IOException;

	@Override
	public String getContent() throws IOException {
		StringBuilder builder;

		builder = new StringBuilder();
		try (Reader reader = new InputStreamReader(
				new BufferedInputStream(this.open()),
				this.charset)) {
			CharBuffer buffer;

			buffer = CharBuffer.allocate(1024);
			while (reader.read(buffer) >= 0) {
				buffer.flip();
				builder.append(buffer.toString());
				buffer.clear();
			}
		}
		return builder.toString();
	}

}

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

package at.pkgs.template.preprocessor;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.IOException;
import java.io.FileNotFoundException;
import at.pkgs.template.TemplateResolver;
import at.pkgs.template.TemplateResource;
import at.pkgs.template.TemplateException;

public class TemplateParser {

	public static final int MAX_DEPTH = 200;

	private static final Pattern PATTERN_TAG = Pattern.compile(
			"\\[\\?(.*?)(\\??)\\?\\]");

	private static final Pattern PATTERN_NEWLINE = Pattern.compile(
			"\\r?\\n");

	private final TemplateResolver resolver;

	private final TemplateParserContext context;

	private int count;

	public TemplateParser(TemplateResolver resolver) {
		this.resolver = resolver;
		this.context = new TemplateParserContext();
		this.count = 0;
	}

	protected void process(String opecode) throws IOException {
		if (opecode.equals("[")) {
			this.context.append("[");
			return;
		}
		if (opecode.equals("]")) {
			this.context.append("]");
			return;
		}
		if (opecode.endsWith("{")) {
			this.context.enter(
					opecode.substring(0, opecode.length() - 1).trim());
			return;
		}
		if (opecode.startsWith("}")) {
			this.context.leave();
			return;
		}
		if (opecode.equals("base")) {
			this.context.base();
		}
		if (opecode.startsWith("extends ")) {
			this.parse(opecode.substring(8).trim());
			return;
		}
		if (opecode.startsWith("include ")) {
			this.parse(opecode.substring(8).trim());
			return;
		}
	}

	public void parse(TemplateResource resource) throws IOException {
		int depth;
		String source;
		int offset;
		Matcher matcher;

		this.count ++;
		if (this.count > MAX_DEPTH)
			throw new TemplateException("too many template include / extends");
		depth = this.context.depth();
		source = resource.getContent();
		offset = 0;
		matcher = PATTERN_TAG.matcher(source);
		while (matcher.find(offset)) {
			int position;

			position = matcher.start();
			this.context.append(source.substring(offset, position));
			offset = matcher.end();
			try {
				this.process(matcher.group(1).trim());
				if (matcher.group(2).length() > 0) {
					if (source.charAt(offset) == '\r') offset ++;
					if (source.charAt(offset) == '\n') offset ++;
				}
			}
			catch (TemplateException throwable) {
				int line;
				int start;
				Matcher counter;

				line = 0;
				start = 0;
				counter = PATTERN_NEWLINE.matcher(source);
				while (start <= position && counter.find(start)) {
					line ++;
					start = counter.end();
				}
				throw new TemplateException(String.format(
						"template error at %s:%d",
						resource.getLocation(),
						line),
						throwable);
			}
		}
		if (this.context.depth() != depth)
			throw new TemplateException(String.format(
					"template error: depth missmatch at %s",
					resource.getLocation()));
		this.context.append(source.substring(offset));
	}

	public void parse(String path) throws IOException {
		TemplateResource resource;

		resource = this.resolver.getResource(path);
		if (resource == null)
			throw new FileNotFoundException(path);
		this.parse(resource);
	}

	public String content() {
		return this.context.toString();
	}

}

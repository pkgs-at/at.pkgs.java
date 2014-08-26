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

import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FileResolver implements TemplateResolver {

	private final File[] roots;

	private final Charset charset;

	public FileResolver(File[] roots, Charset charset) {
		this.roots = roots;
		if (charset == null)
			charset = StandardCharsets.UTF_8;
		this.charset = charset;
	}

	public FileResolver(File[] roots) {
		this(roots, (Charset)null);
	}

	public FileResolver(File root, Charset charset) {
		this(new File[] {root}, charset);
	}

	public FileResolver(File root) {
		this(root, (Charset)null);
	}

	public FileResolver(String[] roots, Charset charset) {
		this(filesFromPaths(roots), charset);
	}

	public FileResolver(String[] roots) {
		this(roots, (Charset)null);
	}

	public FileResolver(String root, Charset charset) {
		this(new File[] {new File(root)}, charset);
	}

	public FileResolver(String root) {
		this(root, (Charset)null);
	}

	@Override
	public TemplateResource getResource(String path) throws IOException {
		for (File root : this.roots) {
			File file;

			file = new File(root, path);
			if (file.canRead())
				return new FileResource(file, this.charset);
		}
		return null;
	}

	private static final File[] EMPTY_FILES = new File[0];

	private static File[] filesFromPaths(String[] paths) {
		List<File> list;

		list = new ArrayList<File>();
		for (String path : paths) list.add(new File(path));
		return list.toArray(EMPTY_FILES);
	}

}

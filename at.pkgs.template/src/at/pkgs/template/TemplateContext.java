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

import java.util.Map;
import java.util.HashMap;

public class TemplateContext extends HashMap<String, Object> {

	public static class Entry extends SimpleImmutableEntry<String, Object> {

		private static final long serialVersionUID = 1L;

		public Entry(String name, Object value) {
			super(name, value);
		}

	}

	private static final long serialVersionUID = 1L;

	public TemplateContext(Map<? extends String, ? extends Object> map) {
		super(map);
	}

	public TemplateContext(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public TemplateContext(int initialCapacity) {
		super(initialCapacity);
	}

	public TemplateContext() {
		super();
	}

	public TemplateContext(Entry... entries) {
		super();
		for (Entry entry : entries)
			this.put(entry.getKey(), entry.getValue());
	}

}

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

import java.util.List;
import java.util.ArrayList;

public class NestedSection implements Section {

	private BaseSection base;

	private List<Section> list;

	public NestedSection() {
		this.base = null;
		this.list = new ArrayList<Section>();
	}

	public void append(Section section) {
		this.list.add(section);
	}

	public void extend() {
		this.base = new BaseSection(this.list);
		this.list = new ArrayList<Section>();
	}

	public void base() {
		if (this.base == null) return;
		this.append(base);
	}

	@Override
	public void render(StringBuilder builder) {
		for (Section child : this.list)
			child.render(builder);
	}

}

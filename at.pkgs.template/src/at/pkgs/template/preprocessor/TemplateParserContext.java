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

import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.HashMap;
import at.pkgs.template.TemplateException;

public class TemplateParserContext {

	private static final int MAX_DEPTH = 100;

	private final NestedSection root;

	private final Deque<NestedSection> stack;

	private final Map<String, NestedSection> map;

	public TemplateParserContext() {
		this.stack = new ArrayDeque<NestedSection>();
		this.map = new HashMap<String, NestedSection>();
		this.root = new NestedSection();
		this.push(this.root);
	}

	@Override
	public String toString() {
		StringBuilder builder;

		builder = new StringBuilder();
		this.root.render(builder);
		return builder.toString();
	}

	protected NestedSection current() {
		return this.stack.getFirst();
	}

	protected NestedSection pop() {
		return this.stack.pop();
	}

	protected void push(NestedSection node) {
		this.stack.push(node);
	}

	public int depth() {
		return this.stack.size();
	}

	protected NestedSection get(String name) {
		return this.map.get(name);
	}

	protected void put(String name, NestedSection node) {
		this.map.put(name, node);
	}

	public void append(String content) {
		this.current().append(new TextSection(content));
	}

	public void enter(String name) {
		NestedSection node;

		if (this.stack.size() > MAX_DEPTH) throw new TemplateException("template nesting too deep");
		node = this.get(name);
		if (node == null) {
			node = new NestedSection();
			this.current().append(node);
			this.put(name, node);
		}
		else {
			node.extend();
		}
		this.push(node);
	}

	public void base() {
		this.current().base();
	}

	public void leave() {
		if (this.current() == this.root) throw new TemplateException("template stete error");
		this.pop();
	}

}

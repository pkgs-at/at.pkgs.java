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

package at.pkgs.web.http;

import java.util.Enumeration;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class HttpRequest extends HttpServletRequestWrapper {

	private final EntryIterable<Object> attributeIterable;

	private final EntryIterable<String> headerIterable;

	private final EntryIterable<String> parameterIterable;

	public HttpRequest(HttpServletRequest request) {
		super(request);
		this.attributeIterable =
				new EntryIterable<Object>(
						new EntryIterable.Keys() {

							@Override
							public Enumeration<String> get() {
								return HttpRequest.this.getAttributeNames();
							}

						},
						new EntryIterable.Values<Object>() {

							@Override
							public Object get(String key) {
								return HttpRequest.this.getAttribute(key);
							}

						});
		this.headerIterable =
				new EntryIterable<String>(
						new EntryIterable.Keys() {

							@Override
							public Enumeration<String> get() {
								return HttpRequest.this.getHeaderNames();
							}

						},
						new EntryIterable.Values<String>() {

							@Override
							public String get(String key) {
								return HttpRequest.this.getHeader(key);
							}

						});
		this.parameterIterable =
				new EntryIterable<String>(
						new EntryIterable.Keys() {

							@Override
							public Enumeration<String> get() {
								return HttpRequest.this.getParameterNames();
							}

						},
						new EntryIterable.Values<String>() {

							@Override
							public String get(String key) {
								return HttpRequest.this.getParameter(key);
							}

						});
	}

	public Iterable<Entry<String, Object>> getAttributeIterable() {
		return this.attributeIterable;
	}

	public Iterable<Entry<String, String>> getHeaderIterable() {
		return this.headerIterable;
	}

	public Iterable<Entry<String, String>> getParameterIterable() {
		return this.parameterIterable;
	}

	public boolean methodIs(String method) {
		return this.getMethod().toUpperCase().equals(method.toUpperCase());
	}

}

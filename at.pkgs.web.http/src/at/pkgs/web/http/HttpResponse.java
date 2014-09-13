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
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Collections;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import java.net.URLConnection;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class HttpResponse extends HttpServletResponseWrapper {

	private final HttpRequest request;

	private final Map<String, Object> attributes;

	private final EntryIterable<Object> attributeIterable;

	private final EntryIterable<String> headerIterable;

	private final Map<String, Object> parameters;

	private final EntryIterable<Object> parameterIterable;

	public HttpResponse(HttpRequest request, HttpServletResponse response) {
		super(response);
		this.request = request;
		this.attributes = new HashMap<String, Object>();
		this.attributeIterable = new EntryIterable<Object>(this.attributes);
		this.headerIterable =
				new EntryIterable<String>(
						new EntryIterable.Keys() {

							@Override
							public Enumeration<String> get() {
								return Collections.enumeration(
										HttpResponse.this.getHeaderNames());
							}

						},
						new EntryIterable.Values<String>() {

							@Override
							public String get(String key) {
								return HttpResponse.this.getHeader(key);
							}

						});
		this.parameters = new HashMap<String, Object>();
		this.parameterIterable = new EntryIterable<Object>(this.parameters);
	}

	public HttpRequest getRequest() {
		return this.request;
	}

	public Object getAttribute(String name) {
		if (!this.attributes.containsKey(name))
			return null;
		else
			return this.attributes.get(name);
	}

	public Enumeration<String> getAttributeNames() {
		return Collections.enumeration(this.attributes.keySet());
	}

	public Iterable<Entry<String, Object>> getAttributeIterable() {
		return this.attributeIterable;
	}

	public Map<String, Object> getAttributeMap() {
		return this.attributes;
	}

	public void setAttribute(String name, Object value) {
		this.attributes.put(name, value);
	}

	public void removeAttribute(String name) {
		if (this.attributes.containsKey(name))
			this.attributes.remove(name);
	}

	public Iterable<Entry<String, String>> getHeaderIterable() {
		return this.headerIterable;
	}

	public Object getParameter(String name) {
		if (!this.parameters.containsKey(name))
			return null;
		else
			return this.parameters.get(name);
	}

	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(this.parameters.keySet());
	}

	public Iterable<Entry<String, Object>> getParameterIterable() {
		return this.parameterIterable;
	}

	public Map<String, Object> getParameterMap() {
		return this.parameters;
	}

	public void setParameter(String name, Object value) {
		this.parameters.put(name, value);
	}

	public void removeParameter(String name) {
		if (this.parameters.containsKey(name))
			this.parameters.remove(name);
	}

	public void setContentLength(long length) {
		boolean supported;
		ServletContext context;

		context = this.getRequest().getServletContext();
		if (context.getMajorVersion() > 3)
			supported = true;
		else if (context.getMajorVersion() < 3)
			supported = false;
		else if (context.getMinorVersion() < 1)
			supported = false;
		else
			supported = true;
		if (!supported && length > (long)Integer.MAX_VALUE) return;
		if (supported)
			super.setContentLengthLong(length);
		else
			super.setContentLength((int)length);
	}

	public void sendResponse(String contentType, byte[] data)
			throws IOException {
		this.setContentType(contentType);
		this.setContentLength(data.length);
		this.getOutputStream().write(data);
	}

	public void sendResponse(String contentType, String text, Charset charset)
			throws IOException {
		this.sendResponse(contentType, text.getBytes(charset));
	}

	public void sendResponse(String contentType, String text)
			throws IOException {
		this.sendResponse(contentType, text, StandardCharsets.UTF_8);
	}

	public void sendResponse(String contentType, InputStream input)
			throws IOException {
		byte[] buffer;
		int size;

		this.setContentType(contentType);
		buffer = new byte[4096];
		while ((size = input.read(buffer)) > 0)
			this.getOutputStream().write(buffer, 0, size);
	}

	public void sendResponse(String contentType, URL url)
			throws IOException {
		URLConnection connection;
		long length;

		connection = url.openConnection();
		length = connection.getContentLength();
		if (length >= 0L) this.setContentLength(length);
		try (InputStream input =
				new BufferedInputStream(
						connection.getInputStream())) {
			this.sendResponse(contentType, input);
		}
	}

	public void sendResponse(String contentType, File file)
			throws IOException {
		long length;

		length = file.length();
		if (length > 0L) this.setContentLength(length);
		try(InputStream input =
				new BufferedInputStream(
						new FileInputStream(file))) {
			this.sendResponse(contentType, input);
		}
	}

}

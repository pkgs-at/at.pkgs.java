/*
 * Copyright (c) 2009-2015, Architector Inc., Japan
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

package at.pkgs.web.trio;

import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.io.Serializable;
import java.io.IOException;
import javax.servlet.FilterConfig;
import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import at.pkgs.web.http.HttpRequest;
import at.pkgs.web.http.HttpResponse;

public abstract class AbstractHandler {

	public static abstract class Filter
	implements javax.servlet.Filter, ContextHolder {

		private ServletContext context;

		private Class<? extends AbstractHandler> handler;

		@SuppressWarnings("unchecked")
		@Override
		public void init(
				FilterConfig config)
						throws ServletException {
			Class<?> enclosure;

			this.context = config.getServletContext();
			enclosure = this.getClass().getEnclosingClass();
			this.handler = (Class<? extends AbstractHandler>)enclosure;
		}

		public ServletContext getContext() {
			return this.context;
		}

		protected boolean applicable(
				HttpRequest request,
				HttpResponse response)
						throws ServletException, IOException {
			return true;
		}

		public void doFilter(
				HttpRequest request,
				HttpResponse response,
				FilterChain chain)
						throws IOException, ServletException {
			AbstractHandler handler;

			if (this.applicable(request, response)) {
				try {
					handler = this.handler.newInstance();
				}
				catch (ReflectiveOperationException cause) {
					throw new ServletException(cause);
				}
				response.setParameter("$", handler);
				handler.initialize(this, request, response);
				if (handler.finished()) return;
				handler.handle();
				if (handler.finished()) return;
				handler.complete();
				if (handler.finished()) return;
			}
			chain.doFilter(request, response);
		}

		@Override
		public void doFilter(
				ServletRequest request,
				ServletResponse response,
				FilterChain chain)
						throws IOException, ServletException {
			HttpRequest wrapped;

			if (request instanceof HttpRequest)
				wrapped = (HttpRequest)request;
			else
				wrapped = new HttpRequest((HttpServletRequest)request);
			this.doFilter(
					wrapped,
					response instanceof HttpResponse ?
							(HttpResponse)response :
							new HttpResponse(
									wrapped,
									(HttpServletResponse)response),
					chain);
		}

		@Override
		public void destroy() {
			// do nothing
		}

	}

	public static enum MessageType {

		ERROR,

		WARNING,

		NOTICE,

		SUCCESS;

	}

	public static class Message implements Serializable {

		private static final long serialVersionUID = 1L;

		private final MessageType type;

		private final String text;

		public Message(
				MessageType type,
				String text) {
			this.type = type;
			this.text = text;
		}

		public MessageType getType() {
			return this.type;
		}

		public String getText() {
			return this.text;
		}

	}

	private static final String SESSION_USER =
			AbstractHandler.class.getName() + ".user";

	private static final String SESSION_MESSAGES =
			AbstractHandler.class.getName() + ".messages";

	private boolean finished;

	private ContextHolder holder;

	private ServletContext context;

	private HttpRequest request;

	private HttpResponse response;

	private Map<String, Object> user;

	private List<Message> messages;

	@SuppressWarnings("unchecked")
	public void initialize(
			ContextHolder holder,
			HttpRequest request,
			HttpResponse response)
					throws ServletException, IOException {
		HttpSession session;
		Object messages;

		this.finished = false;
		this.holder = holder;
		this.context = request.getServletContext();
		this.request = request;
		this.response = response;
		this.messages = new ArrayList<Message>();
		session = this.getRequest().getSession(false);
		if (session == null) return;
		try {
			this.user = (Map<String, Object>)session.getAttribute(
					AbstractHandler.SESSION_USER);
		}
		catch (ClassCastException ignored) {
			// ignore
		}
		messages = session.getAttribute(AbstractHandler.SESSION_MESSAGES);
		if (messages == null) return;
		if (!(messages instanceof Message[])) return;
		for (Message message : (Message[])messages)
			this.addMessage(message);
	}

	public boolean finished() {
		return this.finished;
	}

	public ContextHolder getHolder() {
		return this.holder;
	}

	public ServletContext getContext() {
		return this.context;
	}

	public HttpRequest getRequest() {
		return this.request;
	}

	public HttpResponse getResponse() {
		return this.response;
	}

	protected void startUserSession() {
		HttpSession session;
		Map<String, Object> attributes;

		session = this.getRequest().getSession(false);
		attributes = new HashMap<String, Object>();
		if (session != null) {
			Enumeration<String> cursor;

			cursor = session.getAttributeNames();
			while (cursor.hasMoreElements()) {
				String name;

				name = cursor.nextElement();
				attributes.put(name, session.getAttribute(name));
			}
			session.invalidate();
		}
		session = this.getRequest().getSession(true);
		for (String name : attributes.keySet())
			session.setAttribute(name, attributes.get(name));
		this.user = new HashMap<String, Object>();
		session.setAttribute(AbstractHandler.SESSION_USER, this.user);
	}

	protected void endUserSession() {
		HttpSession session;

		session = this.getRequest().getSession(false);
		if (session != null) session.invalidate();
		this.user = null;
	}

	public boolean hasUserSession() {
		return this.user != null;
	}

	public Map<String, Object> getUserAttributeMap() {
		return this.user;
	}

	public Object getUserAttribute(String name) {
		if (this.user == null) return null;
		return this.user.get(name);
	}

	public void setUserAttribute(String name, Object value) {
		if (this.user == null) throw new IllegalStateException();
		this.user.put(name, value);
	}

	@SuppressWarnings("unchecked")
	public Iterable<Map.Entry<String, Object>> getUserAttributeIterable() {
		if (this.user == null) return Collections.EMPTY_MAP.entrySet();
		else return this.user.entrySet();
	}

	public Enumeration<String> getUserAttributeNames() {
		if (this.user == null) return Collections.emptyEnumeration();
		else return Collections.enumeration(this.user.keySet());
	}

	public boolean hasMessage() {
		return this.messages.size() > 0;
	}

	private void saveMessages() {
		if (this.hasMessage()) {
			HttpSession session;

			session = this.getRequest().getSession(true);
			session.setAttribute(
					AbstractHandler.SESSION_MESSAGES,
					this.messages.toArray(
							new Message[this.messages.size()]));
		}
		else {
			HttpSession session;

			session = this.getRequest().getSession(false);
			if (session != null)
				session.removeAttribute(
						AbstractHandler.SESSION_MESSAGES);
		}
	}

	public String format(
			String format,
			Object... parameters) {
		return String.format(format, parameters);
	}

	public void addMessage(
			Message message) {
		this.messages.add(message);
		this.saveMessages();
	}

	public void addMessage(
			MessageType type,
			String format,
			Object... parameters) {
		this.addMessage(new Message(type, this.format(format, parameters)));
	}

	public void addErrorMessage(
			String format,
			Object... parameters) {
		this.addMessage(MessageType.ERROR, format, parameters);
	}

	public void addWarningMessage(
			String format,
			Object... parameters) {
		this.addMessage(MessageType.WARNING, format, parameters);
	}

	public void addNoticeMessage(
			String format,
			Object... parameters) {
		this.addMessage(MessageType.NOTICE, format, parameters);
	}

	public void addSuccessMessage(
			String format,
			Object... parameters) {
		this.addMessage(MessageType.SUCCESS, format, parameters);
	}

	public Message[] flashMessages() {
		Message[] messages;

		messages = this.messages.toArray(
				new Message[this.messages.size()]);
		this.messages.clear();
		this.saveMessages();
		return messages;
	}

	protected void finish() {
		this.finished = true;
	}

	protected void forward(String path)
			throws ServletException, IOException {
		this.finish();
		this.getContext()
				.getRequestDispatcher(path)
				.forward(this.request, this.response);
	}

	protected abstract void handle()
			throws ServletException, IOException;

	protected void complete()
			throws ServletException, IOException {
	}

}

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

package at.pkgs.web.page;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public abstract class PageHandler {

	public static abstract class Servlet extends HttpServlet {

		private static final long serialVersionUID = 1L;

		protected abstract Class<? extends PageHandler> getHandlerType(
				PageRequest request,
				PageResponse response)
						throws ServletException, IOException;

		protected void service(
				PageRequest request,
				PageResponse response)
						throws ServletException, IOException {
			Class<? extends PageHandler> type;
			PageHandler handler;

			type = this.getHandlerType(request, response);
			if (type == null) return;
			try {
				handler = type.newInstance();
			}
			catch (InstantiationException | IllegalAccessException throwable) {
				throw new ServletException(throwable);
			}
			handler.initialize(this, request, response);
			if (handler.finished()) return;
			handler.handle();
			if (handler.finished()) return;
			handler.complete();
		}

		@Override
		protected void service(
				HttpServletRequest request,
				HttpServletResponse response)
						throws ServletException, IOException {
			PageRequest pageRequest;
			PageResponse pageResponse;

			pageRequest = new PageRequest(request);
			pageResponse = new PageResponse(pageRequest, response);
			this.service(pageRequest, pageResponse);
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

		public Message(MessageType type, String text) {
			this.type = type;
			this.text = text;
		}

		public Message(MessageType type, String format, Object... parameters) {
			this(type, String.format(format, parameters));
		}

		public MessageType getType() {
			return this.type;
		}

		public String getText() {
			return this.text;
		}

	}

	private static final String SESSION_MESSAGE_FLASH_BAG =
			"_PAGE_HANDLER_MESSAGE_FLASH_BAG_";

	private boolean finished;

	private Servlet servlet;

	private PageRequest request;

	private PageResponse response;

	private List<Message> messages;

	private boolean hasErrorMessage;

	public boolean finished() {
		return this.finished;
	}

	public void finish() {
		this.finished = true;
	}

	public Servlet getServlet() {
		return this.servlet;
	}

	public PageRequest getRequest() {
		return this.request;
	}

	public PageResponse getResponse() {
		return this.response;
	}

	public void addMessage(
			Message message) {
		if (message.getType() == MessageType.ERROR)
			this.hasErrorMessage = true;
		this.messages.add(message);
	}

	public void addMessage(
			MessageType type,
			String text) {
		this.addMessage(new Message(type, text));
	}

	public void addMessage(
			MessageType type,
			String format,
			Object... parameters) {
		this.addMessage(new Message(type, format, parameters));
	}

	public boolean hasMessage() {
		return this.messages.size() > 0;
	}

	public boolean hasErrorMessage() {
		return this.hasErrorMessage;
	}

	public Message[] flashMessages() {
		Message[] messages;

		messages = this.messages.toArray(new Message[this.messages.size()]);
		this.messages.clear();
		this.hasErrorMessage = false;
		return messages;
	}

	protected void initialize()
			throws ServletException, IOException {
		HttpSession session;
		Object object;

		this.messages = new ArrayList<Message>();
		this.hasErrorMessage = false;
		session = this.getRequest().getSession(false);
		if (session == null) return;
		object = session.getAttribute(PageHandler.SESSION_MESSAGE_FLASH_BAG);
		if (object == null) return;
		if (!(object instanceof Message[])) return;
		for (Message message : (Message[])object)
			this.addMessage(message);
	}

	private void initialize(
			Servlet servlet,
			PageRequest request,
			PageResponse response)
					throws ServletException, IOException {
		this.finished = false;
		this.servlet = servlet;
		this.request = request;
		this.response = response;
		this.initialize();
	}

	protected abstract void handle()
			throws ServletException, IOException;

	protected void complete()
			throws ServletException, IOException {
		if (this.messages.size() > 0) {
			HttpSession session;

			session = this.getRequest().getSession(true);
			session.setAttribute(
					PageHandler.SESSION_MESSAGE_FLASH_BAG,
					this.flashMessages());
		}
		else {
			HttpSession session;

			session = this.getRequest().getSession(false);
			if (session != null)
				session.removeAttribute(PageHandler.SESSION_MESSAGE_FLASH_BAG);
		}
	}

}

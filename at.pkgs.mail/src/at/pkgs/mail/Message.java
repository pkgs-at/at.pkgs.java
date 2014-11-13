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

package at.pkgs.mail;

import java.util.List;
import java.util.ArrayList;
import java.io.Serializable;
import javax.mail.Session;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public final class Message implements Serializable {

	public static enum Encoding {

		JAPANESE("ISO-2022-JP"),

		UNKNOWN(null);

		private final String name;

		private Encoding(String name) {
			this.name = name;
		}

		public String getEncodingName() {
			return this.name;
		}

	}

	private static final long serialVersionUID = 1L;

	private final String encoding;

	private Address from;

	private final List<Address> to;

	private final List<Address> cc;

	private final List<Address> bcc;

	private String subject;

	private String body;

	public Message(String encoding) {
		this.encoding = encoding;
		this.to = new ArrayList<Address>();
		this.cc = new ArrayList<Address>();
		this.bcc = new ArrayList<Address>();
	}

	public Message(Encoding encoding) {
		this(encoding.getEncodingName());
	}

	public Message() {
		this((String)null);
	}

	public String getEncoding() {
		return this.encoding;
	}

	public Address getFrom() {
		return this.from;
	}

	public List<Address> getTo() {
		return this.to;
	}

	public List<Address> getCc() {
		return this.cc;
	}

	public List<Address> getBcc() {
		return this.bcc;
	}

	public String getSubject() {
		return this.subject;
	}

	public String getBody() {
		return this.body;
	}

	public Message from(Address from) {
		this.from = from;
		return this;
	}

	public Message from(String address, String name) {
		return this.from(new Address(address, name));
	}

	public Message from(String address) {
		return this.from(new Address(address));
	}

	public Message to(Address to) {
		this.to.add(to);
		return this;
	}

	public Message to(String address, String name) {
		return this.to(new Address(address, name));
	}

	public Message to(String address) {
		return this.to(new Address(address));
	}

	public Message to(Address[] addresses) {
		for (Address address : addresses)
			this.to(address);
		return this;
	}

	public Message to(String[] addresses) {
		for (String address : addresses)
			this.to(address);
		return this;
	}

	public Message cc(Address cc) {
		this.cc.add(cc);
		return this;
	}

	public Message cc(String address, String name) {
		return this.cc(new Address(address, name));
	}

	public Message cc(String address) {
		return this.cc(new Address(address));
	}

	public Message cc(Address[] addresses) {
		for (Address address : addresses)
			this.cc(address);
		return this;
	}

	public Message cc(String[] addresses) {
		for (String address : addresses)
			this.cc(address);
		return this;
	}

	public Message bcc(Address bcc) {
		this.bcc.add(bcc);
		return this;
	}

	public Message bcc(String address, String name) {
		return this.bcc(new Address(address, name));
	}

	public Message bcc(String address) {
		return this.bcc(new Address(address));
	}

	public Message bcc(Address[] addresses) {
		for (Address address : addresses)
			this.bcc(address);
		return this;
	}

	public Message bcc(String[] addresses) {
		for (String address : addresses)
			this.bcc(address);
		return this;
	}

	public Message subject(String subject) {
		this.subject = subject;
		return this;
	}

	public Message body(String body) {
		this.body = body;
		return this;
	}

	public MimeMessage encode(Session session) throws MessagingException {
		MimeMessage message;

		message = new MimeMessage(session);
		if (this.from != null) {
			InternetAddress address;

			address = this.from.encode(this.encoding);
			message.setFrom(address);
			message.setSender(address);
		}
		for (Address address : this.to)
			message.addRecipient(
					MimeMessage.RecipientType.TO,
					address.encode(this.encoding));
		for (Address address : this.cc)
			message.addRecipient(
					MimeMessage.RecipientType.CC,
					address.encode(this.encoding));
		for (Address address : this.bcc)
			message.addRecipient(
					MimeMessage.RecipientType.BCC,
					address.encode(this.encoding));
		message.setSubject(this.subject, this.encoding);
		message.setText(this.body, this.encoding);
		return message;
	}

}

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
import java.util.Properties;
import java.io.Serializable;
import java.io.File;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import javax.mail.Session;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

public final class Message implements Serializable {

	public static enum Encoding {

		JAPANESE(EncodingHelper.ISO_2022_JP),

		UNKNOWN(null);

		private final String name;

		private Encoding(String name) {
			this.name = name;
		}

		public String getEncodingName() {
			return this.name;
		}

	}

	public static abstract class Part implements Serializable {

		private static final long serialVersionUID = 1L;

		protected abstract MimeBodyPart encode(String encoding)
				throws MessagingException;

	}

	public static class Parts extends Part {

		private static final long serialVersionUID = 1L;

		private final String subtype;

		private final List<Part> parts;

		public Parts(String subtype) {
			this.subtype = subtype;
			this.parts = new ArrayList<Part>();
		}

		public Parts part(Part part) {
			this.parts.add(part);
			return this;
		}

		protected MimeMultipart multipart(String encoding)
				throws MessagingException {
			MimeMultipart multipart;

			multipart = new MimeMultipart(this.subtype);
			for (Part part : this.parts)
				multipart.addBodyPart(part.encode(encoding));
			return multipart;
		}

		@Override
		protected MimeBodyPart encode(String encoding)
				throws MessagingException {
			MimeBodyPart part;

			part = new MimeBodyPart();
			part.setContent(this.multipart(encoding));
			return part;
		}

	}

	public static class MixedParts extends Parts {

		private static final long serialVersionUID = 1L;

		public MixedParts() {
			super("mixed");
		}

	}

	public static class AlternativeParts extends Parts {

		private static final long serialVersionUID = 1L;

		public AlternativeParts() {
			super("alternative");
		}

	}

	public static class DigestParts extends Parts {

		private static final long serialVersionUID = 1L;

		public DigestParts() {
			super("digest");
		}

	}

	public static class ParallelParts extends Parts {

		private static final long serialVersionUID = 1L;

		public ParallelParts() {
			super("parallel");
		}

	}

	public static class RelatedParts extends Parts {

		private static final long serialVersionUID = 1L;

		public RelatedParts() {
			super("related");
		}

	}

	public static class MimePart extends Part {

		private static final long serialVersionUID = 1L;

		private final MimeBodyPart part;

		public MimePart(MimeBodyPart part) {
			this.part = part;
		}

		@Override
		protected MimeBodyPart encode(String encoding)
				throws MessagingException {
			return this.part;
		}

	}

	public static class TextPart extends Part {

		private static final long serialVersionUID = 1L;

		private final String text;

		private final String subtype;

		public TextPart(String text, String subtype) {
			this.text = text;
			this.subtype = subtype;
		}

		public TextPart(String text) {
			this(text, "plain");
		}

		@Override
		protected MimeBodyPart encode(String encoding)
				throws MessagingException {
			MimeBodyPart part;

			part = new MimeBodyPart();
			part.setText(
					EncodingHelper.normalize(encoding, this.text),
					encoding,
					this.subtype);
			return part;
		}

	}

	public static class DataHandlerPart extends Part {

		private static final long serialVersionUID = 1L;

		private final DataHandler handler;

		public DataHandlerPart(DataHandler handler) {
			this.handler = handler;
		}

		protected void encode(String encoding, MimeBodyPart part)
				throws MessagingException {
			// do nothing
		}

		@Override
		protected MimeBodyPart encode(String encoding)
				throws MessagingException {
			MimeBodyPart part;

			part = new MimeBodyPart();
			part.setDataHandler(this.handler);
			this.encode(encoding, part);
			return part;
		}

	}

	public static class InlinePart extends DataHandlerPart {

		private static final long serialVersionUID = 1L;

		private final String contentId;

		public InlinePart(String contentId, DataHandler handler) {
			super(handler);
			this.contentId = contentId;
		}

		@Override
		protected void encode(String encoding, MimeBodyPart part)
				throws MessagingException {
			part.setDescription(MimeBodyPart.INLINE);
			part.setContentID(this.contentId);
		}

	}

	public static class AttachmentPart extends DataHandlerPart {

		private static final long serialVersionUID = 1L;

		private final String name;

		public AttachmentPart(String name, DataHandler handler) {
			super(handler);
			this.name = name;
		}

		public AttachmentPart(DataHandler handler) {
			this(handler.getName(), handler);
		}

		@Override
		protected void encode(String encoding, MimeBodyPart part)
				throws MessagingException {
			String name;

			try {
				name = MimeUtility.encodeText(this.name, encoding, "B");
			}
			catch (UnsupportedEncodingException cause) {
				throw new MessagingException(
						String.format(
								"failed on encode filename: %s",
								this.name),
						cause);
			}
			part.setDescription(MimeBodyPart.ATTACHMENT);
			part.setFileName(name);
		}

	}

	public static DataHandler newDataHandler(File file) {
		return new DataHandler(new FileDataSource(file));
	}

	public static DataHandler newDataHandler(URL url) {
		return new DataHandler(new URLDataSource(url));
	}

	public static DataHandler newDataHandler(
			final String contentType,
			final String name,
			final byte[] content) {
		return new DataHandler(new DataSource() {

			@Override
			public String getContentType() {
				return contentType;
			}

			@Override
			public String getName() {
				return name;
			}

			@Override
			public InputStream getInputStream() {
				return new ByteArrayInputStream(content);
			}

			@Override
			public OutputStream getOutputStream() {
				throw new UnsupportedOperationException();
			}

		});
	}

	private static final long serialVersionUID = 1L;

	private final String encoding;

	private Address from;

	private Address replyTo;

	private final List<Address> to;

	private final List<Address> cc;

	private final List<Address> bcc;

	private String subject;

	private Parts parts;

	private String body;

	private String subtype;

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

	public Address getReplyTo() {
		return this.replyTo;
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

	public Message replyTo(Address replyTo) {
		this.replyTo = replyTo;
		return this;
	}

	public Message replyTo(String address, String name) {
		return this.replyTo(new Address(address, name));
	}

	public Message replyTo(String address) {
		return this.replyTo(new Address(address));
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

	public Message parts(Parts parts) {
		this.parts = parts;
		return this;
	}

	public Message body(String body, String subtype) {
		this.body = body;
		this.subtype = subtype;
		return this;
	}

	public Message body(String body) {
		return this.body(body, "plain");
	}

	public MimeMessage encode(Session session)
			throws MessagingException {
		MimeMessage message;

		message = new MimeMessage(session);
		if (this.from != null) {
			InternetAddress address;

			address = this.from.encode(this.encoding);
			message.setFrom(address);
			message.setSender(address);
		}
		if (this.replyTo != null) {
			InternetAddress address;

			address = this.replyTo.encode(this.encoding);
			message.setReplyTo(new InternetAddress[] { address });
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
		message.setSubject(
				EncodingHelper.normalize(this.encoding, this.subject),
				this.encoding);
		if (this.parts != null)
			message.setContent(
					this.parts.multipart(this.encoding));
		else
			message.setText(
					EncodingHelper.normalize(this.encoding, this.body),
					this.encoding,
					this.subtype);
		return message;
	}

	public void writeTo(OutputStream output)
			throws IOException, MessagingException {
		this.encode(Session.getInstance(new Properties())).writeTo(output);
	}

	public static void main(String[] arguments)
			throws IOException, MessagingException {
		System.out.println("========");
		System.out.println();
		new Message(Encoding.JAPANESE)
				.from("from@example.com", "送信者")
				.subject("テスト")
				.body("このメールはテストです。", "html")
				.writeTo(System.out);
		System.out.println();
		System.out.println("========");
		System.out.println();
		new Message(Encoding.JAPANESE)
				.from("from@example.com", "送信者")
				.subject("テスト")
				.parts(new MixedParts()
						.part(new TextPart(
								"パート1"))
						.part(new TextPart(
								"パート2",
								"xml"))
						.part(new InlinePart(
								"content3",
								new DataHandler(
										"パート3",
										"text/css")))
						.part(new AttachmentPart(
								"日本語",
								new DataHandler(
										"パート4",
										"text/plain; charset=iso-2022-jp")))
						.part(new AttachmentPart(
								"日本語",
								new DataHandler(
										"パート5",
										"application/zip"))))
				.writeTo(System.out);
		System.out.println();
		System.out.println("========");
	}

}

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

import java.util.Properties;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.SSLContext;
import javax.mail.Session;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


public final class Transport {

	public static class PseudoTrustManager implements X509TrustManager {

		private static final X509Certificate[] EMPTY_CERTIFICATES =
				new X509Certificate[] {};

		@Override
		public void checkClientTrusted(
				X509Certificate[] certificates,
				String type) {
			// do nothing
		}

		@Override
		public void checkServerTrusted(
				X509Certificate[] certificates,
				String type) {
			// do nothing
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return PseudoTrustManager.EMPTY_CERTIFICATES;
		}

		private static final SSLContext SSL_CONTEXT;

		static {
			SSLContext context;

			try {
				context = SSLContext.getInstance("TLS");
				context.init(
						null,
						new X509TrustManager[] { new PseudoTrustManager() },
						null);
			}
			catch (Exception cause) {
				throw new RuntimeException(cause);
			}
			SSL_CONTEXT = context;
		}

	}

	public class Connection implements AutoCloseable {

		private Session session;

		private javax.mail.Transport transport;

		protected Connection() throws MessagingException {
			Properties properties;
			String protocol;

			properties = new Properties();
			protocol = Transport.this.secure ? "smtps" : "smtp";
			properties.setProperty(
					"mail.transport.protocol",
					protocol);
			properties.setProperty(
					"mail." + protocol + ".host",
					Transport.this.hostname);
			properties.setProperty(
					"mail." + protocol + ".port",
					Integer.toString(Transport.this.port, 10));
			properties.setProperty(
					"mail." + protocol + ".connectiontimeout",
					Long.toString(Transport.this.timeout, 10));
			properties.setProperty(
					"mail." + protocol + ".starttls.enable",
					Boolean.toString(true));
			properties.setProperty(
					"mail." + protocol + ".ssl.enable",
					Boolean.toString(Transport.this.secure));
			properties.setProperty(
					"mail." + protocol + ".auth",
					Boolean.toString(Transport.this.username != null));
			if (!Transport.this.certificate)
				properties.put(
						"mail." + protocol + ".ssl.socketFactory",
						PseudoTrustManager.SSL_CONTEXT.getSocketFactory());
			this.session = Session.getInstance(properties);
			this.transport = this.session.getTransport();
			this.transport.connect(
					Transport.this.username,
					Transport.this.password);
		}

		public void send(Message message) throws MessagingException {
			MimeMessage encoded;

			encoded = message.encode(this.session);
			this.transport.sendMessage(encoded, encoded.getAllRecipients());
		}

		@Override
		public void close() throws MessagingException {
			if (this.transport != null) {
				this.transport.close();
				this.transport = null;
			}
		}

	}

	private String hostname;

	private int port;

	private boolean secure;

	private boolean certificate;

	private String username;

	private String password;

	private long timeout;

	public Transport(String hostname, int port, boolean secure) {
		this.hostname = hostname;
		this.port = port;
		this.secure = secure;
		this.certificate = true;
		this.username = null;
		this.password = null;
		this.timeout = 30 * 1000L;
	}

	public Transport certificate(boolean certificate) {
		this.certificate = certificate;
		return this;
	}

	public Transport authenticate(String username, String password) {
		this.username = username;
		this.password = password;
		return this;
	}

	public Transport timeout(long milliseconds) {
		this.timeout = milliseconds;
		return this;
	}

	public Connection connect() throws MessagingException {
		return new Connection();
	}

	public void send(Message message) throws MessagingException {
		try (Connection connection = this.connect()) {
			connection.send(message);
		}
	}

	public static void main(String[] arguments) throws MessagingException {
		new Transport("smtp.gmail.com", 587, false)
				.authenticate(
						"username",
						"password")
				.send(new Message()
						.to("destination", "name")
						.from("sender")
						.subject("this is a test")
						.body("this is a test body"));
	}

}

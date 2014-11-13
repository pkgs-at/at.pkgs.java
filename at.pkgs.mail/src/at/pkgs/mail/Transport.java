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
import javax.mail.Session;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public final class Transport {

	public class Connection implements AutoCloseable {

		private Session session;

		private javax.mail.Transport transport;

		protected Connection() throws MessagingException {
			Properties properties;

			properties = new Properties();
			properties.setProperty(
					"mail.transport.protocol",
					Transport.this.secure ? "smtps" : "smtp");
			properties.setProperty(
					"mail.smtp.host",
					Transport.this.hostname);
			properties.setProperty(
					"mail.smtp.port",
					Integer.toString(Transport.this.port, 10));
			properties.setProperty(
					"mail.smtp.connectiontimeout",
					Long.toString(Transport.this.timeout, 10));
			properties.setProperty(
					"mail.smtp.starttls.enable",
					"true");
			properties.setProperty(
					"mail.smtp.ssl.enable",
					Boolean.toString(Transport.this.secure));
			properties.setProperty(
					"mail.smtp.auth",
					Transport.this.username != null ? "true" : "false");
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

	private String username;

	private String password;

	private long timeout;

	public Transport(String hostname, int port, boolean secure) {
		this.hostname = hostname;
		this.port = port;
		this.secure = secure;
		this.username = null;
		this.password = null;
		this.timeout = 30 * 1000L;
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

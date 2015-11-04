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

import java.util.regex.Pattern;
import java.io.Serializable;
import java.io.IOException;
import java.net.IDN;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

public final class Address implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Pattern ADDRESS_DELIMITOR =
		Pattern.compile(Pattern.quote("@"));

	private String address;

	private String name;

	public Address(String address, String name) {
		String[] parts;
		StringBuilder builder;

		parts = Address.ADDRESS_DELIMITOR.split(address, 2);
		if (parts.length != 2)
			throw new IllegalArgumentException("invalid address");
		builder = new StringBuilder();
		builder.append(IDN.toASCII(parts[0], IDN.ALLOW_UNASSIGNED));
		builder.append('@');
		builder.append(IDN.toASCII(parts[1], IDN.ALLOW_UNASSIGNED));
		this.address = builder.toString();
		this.name = name;
	}

	public Address(String address) {
		this(address, null);
	}

	public Address() {
		this(null, null);
	}

	public String getAddress() {
		return this.address;
	}

	public String getName() {
		return this.name;
	}

	public Address address(String address) {
		this.address = address;
		return this;
	}

	public Address name(String name) {
		this.name = name;
		return this;
	}

	public InternetAddress encode(String encoding) throws MessagingException {
		try {
			return new InternetAddress(
					this.address,
					EncodingHelper.normalize(encoding, this.name),
					encoding);
		}
		catch (IOException cause) {
			throw new MessagingException("failed on encode address", cause);
		}
	}

	public static void main(String[] arguments) {
		System.out.println(new Address("test@example.com").getAddress());
		System.out.println(new Address("テスト@日本語domain.jp").getAddress());
	}

}

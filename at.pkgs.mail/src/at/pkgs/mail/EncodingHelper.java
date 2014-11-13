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

public class EncodingHelper {

	public static final String ISO_2022_JP = "ISO-2022-JP";

	public static final String WINDOWS_31J = "Windows-31J";

	public static String iso_2022_jp(String value) {
		StringBuilder builder;

		if (value == null) return null;
		builder = new StringBuilder(value);
		for (int index = 0; index < value.length(); index ++) {
			switch (value.charAt(index)) {
			case '\u2015' :
				builder.setCharAt(index, '\u2014');
				break;
			case '\u2225' :
				builder.setCharAt(index, '\u2016');
				break;
			case '\uFF0D' :
				builder.setCharAt(index, '\u2212');
				break;
			case '\uFF5E' :
				builder.setCharAt(index, '\u301C');
				break;
			case '\uFFE0' :
				builder.setCharAt(index, '\u00A2');
				break;
			case '\uFFE1' :
				builder.setCharAt(index, '\u00A3');
				break;
			case '\uFFE2' :
				builder.setCharAt(index, '\u00AC');
				break;
			}
		}
		return builder.toString();
	}

	public static String windows_31j(String value) {
		StringBuilder builder;

		if (value == null) return null;
		builder = new StringBuilder(value);
		for (int index = 0; index < value.length(); index ++) {
			switch (value.charAt(index)) {
			case '\u00A2' :
				builder.setCharAt(index, '\uFFE0');
				break;
			case '\u00A3' :
				builder.setCharAt(index, '\uFFE1');
				break;
			case '\u00AC' :
				builder.setCharAt(index, '\uFFE2');
				break;
			case '\u2014' :
				builder.setCharAt(index, '\u2015');
				break;
			case '\u2016' :
				builder.setCharAt(index, '\u2225');
				break;
			case '\u2212' :
				builder.setCharAt(index, '\uFF0D');
				break;
			case '\u301C' :
				builder.setCharAt(index, '\uFF5E');
				break;
			}
		}
		return builder.toString();
	}

	public static String normalize(String encoding, String value) {
		if (encoding == null) return value;
		if (value == null) return null;
		switch (encoding) {
		case EncodingHelper.ISO_2022_JP :
			return EncodingHelper.iso_2022_jp(value);
		case EncodingHelper.WINDOWS_31J :
			return EncodingHelper.windows_31j(value);
		default :
			return value;
		}
	}

}

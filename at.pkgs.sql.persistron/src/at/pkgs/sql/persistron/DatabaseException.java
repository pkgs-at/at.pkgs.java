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

package at.pkgs.sql.persistron;

public class DatabaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DatabaseException(
			String message) {
		super(message);
	}

	public DatabaseException(
			String format,
			Object... arguments) {
		super(String.format(format, arguments));
	}

	public DatabaseException(
			Throwable cause) {
		super(cause);
	}

	public DatabaseException(
			Throwable cause,
			String message) {
		super(message, cause);
	}

	public DatabaseException(
			Throwable cause,
			String format,
			Object... arguments) {
		super(String.format(format, arguments), cause);
	}

}

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

package at.pkgs.sql.sugar;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.SQLException;

public class Clob
extends DatabaseObject<java.sql.Clob>
implements java.sql.Clob {

	public Clob(Connection connection, java.sql.Clob wrapped) {
		super(connection, wrapped);
	}

	@Override
	public String getSubString(long position, int length) {
		try {
			return this.unwrap().getSubString(position, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int setString(
			long position,
			String value,
			int offset,
			int length) {
		try {
			return this.unwrap().setString(position, value, offset, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int setString(long position, String value) {
		try {
			return this.unwrap().setString(position, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public InputStream getAsciiStream() {
		try {
			return this.unwrap().getAsciiStream();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public OutputStream setAsciiStream(long position) {
		try {
			return this.unwrap().setAsciiStream(position);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Reader getCharacterStream(long position, long length) {
		try {
			return this.unwrap().getCharacterStream(position, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Reader getCharacterStream() {
		try {
			return this.unwrap().getCharacterStream();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Writer setCharacterStream(long position) {
		try {
			return this.unwrap().setCharacterStream(position);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public long position(String value, long start) {
		try {
			return this.unwrap().position(value, start);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public long position(java.sql.Clob value, long start) {
		try {
			return this.unwrap().position(this.unwrap(value), start);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public long length() {
		try {
			return this.unwrap().length();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void truncate(long length) {
		try {
			this.unwrap().truncate(length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void free() {
		try {
			this.unwrap().free();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

}

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
import java.sql.SQLException;

public class Blob
extends DatabaseObject<java.sql.Blob>
implements java.sql.Blob {

	public Blob(Connection connection, java.sql.Blob wrapped) {
		super(connection, wrapped);
	}

	@Override
	public byte[] getBytes(long position, int length) {
		try {
			return this.unwrap().getBytes(position, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int setBytes(
			long position,
			byte[] value,
			int offset,
			int length) {
		try {
			return this.unwrap().setBytes(position, value, offset, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int setBytes(long position, byte[] value) {
		try {
			return this.unwrap().setBytes(position, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public InputStream getBinaryStream(long position, long length) {
		try {
			return this.unwrap().getBinaryStream(position, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public InputStream getBinaryStream() {
		try {
			return this.unwrap().getBinaryStream();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public OutputStream setBinaryStream(long position) {
		try {
			return this.unwrap().setBinaryStream(position);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public long position(byte[] value, long start) {
		try {
			return this.unwrap().position(value, start);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public long position(java.sql.Blob value, long start) {
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

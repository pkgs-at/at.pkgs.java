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
import javax.xml.transform.Result;
import javax.xml.transform.Source;

public class SQLXML
extends DatabaseObject<java.sql.SQLXML>
implements java.sql.SQLXML {

	public SQLXML(Connection connection, java.sql.SQLXML wrapped) {
		super(connection, wrapped);
	}

	@Override
	public String getString() {
		try {
			return this.unwrap().getString();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setString(String value) {
		try {
			this.unwrap().setString(value);
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
	public OutputStream setBinaryStream() {
		try {
			return this.unwrap().setBinaryStream();
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
	public Writer setCharacterStream() {
		try {
			return this.unwrap().setCharacterStream();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public <Type extends Source> Type getSource(Class<Type> type) {
		try {
			return this.unwrap().getSource(type);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public <Type extends Result> Type setResult(Class<Type> type) {
		try {
			return this.unwrap().setResult(type);
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

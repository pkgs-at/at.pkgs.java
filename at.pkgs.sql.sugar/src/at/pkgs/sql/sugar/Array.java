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

import java.util.Map;
import java.sql.SQLException;

public class Array
extends DatabaseObject<java.sql.Array>
implements java.sql.Array {

	public Array(Connection connection, java.sql.Array wrapped) {
		super(connection, wrapped);
	}

	@Override
	public int getBaseType() throws SQLException {
		try {
			return this.unwrap().getBaseType();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public String getBaseTypeName() {
		try {
			return this.unwrap().getBaseTypeName();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Object getArray(
			long index,
			int count,
			Map<String, Class<?>> map) {
		try {
			return this.unwrap().getArray(index, count, map);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Object getArray(long index, int count) {
		try {
			return this.unwrap().getArray(index, count);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Object getArray(Map<String, Class<?>> map) {
		try {
			return this.unwrap().getArray(map);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Object getArray() {
		try {
			return this.unwrap().getArray();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public ResultSet getResultSet(
			long index,
			int count,
			Map<String, Class<?>> map) {
		try {
			return this.wrap(
					this.unwrap().getResultSet(index, count, map));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public ResultSet getResultSet(long index, int count) {
		try {
			return this.wrap(
					this.unwrap().getResultSet(index, count));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public ResultSet getResultSet(Map<String, Class<?>> map) {
		try {
			return this.wrap(
					this.unwrap().getResultSet(map));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public ResultSet getResultSet() {
		try {
			return this.wrap(
					this.unwrap().getResultSet());
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

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

public class Struct implements java.sql.Struct {

	private final Connection connection;

	private final java.sql.Struct wrapped;

	public Struct(Connection connection, java.sql.Struct wrapped) {
		this.connection = connection;
		this.wrapped = wrapped;
	}

	public Connection getConnection() {
		return this.connection;
	}

	public java.sql.Struct unwrap() {
		return this.wrapped;
	}

	@Override
	public String getSQLTypeName() {
		try {
			return this.wrapped.getSQLTypeName();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Object[] getAttributes(Map<String, Class<?>> map) {
		try {
			return this.wrapped.getAttributes(map);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Object[] getAttributes() {
		try {
			return this.wrapped.getAttributes();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

}

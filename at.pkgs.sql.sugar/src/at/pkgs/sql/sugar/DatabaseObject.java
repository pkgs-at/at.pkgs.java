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

import java.sql.Wrapper;
import java.sql.SQLException;

public abstract class DatabaseObject<WrappedType>
implements DatabaseObjectWrapper<WrappedType> {

	private final Connection connection;

	private final WrappedType wrapped;

	public DatabaseObject(Connection connection, WrappedType wrapped) {
		this.connection = connection;
		this.wrapped = wrapped;
	}

	public Connection getConnection() {
		return this.connection;
	}

	@Override
	public boolean isWrapperFor(Class<?> type) {
		if (type.isAssignableFrom(this.getClass()))
			return true;
		if (type.isAssignableFrom(this.wrapped.getClass()))
			return true;
		if (this.wrapped instanceof Wrapper) {
			try {
				return ((Wrapper)this.wrapped).isWrapperFor(type);
			}
			catch (SQLException cause) {
				throw new DatabaseException(cause);
			}
		}
		return false;
	}

	@Override
	public <Type> Type unwrap(Class<Type> type) {
		if (type.isAssignableFrom(this.getClass()))
			return type.cast(this);
		if (type.isAssignableFrom(this.wrapped.getClass()))
			return type.cast(this.wrapped);
		if (this.wrapped instanceof Wrapper) {
			try {
				return ((Wrapper)this.wrapped).unwrap(type);
			}
			catch (SQLException cause) {
				throw new DatabaseException(cause);
			}
		}
		throw new DatabaseException(new SQLException("not wrapped"));
	}

	@Override
	public WrappedType unwrap() {
		return this.wrapped;
	}

	@Override
	public <Type> Type unwrap(Type type) {
		return this.connection.unwrap(type);
	}

	@Override
	public Savepoint wrap(java.sql.Savepoint wrapped) {
		return this.connection.wrap(wrapped);
	}

	@Override
	public RowId wrap(java.sql.RowId wrapped) {
		return this.connection.wrap(wrapped);
	}

	@Override
	public Ref wrap(java.sql.Ref wrapped) {
		return this.connection.wrap(wrapped);
	}

	@Override
	public Array wrap(java.sql.Array wrapped) {
		return this.connection.wrap(wrapped);
	}

	@Override
	public Struct wrap(java.sql.Struct wrapped) {
		return this.connection.wrap(wrapped);
	}

	@Override
	public Blob wrap(java.sql.Blob wrapped) {
		return this.connection.wrap(wrapped);
	}

	@Override
	public Clob wrap(java.sql.Clob wrapped) {
		return this.connection.wrap(wrapped);
	}

	@Override
	public NClob wrap(java.sql.NClob wrapped) {
		return this.connection.wrap(wrapped);
	}

	@Override
	public SQLXML wrap(java.sql.SQLXML wrapped) {
		return this.connection.wrap(wrapped);
	}

	@Override
	public Statement wrap(java.sql.Statement wrapped) {
		return this.connection.wrap(wrapped);
	}

	@Override
	public PreparedStatement wrap(java.sql.PreparedStatement wrapped) {
		return this.connection.wrap(wrapped);
	}

	@Override
	public CallableStatement wrap(java.sql.CallableStatement wrapped) {
		return this.connection.wrap(wrapped);
	}

	@Override
	public ResultSet wrap(java.sql.ResultSet wrapped) {
		return this.connection.wrap(wrapped);
	}

	@Override
	public ParameterMetaData wrap(java.sql.ParameterMetaData wrapped) {
		return this.connection.wrap(wrapped);
	}

	@Override
	public ResultSetMetaData wrap(java.sql.ResultSetMetaData wrapped) {
		return this.connection.wrap(wrapped);
	}

}

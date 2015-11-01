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

import java.util.Properties;
import java.util.Map;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLClientInfoException;

import java.sql.DatabaseMetaData;

public class Connection
implements java.sql.Connection, DatabaseObjectWrapper<java.sql.Connection> {

	private final java.sql.Connection wrapped;

	private boolean transactional;

	public Connection(java.sql.Connection wrapped) {
		this.wrapped = wrapped;
		this.transactional = false;
	}

	void transactional() {
		this.transactional = true;
	}

	boolean isTransactional() {
		return this.transactional;
	}

	@Override
	public boolean isWrapperFor(Class<?> type) {
		if (type.isAssignableFrom(this.getClass()))
			return true;
		if (type.isAssignableFrom(this.wrapped.getClass()))
			return true;
		try {
			return this.wrapped.isWrapperFor(type);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public <Type> Type unwrap(Class<Type> type) {
		if (type.isAssignableFrom(this.getClass()))
			return type.cast(this);
		if (type.isAssignableFrom(this.wrapped.getClass()))
			return type.cast(this.wrapped);
		try {
			return this.wrapped.unwrap(type);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public java.sql.Connection unwrap() {
		return this.wrapped;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <Type> Type unwrap(Type value) {
		if (value instanceof DatabaseObjectWrapper)
			return ((DatabaseObjectWrapper<Type>)value).unwrap();
		return value;
	}

	@Override
	public Savepoint wrap(java.sql.Savepoint wrapped) {
		if (wrapped == null) return null;
		if (wrapped instanceof Savepoint) return (Savepoint)wrapped;
		return new Savepoint(this, wrapped);
	}

	@Override
	public RowId wrap(java.sql.RowId wrapped) {
		if (wrapped == null) return null;
		if (wrapped instanceof RowId) return (RowId)wrapped;
		return new RowId(this, wrapped);
	}

	@Override
	public Ref wrap(java.sql.Ref wrapped) {
		if (wrapped == null) return null;
		if (wrapped instanceof Ref) return (Ref)wrapped;
		return new Ref(this, wrapped);
	}

	@Override
	public Array wrap(java.sql.Array wrapped) {
		if (wrapped == null) return null;
		if (wrapped instanceof Array) return (Array)wrapped;
		return new Array(this, wrapped);
	}

	@Override
	public Struct wrap(java.sql.Struct wrapped) {
		if (wrapped == null) return null;
		if (wrapped instanceof Struct) return (Struct)wrapped;
		return new Struct(this, wrapped);
	}

	@Override
	public Blob wrap(java.sql.Blob wrapped) {
		if (wrapped == null) return null;
		if (wrapped instanceof Blob) return (Blob)wrapped;
		return new Blob(this, wrapped);
	}

	@Override
	public Clob wrap(java.sql.Clob wrapped) {
		if (wrapped == null) return null;
		if (wrapped instanceof Clob) return (Clob)wrapped;
		return new Clob(this, wrapped);
	}

	@Override
	public NClob wrap(java.sql.NClob wrapped) {
		if (wrapped == null) return null;
		if (wrapped instanceof NClob) return (NClob)wrapped;
		return new NClob(this, wrapped);
	}

	@Override
	public SQLXML wrap(java.sql.SQLXML wrapped) {
		if (wrapped == null) return null;
		if (wrapped instanceof SQLXML) return (SQLXML)wrapped;
		return new SQLXML(this, wrapped);
	}

	@Override
	public Statement wrap(java.sql.Statement wrapped) {
		if (wrapped == null) return null;
		if (wrapped instanceof Statement)
			return (Statement)wrapped;
		return new Statement(this, wrapped);
	}

	@Override
	public PreparedStatement wrap(java.sql.PreparedStatement wrapped) {
		if (wrapped == null) return null;
		if (wrapped instanceof PreparedStatement)
			return (PreparedStatement)wrapped;
		return new PreparedStatement(this, wrapped);
	}

	@Override
	public CallableStatement wrap(java.sql.CallableStatement wrapped) {
		if (wrapped == null) return null;
		if (wrapped instanceof CallableStatement)
			return (CallableStatement)wrapped;
		return new CallableStatement(this, wrapped);
	}

	@Override
	public ResultSet wrap(java.sql.ResultSet wrapped) {
		if (wrapped == null) return null;
		if (wrapped instanceof ResultSet) return (ResultSet)wrapped;
		return new ResultSet(this, wrapped);
	}

	@Override
	public ParameterMetaData wrap(java.sql.ParameterMetaData wrapped) {
		if (wrapped == null) return null;
		if (wrapped instanceof ParameterMetaData)
			return (ParameterMetaData)wrapped;
		return new ParameterMetaData(this, wrapped);
	}

	@Override
	public ResultSetMetaData wrap(java.sql.ResultSetMetaData wrapped) {
		if (wrapped == null) return null;
		if (wrapped instanceof ResultSetMetaData)
			return (ResultSetMetaData)wrapped;
		return new ResultSetMetaData(this, wrapped);
	}

	@Override
	public Array createArrayOf(String typeName, Object[] elements) {
		try {
			return this.wrap(
					this.wrapped.createArrayOf(typeName, elements));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Struct createStruct(String typeName, Object[] attributes) {
		try {
			return this.wrap(
					this.wrapped.createStruct(typeName, attributes));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Blob createBlob() {
		try {
			return this.wrap(
					this.wrapped.createBlob());
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Clob createClob() {
		try {
			return this.wrap(
					this.wrapped.createClob());
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public NClob createNClob() {
		try {
			return this.wrap(
					this.wrapped.createNClob());
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public SQLXML createSQLXML() {
		try {
			return this.wrap(
					this.wrapped.createSQLXML());
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Statement createStatement(
			int resultSetType,
			int resultSetConcurrency,
			int resultSetHoldability) {
		try {
			return this.wrap(
					this.wrapped.createStatement(
							resultSetType,
							resultSetConcurrency,
							resultSetHoldability));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Statement createStatement(
			int resultSetType,
			int resultSetConcurrency) {
		try {
			return this.wrap(
					this.wrapped.createStatement(
							resultSetType,
							resultSetConcurrency));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Statement createStatement() {
		try {
			return this.wrap(
					this.wrapped.createStatement());
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public PreparedStatement prepareStatement(
			String query,
			int[] columnIndexes) {
		try {
			return this.wrap(
					this.wrapped.prepareStatement(query, columnIndexes));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public PreparedStatement prepareStatement(
			String query,
			String[] columnNames) {
		try {
			return this.wrap(
					this.wrapped.prepareStatement(query, columnNames));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public PreparedStatement prepareStatement(
			String query,
			int autoGeneratedKeys) {
		try {
			return this.wrap(
					this.wrapped.prepareStatement(query, autoGeneratedKeys));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public PreparedStatement prepareStatement(
			String query,
			int resultSetType,
			int resultSetConcurrency,
			int resultSetHoldability) {
		try {
			return this.wrap(
					this.wrapped.prepareStatement(
							query,
							resultSetType,
							resultSetConcurrency,
							resultSetHoldability));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public PreparedStatement prepareStatement(
			String query,
			int resultSetType,
			int resultSetConcurrency) {
		try {
			return this.wrap(
					this.wrapped.prepareStatement(
							query,
							resultSetType,
							resultSetConcurrency));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public PreparedStatement prepareStatement(
			String query) {
		try {
			return this.wrap(
					this.wrapped.prepareStatement(
							query));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	public static Connection wrap(java.sql.Connection wrapped) {
		if (wrapped == null) return null;
		if (wrapped instanceof Connection) return (Connection)wrapped;
		return new Connection(wrapped);
	}








	@Override
	public CallableStatement prepareCall(
			String query,
			int resultSetType,
			int resultSetConcurrency,
			int resultSetHoldability) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CallableStatement prepareCall(
			String query,
			int resultSetType,
			int resultSetConcurrency) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CallableStatement prepareCall(
			String query) {
		// TODO Auto-generated method stub
		return null;
	}





	@Override
	public String nativeSQL(String sql) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAutoCommit(boolean autoCommit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getAutoCommit() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void commit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rollback() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Savepoint setSavepoint(String name) {
		try {
			return this.wrap(
					this.wrapped.setSavepoint(name));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Savepoint setSavepoint() {
		try {
			return this.wrap(
					this.wrapped.setSavepoint());
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void rollback(java.sql.Savepoint savepoint) {
		try {
			this.wrapped.rollback(this.unwrap(savepoint));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void releaseSavepoint(java.sql.Savepoint savepoint) {
		try {
			this.wrapped.releaseSavepoint(this.unwrap(savepoint));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isClosed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DatabaseMetaData getMetaData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setCatalog(String catalog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCatalog() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTransactionIsolation(int level) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getTransactionIsolation() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SQLWarning getWarnings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearWarnings() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Class<?>> getTypeMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTypeMap(Map<String, Class<?>> map) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHoldability(int holdability) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getHoldability() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isValid(int timeout) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setClientInfo(String name, String value)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setClientInfo(Properties properties)
			throws SQLClientInfoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getClientInfo(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties getClientInfo() {
		// TODO Auto-generated method stub
		return null;
	}

}

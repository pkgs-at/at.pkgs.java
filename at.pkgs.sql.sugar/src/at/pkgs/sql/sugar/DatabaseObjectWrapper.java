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

public interface DatabaseObjectWrapper<WrappedType> extends Wrapper {

	@Override
	public boolean isWrapperFor(Class<?> type);

	@Override
	public <Type> Type unwrap(Class<Type> type);

	public WrappedType unwrap();

	public <Type> Type unwrap(Type value);

	public Savepoint wrap(java.sql.Savepoint wrapped);

	public RowId wrap(java.sql.RowId wrapped);

	public Ref wrap(java.sql.Ref wrapped);

	public Array wrap(java.sql.Array wrapped);

	public Struct wrap(java.sql.Struct wrapped);

	public Blob wrap(java.sql.Blob wrapped);

	public Clob wrap(java.sql.Clob wrapped);

	public NClob wrap(java.sql.NClob wrapped);

	public SQLXML wrap(java.sql.SQLXML wrapped);

	public Statement wrap(java.sql.Statement wrapped);

	public PreparedStatement wrap(java.sql.PreparedStatement wrapped);

	public CallableStatement wrap(java.sql.CallableStatement wrapped);

	public ResultSet wrap(java.sql.ResultSet wrapped);

	public ParameterMetaData wrap(java.sql.ParameterMetaData wrapped);

	public ResultSetMetaData wrap(java.sql.ResultSetMetaData wrapped);

}

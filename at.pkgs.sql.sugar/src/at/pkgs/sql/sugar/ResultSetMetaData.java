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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.sql.SQLException;

public class ResultSetMetaData
extends DatabaseObject<java.sql.ResultSetMetaData>
implements java.sql.ResultSetMetaData, Iterable<ResultSetColumn> {

	private class Column implements ResultSetColumn {

		private final int column;

		private Column(int column) {
			this.column = column;
		}

		@Override
		public int getColumn() {
			return this.column;
		}

		@Override
		public boolean isAutoIncrement() {
			return ResultSetMetaData.this.isAutoIncrement(this.column);
		}

		@Override
		public boolean isCaseSensitive() {
			return ResultSetMetaData.this.isCaseSensitive(this.column);
		}

		@Override
		public boolean isSearchable() {
			return ResultSetMetaData.this.isSearchable(this.column);
		}

		@Override
		public boolean isCurrency() {
			return ResultSetMetaData.this.isCurrency(this.column);
		}

		@Override
		public int isNullable() {
			return ResultSetMetaData.this.isNullable(this.column);
		}

		@Override
		public boolean isSigned() {
			return ResultSetMetaData.this.isSigned(this.column);
		}

		@Override
		public int getColumnDisplaySize() {
			return ResultSetMetaData.this.getColumnDisplaySize(this.column);
		}

		@Override
		public String getColumnLabel() {
			return ResultSetMetaData.this.getColumnLabel(this.column);
		}

		@Override
		public String getColumnName() {
			return ResultSetMetaData.this.getColumnName(this.column);
		}

		@Override
		public String getSchemaName() {
			return ResultSetMetaData.this.getSchemaName(this.column);
		}

		@Override
		public int getPrecision() {
			return ResultSetMetaData.this.getPrecision(this.column);
		}

		@Override
		public int getScale() {
			return ResultSetMetaData.this.getScale(this.column);
		}

		@Override
		public String getTableName() {
			return ResultSetMetaData.this.getTableName(this.column);
		}

		@Override
		public String getCatalogName() {
			return ResultSetMetaData.this.getCatalogName(this.column);
		}

		@Override
		public int getColumnType() {
			return ResultSetMetaData.this.getColumnType(this.column);
		}

		@Override
		public String getColumnTypeName() {
			return ResultSetMetaData.this.getColumnTypeName(this.column);
		}

		@Override
		public boolean isReadOnly() {
			return ResultSetMetaData.this.isReadOnly(this.column);
		}

		@Override
		public boolean isWritable() {
			return ResultSetMetaData.this.isWritable(this.column);
		}

		@Override
		public boolean isDefinitelyWritable() {
			return ResultSetMetaData.this.isDefinitelyWritable(this.column);
		}

		@Override
		public String getColumnClassName() {
			return ResultSetMetaData.this.getColumnClassName(this.column);
		}

	}

	private class ColumnIterator implements Iterator<ResultSetColumn> {

		private final int length;

		private int index;

		private ColumnIterator() {
			this.length = ResultSetMetaData.this.getColumnCount();
			this.index = 0;
		}

		@Override
		public boolean hasNext() {
			return  this.index < this.length;
		}

		@Override
		public ResultSetColumn next() {
			if (!this.hasNext()) throw new NoSuchElementException();
			return new Column(++ this.index);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("remove()");
		}

	}

	public ResultSetMetaData(
			Connection connection,
			java.sql.ResultSetMetaData wrapped) {
		super(connection, wrapped);
	}

	@Override
	public int getColumnCount() {
		try {
			return this.unwrap().getColumnCount();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean isAutoIncrement(int column) {
		try {
			return this.unwrap().isAutoIncrement(column);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean isCaseSensitive(int column) {
		try {
			return this.unwrap().isCaseSensitive(column);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean isSearchable(int column) {
		try {
			return this.unwrap().isSearchable(column);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean isCurrency(int column) {
		try {
			return this.unwrap().isCurrency(column);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int isNullable(int column) {
		try {
			return this.unwrap().isNullable(column);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean isSigned(int column) {
		try {
			return this.unwrap().isSigned(column);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int getColumnDisplaySize(int column) {
		try {
			return this.unwrap().getColumnDisplaySize(column);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public String getColumnLabel(int column) {
		try {
			return this.unwrap().getColumnLabel(column);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public String getColumnName(int column) {
		try {
			return this.unwrap().getColumnName(column);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public String getSchemaName(int column) {
		try {
			return this.unwrap().getSchemaName(column);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int getPrecision(int column) {
		try {
			return this.unwrap().getPrecision(column);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int getScale(int column) {
		try {
			return this.unwrap().getScale(column);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public String getTableName(int column) {
		try {
			return this.unwrap().getTableName(column);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public String getCatalogName(int column) {
		try {
			return this.unwrap().getCatalogName(column);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int getColumnType(int column) {
		try {
			return this.unwrap().getColumnType(column);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public String getColumnTypeName(int column) {
		try {
			return this.unwrap().getColumnTypeName(column);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean isReadOnly(int column) {
		try {
			return this.unwrap().isReadOnly(column);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean isWritable(int column) {
		try {
			return this.unwrap().isWritable(column);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean isDefinitelyWritable(int column) {
		try {
			return this.unwrap().isDefinitelyWritable(column);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public String getColumnClassName(int column) {
		try {
			return this.unwrap().getColumnClassName(column);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Iterator<ResultSetColumn> iterator() {
		return new ColumnIterator();
	}

}

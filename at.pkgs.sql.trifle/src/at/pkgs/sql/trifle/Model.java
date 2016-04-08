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

package at.pkgs.sql.trifle;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import at.pkgs.sql.trifle.dialect.Dialect;

public class Model<ColumnType extends Model.Column> implements Serializable {

	public static interface Column {

		public void expression(Query query);

	}

	public static abstract class Via<ModelType extends Model<?>> {

		protected final Dialect.Table table;

		private volatile List<Column> columns;

		private volatile Constructor<?> constructor;

		protected Via() {
			this.table = new Dialect.Table() {

				@Override
				public void from(Query query) {
					Via.this.from(query);
				}

				@Override
				public List<Column> getColumns() {
					return Via.this.getColumns();
				}

			};
		}

		protected abstract AbstractDatabase getDatabase();

		protected Dialect getDialect() {
			return this.getDatabase().getDialect();
		}

		protected Connection getConnection() throws SQLException {
			return this.getDatabase().getConnection();
		}

		protected Query query() {
			return this.getDatabase().query();
		}

		protected <ResultType> ResultType transaction(
				AbstractDatabase.Function<ResultType> transaction)
						throws SQLException {
			return this.getDatabase().transaction(transaction);
		}

		protected void transaction(
				AbstractDatabase.Action transaction)
						throws SQLException {
			this.getDatabase().transaction(transaction);
		}

		private List<Column> columns() {
			List<Column> columns;
			Class<?> model;

			columns = new ArrayList<Column>();
			model = this.getClass().getEnclosingClass();
			for (Class<?> content : model.getDeclaredClasses()) {
				if (!content.isEnum()) continue;
				if (!Column.class.isAssignableFrom(content)) continue;
				if (content.getDeclaringClass() != model) continue;
				for (Object constant : content.getEnumConstants())
					columns.add((Column)constant);
			}
			return columns;
		}

		@SuppressWarnings("unchecked")
		protected <ColumnType extends Column> List<ColumnType> getColumns() {
			if (this.columns == null) {
				synchronized (this) {
					if (this.columns == null)
						this.columns = Collections.unmodifiableList(this.columns());
				}
			}
			return (List<ColumnType>)this.columns;
		}

		private Constructor<?> constructor() {
			try {
				return this.getClass().getEnclosingClass().getConstructor();
			}
			catch (Exception cause) {
				throw new RuntimeException(cause);
			}
		}

		@SuppressWarnings("unchecked")
		protected Constructor<ModelType> getConstructor() {
			if (this.constructor == null) {
				synchronized (this) {
					if (this.constructor == null)
						this.constructor = this.constructor();
				}
			}
			return (Constructor<ModelType>)this.constructor;
		}

		protected ModelType model() {
			try {
				return this.getConstructor().newInstance();
			}
			catch (RuntimeException cause) {
				throw cause;
			}
			catch (Exception cause) {
				throw new RuntimeException(cause);
			}
		}

		@SuppressWarnings("unchecked")
		protected ModelType populate(
				List<Column> columns,
				Object... values) {
			ModelType model;
			Map<Object, Object> map;

			model = this.model();
			map = (Map<Object, Object>)model.values;
			for (int index = 0; index < values.length; index ++)
				map.put(columns.get(index), values[index]);
			return (ModelType)model;
		}

		protected ModelType populateOne(
				List<Column> columns,
				ResultSet result)
						throws SQLException {
			Object[] values;

			values = new Object[result.getMetaData().getColumnCount()];
			if (!result.next()) return null;
			for (int index = 0; index < values.length; index ++)
				values[index] = result.getObject(index + 1);
			return (ModelType)this.populate(columns, values);
		}

		protected List<ModelType> populateAll(
				List<Column> columns,
				ResultSet result)
						throws SQLException {
			List<ModelType> models;
			Object[] values;

			models = new ArrayList<ModelType>();
			values = new Object[result.getMetaData().getColumnCount()];
			while (result.next()) {
				for (int index = 0; index < values.length; index ++)
					values[index] = result.getObject(index + 1);
				models.add((ModelType)this.populate(columns, values));
			}
			return models;
		}

		protected abstract void from(Query query);

		public ModelType retrieveOne(
				Connection connection,
				Query.Criteria criteria,
				Query.OrderBy sort)
						throws SQLException {
			Query query;
			boolean close;

			query = this.query();
			this.getDialect().select(
					query,
					this.table,
					false,
					criteria,
					sort,
					-1L,
					-1L);
			if (connection == null) {
				connection = this.getConnection();
				close = true;
			}
			else {
				close = false;
			}
			try {
				return this.populateOne(
						this.getColumns(),
						query.prepare(connection).executeQuery());
			}
			finally {
				if (close) connection.close();
			}
		}

		public ModelType retrieveOne(
				Connection connection,
				Query.Criteria criteria)
						throws SQLException {
			return this.retrieveOne(connection, criteria, null);
		}

		public ModelType retrieveOne(
				Connection connection)
						throws SQLException {
			return this.retrieveOne(connection, null, null);
		}

		public ModelType retrieveOne(
				Connection connection,
				Query.OrderBy sort)
						throws SQLException {
			return this.retrieveOne(connection, null, sort);
		}

		public ModelType retrieveOne(
				Query.Criteria criteria,
				Query.OrderBy sort)
						throws SQLException {
			return this.retrieveOne(null, criteria, sort);
		}

		public ModelType retrieveOne(
				Query.Criteria criteria)
						throws SQLException {
			return this.retrieveOne(null, criteria, null);
		}

		public ModelType retrieveOne(
				Query.OrderBy sort)
						throws SQLException {
			return this.retrieveOne(null, null, sort);
		}

		public ModelType retrieveOne() throws SQLException {
			return this.retrieveOne(null, null, null);
		}

		protected List<ModelType> retrieveAny(
				Connection connection,
				boolean distinct,
				Query.Criteria criteria,
				Query.OrderBy sort,
				long offset,
				long length)
						throws SQLException {
			Query query;
			boolean close;

			query = this.query();
			this.getDialect().select(
					query,
					this.table,
					distinct,
					criteria,
					sort,
					offset,
					length);
			if (connection == null) {
				connection = this.getConnection();
				close = true;
			}
			else {
				close = false;
			}
			try {
				return this.populateAll(
						this.getColumns(),
						query.prepare(connection).executeQuery());
			}
			finally {
				if (close) connection.close();
			}
		}

		public List<ModelType> retrieveAll(
				Connection connection,
				Query.Criteria criteria,
				Query.OrderBy sort,
				long offset,
				long length)
						throws SQLException {
			return this.retrieveAny(
					connection,
					false,
					criteria,
					sort,
					offset,
					length);
		}

		public List<ModelType> retrieveAll(
				Connection connection,
				Query.Criteria criteria,
				Query.OrderBy sort)
						throws SQLException {
			return this.retrieveAny(
					connection,
					false,
					criteria,
					sort,
					-1L,
					-1L);
		}

		public List<ModelType> retrieveAll(
				Connection connection,
				Query.Criteria criteria,
				long offset,
				long length)
						throws SQLException {
			return this.retrieveAny(
					connection,
					false,
					criteria,
					null,
					offset,
					length);
		}

		public List<ModelType> retrieveAll(
				Connection connection,
				Query.Criteria criteria)
						throws SQLException {
			return this.retrieveAny(
					connection,
					false,
					criteria,
					null,
					-1L,
					-1L);
		}

		public List<ModelType> retrieveAll(
				Connection connection,
				Query.OrderBy sort,
				long offset,
				long length)
						throws SQLException {
			return this.retrieveAny(
					connection,
					false,
					null,
					sort,
					offset,
					length);
		}

		public List<ModelType> retrieveAll(
				Connection connection,
				Query.OrderBy sort)
						throws SQLException {
			return this.retrieveAny(
					connection,
					false,
					null,
					sort,
					-1L,
					-1L);
		}

		public List<ModelType> retrieveAll(
				Connection connection,
				long offset,
				long length)
						throws SQLException {
			return this.retrieveAny(
					connection,
					false,
					null,
					null,
					offset,
					length);
		}

		public List<ModelType> retrieveAll(
				Connection connection)
						throws SQLException {
			return this.retrieveAny(
					connection,
					false,
					null,
					null,
					-1L,
					-1L);
		}

		public List<ModelType> retrieveAll(
				Query.Criteria criteria,
				Query.OrderBy sort,
				long offset,
				long length)
						throws SQLException {
			return this.retrieveAny(
					null,
					false,
					criteria,
					sort,
					offset,
					length);
		}

		public List<ModelType> retrieveAll(
				Query.Criteria criteria,
				Query.OrderBy sort)
						throws SQLException {
			return this.retrieveAny(
					null,
					false,
					criteria,
					sort,
					-1L,
					-1L);
		}

		public List<ModelType> retrieveAll(
				Query.Criteria criteria,
				long offset,
				long length)
						throws SQLException {
			return this.retrieveAny(
					null,
					false,
					criteria,
					null,
					offset,
					length);
		}

		public List<ModelType> retrieveAll(
				Query.Criteria criteria)
						throws SQLException {
			return this.retrieveAny(
					null,
					false,
					criteria,
					null,
					-1L,
					-1L);
		}

		public List<ModelType> retrieveAll(
				Query.OrderBy sort,
				long offset,
				long length)
						throws SQLException {
			return this.retrieveAny(
					null,
					false,
					null,
					sort,
					offset,
					length);
		}

		public List<ModelType> retrieveAll(
				Query.OrderBy sort)
						throws SQLException {
			return this.retrieveAny(
					null,
					false,
					null,
					sort,
					-1L,
					-1L);
		}

		public List<ModelType> retrieveAll(
				long offset,
				long length)
						throws SQLException {
			return this.retrieveAny(
					null,
					false,
					null,
					null,
					offset,
					length);
		}

		public List<ModelType> retrieveAll()
						throws SQLException {
			return this.retrieveAny(
					null,
					false,
					null,
					null,
					-1L,
					-1L);
		}

		public List<ModelType> retrieveDistinct(
				Connection connection,
				Query.Criteria criteria,
				Query.OrderBy sort,
				long offset,
				long length)
						throws SQLException {
			return this.retrieveAny(
					connection,
					true,
					criteria,
					sort,
					offset,
					length);
		}

		public List<ModelType> retrieveDistinct(
				Connection connection,
				Query.Criteria criteria,
				Query.OrderBy sort)
						throws SQLException {
			return this.retrieveAny(
					connection,
					true,
					criteria,
					sort,
					-1L,
					-1L);
		}

		public List<ModelType> retrieveDistinct(
				Connection connection,
				Query.Criteria criteria,
				long offset,
				long length)
						throws SQLException {
			return this.retrieveAny(
					connection,
					true,
					criteria,
					null,
					offset,
					length);
		}

		public List<ModelType> retrieveDistinct(
				Connection connection,
				Query.Criteria criteria)
						throws SQLException {
			return this.retrieveAny(
					connection,
					true,
					criteria,
					null,
					-1L,
					-1L);
		}

		public List<ModelType> retrieveDistinct(
				Connection connection,
				Query.OrderBy sort,
				long offset,
				long length)
						throws SQLException {
			return this.retrieveAny(
					connection,
					true,
					null,
					sort,
					offset,
					length);
		}

		public List<ModelType> retrieveDistinct(
				Connection connection,
				Query.OrderBy sort)
						throws SQLException {
			return this.retrieveAny(
					connection,
					true,
					null,
					sort,
					-1L,
					-1L);
		}

		public List<ModelType> retrieveDistinct(
				Connection connection,
				long offset,
				long length)
						throws SQLException {
			return this.retrieveAny(
					connection,
					true,
					null,
					null,
					offset,
					length);
		}

		public List<ModelType> retrieveDistinct(
				Connection connection)
						throws SQLException {
			return this.retrieveAny(
					connection,
					true,
					null,
					null,
					-1L,
					-1L);
		}

		public List<ModelType> retrieveDistinct(
				Query.Criteria criteria,
				Query.OrderBy sort,
				long offset,
				long length)
						throws SQLException {
			return this.retrieveAny(
					null,
					true,
					criteria,
					sort,
					offset,
					length);
		}

		public List<ModelType> retrieveDistinct(
				Query.Criteria criteria,
				Query.OrderBy sort)
						throws SQLException {
			return this.retrieveAny(
					null,
					true,
					criteria,
					sort,
					-1L,
					-1L);
		}

		public List<ModelType> retrieveDistinct(
				Query.Criteria criteria,
				long offset,
				long length)
						throws SQLException {
			return this.retrieveAny(
					null,
					true,
					criteria,
					null,
					offset,
					length);
		}

		public List<ModelType> retrieveDistinct(
				Query.Criteria criteria)
						throws SQLException {
			return this.retrieveAny(
					null,
					true,
					criteria,
					null,
					-1L,
					-1L);
		}

		public List<ModelType> retrieveDistinct(
				Query.OrderBy sort,
				long offset,
				long length)
						throws SQLException {
			return this.retrieveAny(
					null,
					true,
					null,
					sort,
					offset,
					length);
		}

		public List<ModelType> retrieveDistinct(
				Query.OrderBy sort)
						throws SQLException {
			return this.retrieveAny(
					null,
					true,
					null,
					sort,
					-1L,
					-1L);
		}

		public List<ModelType> retrieveDistinct(
				long offset,
				long length)
						throws SQLException {
			return this.retrieveAny(
					null,
					true,
					null,
					null,
					offset,
					length);
		}

		public List<ModelType> retrieveDistinct()
						throws SQLException {
			return this.retrieveAny(
					null,
					true,
					null,
					null,
					-1L,
					-1L);
		}

		public long count(
				Connection connection,
				Query.Criteria criteria)
						throws SQLException {
			Query query;
			boolean close;

			query = this.query();
			this.getDialect().count(
					query,
					this.table,
					criteria);
			if (connection == null) {
				connection = this.getConnection();
				close = true;
			}
			else {
				close = false;
			}
			try {
				ResultSet result;

				result = query.prepare(connection).executeQuery();
				return result.next() ? result.getLong(1) : 0L;
			}
			finally {
				if (close) connection.close();
			}
		}

		public long count(
				Connection connection)
						throws SQLException {
			return this.count(
					connection,
					null);
		}

		public long count(
				Query.Criteria criteria)
						throws SQLException {
			return this.count(
					null,
					criteria);
		}

		public long count() throws SQLException {
			return this.count(
					null,
					null);
		}

	}

	private static final long serialVersionUID = 1L;

	final Map<ColumnType, Object> values;

	public Model() {
		this.values = new LinkedHashMap<ColumnType, Object>();
	}

	public Map<ColumnType, Object> getValues() {
		return Collections.unmodifiableMap(this.values);
	}

	@SuppressWarnings("unchecked")
	public <ValueType> ValueType get(ColumnType column) {
		return (ValueType)this.values.get(column);
	}

	protected void set(ColumnType column, Object value) {
		this.values.put(column, value);
	}

}

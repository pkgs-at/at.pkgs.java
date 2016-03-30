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
import javax.sql.DataSource;

public class Model<ColumnType extends Model.Column> implements Serializable {

	public static interface Column {

		public void expression(Query query);

	}

	public static abstract class Transaction<ResultType> {

		public enum Level {

			READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),

			READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),

			REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),

			SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

			private final int value;

			private Level(int value) {
				this.value = value;
			}

		}

		private final Level level;

		private final double interval;

		private final int retry;

		public Transaction(Level level, double interval, int retry) {
			this.level = level;
			this.interval = interval;
			this.retry = retry;
		}

		protected abstract ResultType execute(
				Connection connection)
						throws SQLException;

		public ResultType execute(
				DataSource source)
						throws SQLException {
			int retry;

			retry = this.retry;
			while (true) {
				Connection connection;
				Connection rollback;

				-- retry;
				connection = null;
				rollback = null;
				try {
					ResultType result;

					connection = source.getConnection();
					connection.setAutoCommit(false);
					connection.setTransactionIsolation(this.level.value);
					rollback = connection;
					result = this.execute(connection);
					connection.commit();
					rollback = null;
					try {
						connection.setAutoCommit(true);
					}
					catch (SQLException cause) {
						throw new RuntimeException(cause);
					}
					return result;
				}
				catch (SQLException cause) {
					try {
						if (rollback != null) rollback.rollback();
					}
					catch (SQLException ignored) {
						// do nothing
					}
					if (retry < 0) throw cause;
					if (this.interval > 0D) {
						double time;

						time = Math.random() * 1000D * this.interval;
						try {
							Thread.sleep(Double.valueOf(time).longValue());
						}
						catch (InterruptedException ignored) {
							// do nothing
						}
					}
				}
				finally {
					try {
						if (connection != null) connection.close();
					}
					catch (SQLException ignored) {
						// do nothing
					}
				}
			}
		}

	}

	public static abstract class Via<ModelType extends Model<?>> {

		private List<Column> columns;

		private Constructor<?> constructor;

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
		public <ColumnType extends Column> List<ColumnType> getColumns() {
			if (this.columns == null) {
				synchronized (this) {
					if (this.columns == null)
						this.columns = this.columns();
				}
			}
			return (List<ColumnType>)this.columns;
		}

		private Constructor<?> constructor() {
			try {
				return this.getClass()
						.getEnclosingClass()
						.getConstructor();
			}
			catch (Exception cause) {
				throw new RuntimeException(cause);
			}
		}

		@SuppressWarnings("unchecked")
		public Constructor<ModelType> getConstructor() {
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
				DataSource source,
				Query.Criteria criteria,
				Query.OrderBy sort)
						throws SQLException {
			Query query;
			Connection connection;

			query = new Query();
			query.append("SELECT ALL ").join(", ", this.getColumns());
			this.from(query.append(" FROM "));
			if (criteria != null) query.append(" WHERE ").append(criteria);
			if (sort != null) query.append(sort);
			connection = null;
			try {
				connection = source.getConnection();
				return this.populateOne(
						this.getColumns(),
						query.prepare(connection).executeQuery());
			}
			finally {
				if (connection != null) connection.close();
			}
		}

		public ModelType retrieveOne(
				DataSource source,
				Query.Criteria criteria)
						throws SQLException {
			return this.retrieveOne(source, criteria, null);
		}

		public ModelType retrieveOne(
				DataSource source,
				Query.OrderBy sort)
						throws SQLException {
			return this.retrieveOne(source, null, sort);
		}

		public ModelType retrieveOne(
				DataSource source)
						throws SQLException {
			return this.retrieveOne(source, null, null);
		}

		protected List<ModelType> retrieveAny(
				DataSource source,
				boolean distinct,
				Query.Criteria criteria,
				Query.OrderBy sort,
				long offset,
				long length)
						throws SQLException {
			Query query;
			Connection connection;

			if (offset > 0L || length > 0L)
				throw new UnsupportedOperationException();
			query = new Query();
			query.append("SELECT ").append(distinct ? "DISTINCT" : "ALL");
			query.append(' ').join(", ", this.getColumns());
			this.from(query.append(" FROM "));
			if (criteria != null) query.append(" WHERE ").append(criteria);
			if (sort != null) query.append(sort);
			connection = null;
			try {
				connection = source.getConnection();
				return this.populateAll(
						this.getColumns(),
						query.prepare(connection).executeQuery());
			}
			finally {
				if (connection != null) connection.close();
			}
		}

		public List<ModelType> retrieveAll(
				DataSource source,
				Query.Criteria criteria,
				Query.OrderBy sort,
				long offset,
				long length)
						throws SQLException {
			return this.retrieveAny(
					source,
					false,
					criteria,
					sort,
					offset,
					length);
		}

		public List<ModelType> retrieveAll(
				DataSource source,
				Query.Criteria criteria,
				Query.OrderBy sort)
						throws SQLException {
			return this.retrieveAny(
					source,
					false,
					criteria,
					sort,
					-1L,
					-1L);
		}

		public List<ModelType> retrieveAll(
				DataSource source,
				Query.Criteria criteria,
				long offset,
				long length)
						throws SQLException {
			return this.retrieveAny(
					source,
					false,
					criteria,
					null,
					offset,
					length);
		}

		public List<ModelType> retrieveAll(
				DataSource source,
				Query.Criteria criteria)
						throws SQLException {
			return this.retrieveAny(
					source,
					false,
					criteria,
					null,
					-1L,
					-1L);
		}

		public List<ModelType> retrieveAll(
				DataSource source,
				Query.OrderBy sort,
				long offset,
				long length)
						throws SQLException {
			return this.retrieveAny(
					source,
					false,
					null,
					sort,
					offset,
					length);
		}

		public List<ModelType> retrieveAll(
				DataSource source,
				Query.OrderBy sort)
						throws SQLException {
			return this.retrieveAny(
					source,
					false,
					null,
					sort,
					-1L,
					-1L);
		}

		public List<ModelType> retrieveAll(
				DataSource source,
				long offset,
				long length)
						throws SQLException {
			return this.retrieveAny(
					source,
					false,
					null,
					null,
					offset,
					length);
		}

		public List<ModelType> retrieveAll(
				DataSource source)
						throws SQLException {
			return this.retrieveAny(
					source,
					false,
					null,
					null,
					-1L,
					-1L);
		}

		public List<ModelType> retrieveDistinct(
				DataSource source,
				Query.Criteria criteria,
				Query.OrderBy sort,
				long offset,
				long length)
						throws SQLException {
			return this.retrieveAny(
					source,
					true,
					criteria,
					sort,
					offset,
					length);
		}

		public List<ModelType> retrieveDistinct(
				DataSource source,
				Query.Criteria criteria,
				Query.OrderBy sort)
						throws SQLException {
			return this.retrieveAny(
					source,
					true,
					criteria,
					sort,
					-1L,
					-1L);
		}

		public List<ModelType> retrieveDistinct(
				DataSource source,
				Query.Criteria criteria,
				long offset,
				long length)
						throws SQLException {
			return this.retrieveAny(
					source,
					true,
					criteria,
					null,
					offset,
					length);
		}

		public List<ModelType> retrieveDistinct(
				DataSource source,
				Query.Criteria criteria)
						throws SQLException {
			return this.retrieveAny(
					source,
					true,
					criteria,
					null,
					-1L,
					-1L);
		}

		public List<ModelType> retrieveDistinct(
				DataSource source,
				Query.OrderBy sort,
				long offset,
				long length)
						throws SQLException {
			return this.retrieveAny(
					source,
					true,
					null,
					sort,
					offset,
					length);
		}

		public List<ModelType> retrieveDistinct(
				DataSource source,
				Query.OrderBy sort)
						throws SQLException {
			return this.retrieveAny(
					source,
					true,
					null,
					sort,
					-1L,
					-1L);
		}

		public List<ModelType> retrieveDistinct(
				DataSource source,
				long offset,
				long length)
						throws SQLException {
			return this.retrieveAny(
					source,
					true,
					null,
					null,
					offset,
					length);
		}

		public List<ModelType> retrieveDistinct(
				DataSource source)
						throws SQLException {
			return this.retrieveAny(
					source,
					true,
					null,
					null,
					-1L,
					-1L);
		}

		public long count(
				DataSource source,
				Query.Criteria criteria)
						throws SQLException {
			Query query;
			Connection  connection;

			query = new Query();
			query.append("SELECT ALL COUNT(*)");
			this.from(query.append(" FROM "));
			if (criteria != null) query.append(" WHERE ").append(criteria);
			connection = null;
			try {
				ResultSet result;

				connection = source.getConnection();
				result = query.prepare(connection).executeQuery();
				return result.next() ? result.getLong(1) : 0L;
			}
			finally {
				if (connection != null) connection.close();
			}
		}

	}

	public static abstract class PostgreSQL<ModelType extends Model<?>>
	extends Via<ModelType> {

		@Override
		protected List<ModelType> retrieveAny(
				DataSource source,
				boolean distinct,
				Query.Criteria criteria,
				Query.OrderBy sort,
				long offset,
				long length)
						throws SQLException {
			Query query;
			Connection connection;

			query = new Query();
			query.append("SELECT ").append(distinct ? "DISTINCT" : "ALL");
			query.append(' ').join(", ", this.getColumns());
			this.from(query.append(" FROM "));
			if (criteria != null) query.append(" WHERE ").append(criteria);
			if (sort != null) query.append(sort);
			if (length > 0L)
				query.append(" LIMIT ").value(offset);
			if (offset > 0L)
				query.append(" OFFSET ").value(offset);
			connection = null;
			try {
				connection = source.getConnection();
				return this.populateAll(
						this.getColumns(),
						query.prepare(connection).executeQuery());
			}
			finally {
				if (connection != null) connection.close();
			}
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

/*
 * Copyright (c) 2009-2014, Architector Inc., Japan
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

package at.pkgs.sql.query;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.sql.Types;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import at.pkgs.sql.query.dialect.Dialect;

public class Database {

	public static class Exception
	extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public Exception(Throwable cause, String message) {
			super(message, cause);
		}

		public Exception(Throwable cause) {
			super(cause);
		}

		public Exception(String message) {
			super(message);
		}

		public Exception(Throwable cause, String format, Object... arguments) {
			this(cause, String.format(format, arguments));
		}

		public Exception(String format, Object... arguments) {
			this(String.format(format, arguments));
		}

	}

	public static interface DumpCollector {

		public static final DumpCollector out = new DumpCollector() {

			public void collect(String dump) {
				System.out.println(dump);
			}

		};

		public void collect(String dump);

	}

	public static interface Expression<TableType extends Enum<?>> {

		public void build(QueryBuilder<TableType> builder);

	}

	public enum Null {

		BigDecimal(Types.NUMERIC),

		Boolean(Types.BOOLEAN),

		Byte(Types.TINYINT),

		Bytes(Types.VARBINARY),

		Date(Types.DATE),

		Double(Types.DOUBLE),

		Float(Types.FLOAT),

		Integer(Types.INTEGER),

		Long(Types.BIGINT),

		String(Types.VARCHAR),

		Time(Types.TIME),

		Timestamp(Types.TIMESTAMP);

		private final int type;

		private Null(int type) {
			this.type = type;
		}

		@Override
		public String toString() {
			return new StringBuilder("(typed NULL of ")
					.append(this.name())
					.append(')')
					.toString();
		}

	}

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Table {

		public String schema() default "";

		public String name();

	}

	public static enum ColumnValue {

		ModelValue,

		KeepOriginal,

		DefaultValue,

		CurrentTimestamp,

		CurrentDate,

		CurrentTime;

	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Column {

		public String name();

		public boolean primaryKey() default false;

		public ColumnValue insertWith() default ColumnValue.ModelValue;

		public ColumnValue updateWith() default ColumnValue.ModelValue;

		public boolean returning() default false;

	}

	@Table(name = "")
	public enum EmptyTable {

		// nothing

	}

	public static interface EnumValue<DatabaseType> {

		public Class<DatabaseType> getDatabaseType();

		public DatabaseType getDatabaseValue();

	}

	public static abstract class Values<TableType extends Enum<?>>
	extends ValuesClause<TableType> {

		// nothing

	}

	public static abstract class And<TableType extends Enum<?>, ModelType>
	extends Criteria<TableType, ModelType> {

		void appendJoint(QueryBuilder<TableType> builder) {
			builder.append(" AND ");
		}

	}

	public static abstract class Or<TableType extends Enum<?>, ModelType>
	extends Criteria<TableType, ModelType> {

		void appendJoint(QueryBuilder<TableType> builder) {
			builder.append(" OR ");
		}

	}

	public static abstract class OrderBy<TableType extends Enum<?>>
	extends OrderByClause<TableType> {

		// nothing

	}

	public static abstract class Query <TableType extends Enum<?>, ModelType>
	extends at.pkgs.sql.query.Query<TableType, ModelType> {

		protected Query(
				Class<TableType> table,
				Class<ModelType> model) {
			super(table, model);
		}

	}

	private final Dialect dialect;

	private final DataSource source;

	private final ConcurrentMap<Class<?>, TableDefinition<?>> tables;

	private DumpCollector insertDumpCollector;

	private DumpCollector updateDumpCollector;

	private DumpCollector deleteDumpCollector;

	public Database(Dialect dialect, DataSource source) {
		this.dialect = dialect;
		this.source = source;
		this.tables = new ConcurrentHashMap<Class<?>, TableDefinition<?>>();
		this.insertDumpCollector = null;
		this.updateDumpCollector = null;
		this.deleteDumpCollector = null;
	}

	public void setInsertDumpCollector(DumpCollector collector) {
		this.insertDumpCollector = collector;
	}

	public void setUpdateDumpCollector(DumpCollector collector) {
		this.updateDumpCollector = collector;
	}

	public void setDeleteDumpCollector(DumpCollector collector) {
		this.deleteDumpCollector = collector;
	}

	Dialect getDialect() {
		return this.dialect;
	}

	@SuppressWarnings("unchecked")
	<TableType extends Enum<?>> TableDefinition<TableType> getTable(
			Class<TableType> type) {
		if (!this.tables.containsKey(type))
			this.tables.putIfAbsent(
					type,
					new TableDefinition<TableType>(type));
		return (TableDefinition<TableType>)this.tables.get(type);
	}

	public Connection getConnection() {
		try {
			return this.source.getConnection();
		}
		catch (SQLException throwable) {
			throw new Exception(throwable);
		}
	}

	public <TableType extends Enum<?>> QueryBuilder<TableType> newQueryBuilder(
			Class<TableType> table) {
		return new QueryBuilder<TableType>(this, table);
	}

	public <TableType extends Enum<?>, ModelType>
	Query<TableType, ModelType> query(
			Class<TableType> table,
			Class<ModelType> model) {
		return new Query<TableType, ModelType>(table, model) {{
			this.prepare(Database.this);
		}};
	}

	public <TableType extends Enum<?>, ModelType>
	Query<TableType, ModelType> query(
			Query<TableType, ModelType> query) {
		query.prepare(this);
		return query;
	}

	PreparedStatement bindParameters(
			PreparedStatement statement,
			Iterable<Object> parameters) {
		try {
			int index;

			index = 0;
			for (Object parameter : parameters) {
				if (parameter instanceof Null)
					statement.setNull(++ index, ((Null)parameter).type);
				else
					statement.setObject(++ index, parameter);
			}
		}
		catch (SQLException throwable) {
			throw new Exception(throwable);
		}
		return statement;
	}

	public int executeAffectedRows(
			Connection connection,
			String query,
			Iterable<Object> parameters) {
		boolean close;
		PreparedStatement statement;

		close = (connection == null);
		statement = null;
		if (connection == null) connection = this.getConnection();
		try {
			statement = connection.prepareStatement(query);
			return this.bindParameters(statement, parameters).executeUpdate();
		}
		catch (SQLException throwable) {
			throw new Exception(throwable);
		}
		finally {
			try {
				if (statement != null) statement.close();
				if (close) connection.close();
			}
			catch (SQLException throwable) {
				throw new Exception(throwable);
			}
		}
	}

	public <TableType extends Enum<?>> List<List<Object>> executeGeneratedKeys(
			Connection connection,
			String query,
			Iterable<Object> parameters,
			Class<TableType> table,
			Iterable<TableType> columns) {
		boolean close;
		PreparedStatement statement;

		close = (connection == null);
		statement = null;
		if (connection == null) connection = this.getConnection();
		try {
			List<String> keys;
			ResultSet result;
			int length;
			List<List<Object>> rows;

			keys = new ArrayList<String>();
			for (TableType column : columns)
				keys.add(this.getTable(table).getColumn(column).getName());
			statement = connection.prepareStatement(
					query,
					keys.toArray(new String[keys.size()]));
			this.bindParameters(statement, parameters).executeUpdate();
			result = statement.getGeneratedKeys();
			length = result.getMetaData().getColumnCount();
			rows = new ArrayList<List<Object>>();
			while (result.next()) {
				List<Object> row;

				row = new ArrayList<Object>();
				for (int index = 0; index < length;)
					row.add(result.getObject(++ index));
				rows.add(row);
			}
			return rows;
		}
		catch (SQLException throwable) {
			throw new Exception(throwable);
		}
		finally {
			try {
				if (statement != null) statement.close();
				if (close) connection.close();
			}
			catch (SQLException throwable) {
				throw new Exception(throwable);
			}
		}
	}

	public ResultSet executeResultSet(
			Connection connection,
			String query,
			Iterable<Object> parameters) {
		if (connection == null) connection = this.getConnection();
		try {
			return this.bindParameters(
					connection.prepareStatement(query),
					parameters).executeQuery();
		}
		catch (SQLException throwable) {
			throw new Exception(throwable);
		}
	}

	public <TableType extends Enum<?>, ModelType> ModelType executeModel(
			Connection connection,
			Class<TableType> table,
			Class<ModelType> type,
			Iterable<TableType> columns,
			ModelType model,
			String query,
			Iterable<Object> parameters) {
		boolean close;
		ResultSet result;

		close = (connection == null);
		result = this.executeResultSet(connection, query, parameters);
		try {
			TableMapper<TableType, ModelType> mapper;
			int length;
			if (!result.next()) return null;
			length = result.getMetaData().getColumnCount();
			mapper = this.getTable(table).getMapper(type);
			return mapper.setValues(
					model,
					columns,
					new ResultRowIterable(
							result,
							length));
		}
		catch (SQLException throwable) {
			throw new Exception(throwable);
		}
		finally {
			try {
				connection = result.getStatement().getConnection();
				result.getStatement().close();
				if (close) connection.close();
			}
			catch (SQLException throwable) {
				throw new Exception(throwable);
			}
		}
	}

	public <TableType extends Enum<?>, ModelType>
	List<ModelType> executeModelList(
			Connection connection,
			Class<TableType> table,
			Class<ModelType> type,
			Iterable<TableType> columns,
			String query,
			Iterable<Object> parameters) {
		boolean close;
		ResultSet result;

		close = (connection == null);
		result = this.executeResultSet(connection, query, parameters);
		try {
			List<ModelType> list;
			TableMapper<TableType, ModelType> mapper;
			int length;

			list = new ArrayList<ModelType>();
			length = result.getMetaData().getColumnCount();
			mapper = this.getTable(table).getMapper(type);
			while (result.next())
				list.add(
						mapper.setValues(
								null,
								columns,
								new ResultRowIterable(
										result,
										length)));
			return list;
		}
		catch (SQLException throwable) {
			throw new Exception(throwable);
		}
		finally {
			try {
				connection = result.getStatement().getConnection();
				result.getStatement().close();
				if (close) connection.close();
			}
			catch (SQLException throwable) {
				throw new Exception(throwable);
			}
		}
	}

	public QueryExecutor execute(
			String query,
			Iterable<Object> parameters) {
		return new QueryExecutor(this, query, parameters);
	}

	public QueryExecutor execute(
			String query,
			Object parameters) {
		return this.execute(query, Arrays.asList(parameters));
	}

	@SuppressWarnings("unchecked")
	public <TableType extends Enum<?>, ModelType> int insert(
			Connection connection,
			Class<TableType> table,
			ModelType model) {
		Class<ModelType> type;
		TableDefinition<TableType> definition;
		TableMapper<TableType, ModelType> mapper;
		Values<TableType> values;
		List<TableType> returnings;
		final Map<TableType, Object> primaryKeys;

		type = (Class<ModelType>)model.getClass();
		definition = this.getTable(table);
		mapper = definition.getMapper(type);
		values = new Values<TableType>() {

			// nothing

		};
		returnings = new ArrayList<TableType>();
		primaryKeys = new LinkedHashMap<TableType, Object>();
		for (TableType column : definition.getPrimaryKeys())
			primaryKeys.put(column, null);
		for (TableType column : mapper.getColumns().keySet()) {
			ColumnDefinition<TableType> columnDefinition;
			ColumnValue value;

			columnDefinition = definition.getColumn(column);
			value = columnDefinition.getInsertWith();
			switch (value) {
			case ModelValue :
				Object modelValue;

				modelValue = mapper.getColumn(column).getValue(model);
				values.with(column, modelValue);
				if (!primaryKeys.containsKey(column)) break;
				if (modelValue instanceof Null) break;
				primaryKeys.put(column, modelValue);
				break;
			default :
				values.with(column, value);
				break;
			}
			if (columnDefinition.isReturning()) returnings.add(column);
		}
		if (returnings.size() <= 0) {
			return this.query(table, model.getClass())
					.set(values)
					.dumpInsertIf(
							this.insertDumpCollector != null,
							this.insertDumpCollector)
					.insert(connection);
		}
		else if (this.dialect.hasReturningSupport()) {
			return this.query(table, model.getClass())
					.columns(returnings)
					.set(values)
					.dumpInsertIf(
							this.insertDumpCollector != null,
							this.insertDumpCollector)
					.buildInsertQuery()
					.execute(type, returnings)
					.asModel(connection, model) == null ? 0 : 1;
		}
		else {
			boolean close;

			close = (connection == null);
			if (connection == null) connection = this.getConnection();
			try {
				List<TableType> generateds;

				generateds = new ArrayList<TableType>();
				for (TableType column : primaryKeys.keySet())
					if (primaryKeys.get(column) == null)
						generateds.add(column);
				if (generateds.size() > 0) {
					List<List<Object>> rows;

					rows = this.query(table, type)
							.set(values)
							.dumpInsertIf(
									this.insertDumpCollector != null,
									this.insertDumpCollector)
							.buildInsertQuery()
							.execute(generateds)
							.asGeneratedKeys(connection);
					if (rows.size() <= 0) return rows.size();
					for (int index = 0; index < generateds.size(); index ++)
						primaryKeys.put(
								generateds.get(index),
								rows.get(0).get(index));
				}
				else {
					int affected;

					affected = this.query(table, type)
							.set(values)
							.dumpInsertIf(
									this.insertDumpCollector != null,
									this.insertDumpCollector)
							.insert(connection);
					if (affected < 1) return affected;
				}
				this.query(table, type)
						.columns(returnings)
						.where(new And<TableType, ModelType>() {{
							for (TableType column : primaryKeys.keySet())
								with(column).is(primaryKeys.get(column));
						}})
						.dumpSelectIf(
								this.insertDumpCollector != null,
								this.insertDumpCollector)
						.buildSelectQuery()
						.execute(type, returnings)
						.asModel(connection, model);
				return 1;
			}
			finally {
				try {
					if (close) connection.close();
				}
				catch (SQLException throwable) {
					throw new Database.Exception(throwable);
				}
			}
		}
	}

	public <TableType extends Enum<?>, ModelType> int insert(
			Class<TableType> table,
			ModelType model) {
		return this.insert(null, table, model);
	}

	@SuppressWarnings("unchecked")
	public <TableType extends Enum<?>, ModelType> int update(
			Connection connection,
			Class<TableType> table,
			ModelType model) {
		Class<ModelType> type;
		TableDefinition<TableType> definition;
		TableMapper<TableType, ModelType> mapper;
		Values<TableType> values;
		List<TableType> returnings;
		final Map<TableType, Object> primaryKeys;
		And<TableType, ModelType> where;

		type = (Class<ModelType>)model.getClass();
		definition = this.getTable(table);
		mapper = definition.getMapper(type);
		values = new Values<TableType>() {

			// nothing

		};
		returnings = new ArrayList<TableType>();
		primaryKeys = new LinkedHashMap<TableType, Object>();
		for (TableType column : definition.getPrimaryKeys())
			primaryKeys.put(column, null);
		for (TableType column : mapper.getColumns().keySet()) {
			ColumnDefinition<TableType> columnDefinition;
			ColumnValue value;
			Object modelValue;

			columnDefinition = definition.getColumn(column);
			value = columnDefinition.getUpdateWith();
			modelValue = mapper.getColumn(column).getValue(model);
			if (value == ColumnValue.ModelValue)
				values.with(column, modelValue);
			else
				values.with(column, value);
			if (columnDefinition.isReturning()) returnings.add(column);
			if (!primaryKeys.containsKey(column)) continue;
			if (modelValue instanceof Null) continue;
			primaryKeys.put(column, modelValue);
		}
		where = new And<TableType, ModelType>() {{
			for (TableType column : primaryKeys.keySet()) {
				Object value;

				value = primaryKeys.get(column);
				if (value == null)
					throw new Database.Exception(
							"update key cannot be null: %s",
							column);
				with(column).is(value);
			}
		}};
		if (returnings.size() <= 0) {
			return this.query(table, type)
					.set(values)
					.where(where)
					.dumpUpdateIf(
							this.updateDumpCollector != null,
							this.updateDumpCollector)
					.update(connection);
		}
		else if (this.dialect.hasReturningSupport()) {
			return this.query(table, type)
					.columns(returnings)
					.set(values)
					.where(where)
					.dumpUpdateIf(
							this.updateDumpCollector != null,
							this.updateDumpCollector)
					.buildUpdateQuery()
					.execute(type, returnings)
					.asModel(connection, model) == null ? 0 : 1;
		}
		else {
			boolean close;

			close = (connection == null);
			if (connection == null) connection = this.getConnection();
			try {
				int affected;

				affected = this.query(table, type)
						.set(values)
						.where(where)
						.dumpUpdateIf(
								this.updateDumpCollector != null,
								this.updateDumpCollector)
						.update(connection);
				if (affected < 1) return affected;
				this.query(table, type)
						.columns(returnings)
						.where(where)
						.dumpSelectIf(
								this.updateDumpCollector != null,
								this.updateDumpCollector)
						.buildSelectQuery()
						.execute(type, returnings)
						.asModel(connection, model);
				return affected;
			}
			finally {
				try {
					if (close) connection.close();
				}
				catch (SQLException throwable) {
					throw new Database.Exception(throwable);
				}
			}
		}
	}

	public <TableType extends Enum<?>, ModelType> int update(
			Class<TableType> table,
			ModelType model) {
		return this.update(null,  table, model);
	}

	@SuppressWarnings("unchecked")
	public <TableType extends Enum<?>, ModelType> int delete(
			Connection connection,
			Class<TableType> table,
			final ModelType model) {
		Class<ModelType> type;
		final TableDefinition<TableType> definition;
		final TableMapper<TableType, ModelType> mapper;

		type = (Class<ModelType>)model.getClass();
		definition = this.getTable(table);
		mapper = definition.getMapper(type);
		return this.query(table, type)
				.where(new And<TableType, ModelType>() {{
					for (TableType column : definition.getPrimaryKeys()) {
						Object value;

						value = mapper.getColumn(column).getValue(model);
						if (value == null || value instanceof Null)
							throw new Database.Exception(
									"delete key cannot be null: %s",
									column);
						with(column).is(value);
					}
				}})
				.dumpDeleteIf(
						this.deleteDumpCollector != null,
						this.deleteDumpCollector)
				.delete(connection);
	}

	public <TableType extends Enum<?>, ModelType> int delete(
			Class<TableType> table,
			ModelType model) {
		return this.delete(table, model);
	}

}

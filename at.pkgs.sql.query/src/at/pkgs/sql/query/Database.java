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

	public static interface Criterion<TableType extends Enum<?>> {

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

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Column {

		public String name();

	}

	@Table(name = "")
	public enum EmptyTable {

		// nothing

	}

	public static interface EnumValue<DatabaseType> {

		public Class<DatabaseType> getDatabaseType();

		public DatabaseType getDatabaseValue();

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

	public Database(Dialect dialect, DataSource source) {
		this.dialect = dialect;
		this.source = source;
		this.tables = new ConcurrentHashMap<Class<?>, TableDefinition<?>>();
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

}

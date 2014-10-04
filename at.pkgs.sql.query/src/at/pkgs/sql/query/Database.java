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
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import at.pkgs.sql.query.dialect.Dialect;

public class Database {

	public static class Exception extends RuntimeException {

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

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Table {

		public String schema() default "";

		public String name();

		public Class<?> type();

	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Column {

		public String name();

	}

	public static abstract class And<TableType> extends Criteria<TableType> {

		void appendJoint(QueryBuilder<TableType> builder) {
			builder.append(" AND ");
		}

	}

	public static abstract class Or<TableType> extends Criteria<TableType> {

		void appendJoint(QueryBuilder<TableType> builder) {
			builder.append(" OR ");
		}

	}

	public static abstract class OrderBy<TableType>
	extends OrderByClause<TableType> {

		// nothing

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

	public <TableType extends Enum<?>> Query<TableType> query(
			Class<TableType> type) {
		return new Query<TableType>(this, this.getTable(type));
	}

}

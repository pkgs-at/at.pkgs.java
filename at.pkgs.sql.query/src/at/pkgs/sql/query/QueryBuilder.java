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

import java.util.List;
import java.util.ArrayList;
import java.io.PrintStream;
import at.pkgs.sql.query.dialect.Dialect;

public final class QueryBuilder<TableType extends Enum<?>> {

	private final Database database;

	private final Dialect dialect;

	private final TableDefinition<TableType> table;

	private final StringBuilder builder;

	private final List<Object> parameters;

	QueryBuilder(Database database, Class<TableType> table) {
		this.database = database;
		this.dialect = database.getDialect();
		this.table = database.getTable(table);
		this.builder = new StringBuilder();
		this.parameters = new ArrayList<Object>();
	}

	@Override
	public String toString() {
		return this.builder.toString();
	}

	List<Object> getParameters() {
		return this.parameters;
	}

	public QueryBuilder<TableType> appendIdentifier(
			String name) {
		this.dialect.appendIdentifier(this.builder, name);
		return this;
	}

	public QueryBuilder<TableType> append(
			Iterable<Object> parameters) {
		for (Object parameter : parameters)
			this.parameters.add(parameter);
		return this;
	}

	public QueryBuilder<TableType> append(
			Object... parameters) {
		for (Object parameter : parameters)
			this.parameters.add(parameter);
		return this;
	}

	public QueryBuilder<TableType> append(
			char value) {
		this.builder.append(value);
		return this;
	}

	public QueryBuilder<TableType> append(
			String value) {
		this.builder.append(value);
		return this;
	}

	public QueryBuilder<TableType> append(
			String query,
			Iterable<Object> parameters) {
		this.append(query);
		this.append(parameters);
		return this;
	}

	public QueryBuilder<TableType> append(
			String query,
			Object... parameters) {
		this.append(query);
		this.append(parameters);
		return this;
	}

	public QueryBuilder<TableType> append(
			TableType column) {
		this.appendIdentifier(this.table.getColumn(column).getName());
		return this;
	}

	public QueryBuilder<TableType> append(
			TableType column,
			String query) {
		this.append(column);
		this.append(query);
		return this;
	}

	public QueryBuilder<TableType> append(
			TableType column,
			String query,
			Iterable<Object> parameters) {
		this.append(column);
		this.append(query);
		this.append(parameters);
		return this;
	}

	public QueryBuilder<TableType> append(
			TableType column,
			String query,
			Object... parameters) {
		this.append(column);
		this.append(query);
		this.append(parameters);
		return this;
	}

	public QueryBuilder<TableType> appendTableName() {
		this.appendIdentifier(this.table.getName());
		return this;
	}

	public QueryBuilder<TableType> appendQualifiedTableName() {
		String schema;

		schema = this.table.getSchema();
		if (schema != null) this.appendIdentifier(schema).append('.');
		return this.appendTableName();
	}

	public QueryBuilder<TableType> dump(PrintStream stream) {
		stream.println(this.toString());
		for (Object parameter : this.parameters) {
			stream.print(parameter);
			stream.print(", ");
		}
		stream.println("(EOL)");
		return this;
	}

	public QueryExecutor execute() {
		return new QueryExecutor(
				this.database,
				this.toString(),
				this.getParameters());
	}

}

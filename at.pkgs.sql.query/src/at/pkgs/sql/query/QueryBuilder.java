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
import java.util.Arrays;
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
		StringBuilder builder;

		builder = new StringBuilder(this.builder);
		builder.append('\n');
		for (Object parameter : parameters)
			builder.append(parameter).append(", ");
		builder.append("(end of parameters)");
		return builder.toString();
	}

	String getQuery() {
		return this.builder.toString();
	}

	List<Object> getParameters() {
		return this.parameters;
	}

	boolean isColumn(Object column) {
		return this.table.getType().isAssignableFrom(column.getClass());
	}

	public QueryBuilder<TableType> identifier(
			String name) {
		this.dialect.appendIdentifier(this.builder, name);
		return this;
	}

	public QueryBuilder<TableType> parameter(
			Object parameter) {
		this.parameters.add(parameter);
		return this;
	}

	public QueryBuilder<TableType> parameters(
			Iterable<Object> parameters) {
		for (Object parameter : parameters)
			this.parameters.add(parameter);
		return this;
	}

	public QueryBuilder<TableType> parameters(
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
			Object parameter) {
		if (query != null) {
			this.append(query);
			this.parameter(parameter);
		}
		else {
			this.append('?');
			this.parameter(parameter);
		}
		return this;
	}

	public QueryBuilder<TableType> append(
			String query,
			Iterable<Object> parameters) {
		if (query != null) {
			this.append(query);
			this.parameters(parameters);
		}
		else {
			boolean first;

			first = true;
			for (Object parameter : parameters) {
				if (first) first = false;
				else this.append(", ");
				this.append('?');
				this.parameter(parameter);
			}
		}
		return this;
	}

	public QueryBuilder<TableType> append(
			String query,
			Object... parameters) {
		if (query != null) {
			this.append(query);
			this.parameters(parameters);
		}
		else {
			boolean first;

			first = true;
			for (Object parameter : parameters) {
				if (first) first = false;
				else this.append(", ");
				this.append('?');
				this.parameter(parameter);
			}
		}
		return this;
	}

	public QueryBuilder<TableType> column(
			TableType column) {
		this.identifier(this.table.getColumn(column).getName());
		return this;
	}

	public QueryBuilder<TableType> column(
			TableType column,
			String query) {
		this.column(column);
		this.append(query);
		return this;
	}

	public QueryBuilder<TableType> column(
			TableType column,
			String query,
			Object parameter) {
		this.column(column);
		this.append(query);
		this.parameter(parameter);
		return this;
	}

	public QueryBuilder<TableType> column(
			TableType column,
			String query,
			Iterable<Object> parameters) {
		this.column(column);
		this.append(query);
		this.parameters(parameters);
		return this;
	}

	public QueryBuilder<TableType> column(
			TableType column,
			String query,
			Object... parameters) {
		this.column(column);
		this.append(query);
		this.parameters(parameters);
		return this;
	}

	public QueryBuilder<TableType> columns(
			Iterable<TableType> columns) {
		boolean first;

		first = true;
		for (TableType column : columns) {
			if (first) first = false;
			else this.append(", ");
			this.column(column);
		}
		return this;
	}

	public QueryBuilder<TableType> columns(
			TableType... columns) {
		boolean first;

		first = true;
		for (TableType column : columns) {
			if (first) first = false;
			else this.append(", ");
			this.column(column);
		}
		return this;
	}

	public QueryBuilder<TableType> tableName() {
		this.identifier(this.table.getName());
		return this;
	}

	public QueryBuilder<TableType> qualifiedTableName() {
		String schema;

		schema = this.table.getSchema();
		if (schema != null) this.identifier(schema).append('.');
		return this.tableName();
	}

	public QueryBuilder<TableType> defaultValue(TableType column) {
		this.dialect.defaultValue(this, column);
		return this;
	}

	public QueryBuilder<TableType> currentTimestamp() {
		this.dialect.currentTimestamp(this.builder);
		return this;
	}

	public QueryBuilder<TableType> currentDate() {
		this.dialect.currentDate(this.builder);
		return this;
	}

	public QueryBuilder<TableType> currentTime() {
		this.dialect.currentTime(this.builder);
		return this;
	}

	public QueryBuilder<TableType> dumpIf(
			boolean enabled,
			Database.DumpCollector sink) {
		if (!enabled) return this;
		sink.collect(this.toString());
		return this;
	}

	public QueryExecutor execute() {
		return new QueryExecutor(
				this.database,
				this.getQuery(),
				this.getParameters());
	}

	public <ModelType> ModelExecutor<TableType, ModelType> execute(
			Class<ModelType> model,
			Iterable<TableType> columns) {
		return this.execute().withModel(
				table.getType(),
				model,
				columns);
	}

	public <ModelType> ModelExecutor<TableType, ModelType> execute(
			Class<ModelType> model,
			TableType... columns) {
		return this.execute(model, Arrays.asList(columns));
	}

}

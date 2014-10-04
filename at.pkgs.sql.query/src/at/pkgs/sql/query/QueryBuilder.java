package at.pkgs.sql.query;

import java.util.List;
import java.util.ArrayList;
import at.pkgs.sql.query.dialect.Dialect;

final class QueryBuilder<TableType> {

	private final Dialect dialect;

	private final TableDefinition<TableType> table;

	private final StringBuilder builder;

	private final List<Object> parameters;

	QueryBuilder(Dialect dialect, TableDefinition<TableType> table) {
		this.dialect = dialect;
		this.table = table;
		this.builder = new StringBuilder();
		this.parameters = new ArrayList<Object>();
	}

	@Override
	public String toString() {
		return this.builder.toString();
	}

	QueryBuilder<TableType> append(
			Iterable<Object> parameters) {
		for (Object parameter : parameters)
			this.parameters.add(parameter);
		return this;
	}

	QueryBuilder<TableType> append(
			Object... parameters) {
		for (Object parameter : parameters)
			this.parameters.add(parameter);
		return this;
	}

	QueryBuilder<TableType> append(
			char value) {
		this.builder.append(value);
		return this;
	}

	QueryBuilder<TableType> append(
			String value) {
		this.builder.append(value);
		return this;
	}

	QueryBuilder<TableType> append(
			String query,
			Iterable<Object> parameters) {
		this.append(query);
		this.append(parameters);
		return this;
	}

	QueryBuilder<TableType> append(
			String query,
			Object... parameters) {
		this.append(query);
		this.append(parameters);
		return this;
	}

	QueryBuilder<TableType> append(
			TableType column) {
		this.dialect.appendIdentifier(
				this.builder,
				this.table.getColumn(column).getName());
		return this;
	}

	QueryBuilder<TableType> append(
			TableType column,
			String query) {
		this.append(column);
		this.append(query);
		return this;
	}

	QueryBuilder<TableType> append(
			TableType column,
			String query,
			Iterable<Object> parameters) {
		this.append(column);
		this.append(query);
		this.append(parameters);
		return this;
	}

	QueryBuilder<TableType> append(
			TableType column,
			String query,
			Object... parameters) {
		this.append(column);
		this.append(query);
		this.append(parameters);
		return this;
	}

}

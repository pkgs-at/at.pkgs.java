package at.pkgs.sql.query;

import java.util.Arrays;
import java.sql.Connection;
import java.sql.ResultSet;

public class QueryExecutor {

	private final Database database;

	private String query;

	private Iterable<Object> parameters;

	QueryExecutor(
			Database database,
			String query,
			Iterable<Object> parameters) {
		this.database = database;
		this.query = query;
		this.parameters = parameters;
	}

	public int asAffectedRows(Connection connection) {
		return this.database.executeAffectedRows(
				connection,
				this.query,
				this.parameters);
	}

	public int asAffectedRows() {
		return this.asAffectedRows(null);
	}

	public ResultSet asResultSet(Connection connection) {
		return this.database.executeResultSet(
				connection,
				this.query,
				this.parameters);
	}

	public ResultSet asResultSet() {
		return this.asResultSet(null);
	}

	public <TableType extends Enum<?>, ModelType>
	ModelExecutor<TableType, ModelType> withModel(
			Class<TableType> table,
			Class<ModelType> model,
			Iterable<TableType> columns) {
		return new ModelExecutor<TableType, ModelType> (
				this.database,
				table,
				model,
				columns,
				this.query,
				this.parameters);
	}

	public <TableType extends Enum<?>, ModelType>
	ModelExecutor<TableType, ModelType> withModel(
			Class<TableType> table,
			Class<ModelType> model,
			TableType... columns) {
		return this.withModel(
				table,
				model,
				Arrays.asList(columns));
	}

}

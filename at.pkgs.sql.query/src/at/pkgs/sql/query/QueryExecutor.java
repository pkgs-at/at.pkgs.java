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

	public <ResultType> ResultType asScalar(
			Connection connection,
			Class<ResultType> type) {
		return this.database.executeScalar(
				connection,
				type,
				this.query,
				this.parameters);
	}

	public <ResultType> ResultType asScalar(
			Class<ResultType> type) {
		return this.asScalar(
				null,
				type);
	}

	public <TableType extends Enum<?>>
	TableExecutor<TableType> withTable(
			Class<TableType> table,
			Iterable<TableType> columns) {
		return new TableExecutor<TableType> (
				this.database,
				table,
				columns,
				this.query,
				this.parameters);
	}

	public <TableType extends Enum<?>>
	TableExecutor<TableType> withTable(
			Class<TableType> table,
			TableType... columns) {
		return this.withTable(
				table,
				Arrays.asList(columns));
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

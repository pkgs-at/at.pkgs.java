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
import java.sql.Connection;
import java.sql.ResultSet;

public class ModelExecutor<TableType extends Enum<?>, ModelType> {

	private final Database database;

	private final Class<TableType> table;

	private final Class<ModelType> model;

	private final Iterable<TableType> columns;

	private final String query;

	private final Iterable<Object> parameters;

	ModelExecutor(
			Database database,
			Class<TableType> table,
			Class<ModelType> model,
			Iterable<TableType> columns,
			String query,
			Iterable<Object> parameters) {
		this.database = database;
		this.table = table;
		this.model = model;
		this.columns = columns;
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

	public List<List<Object>> asGeneratedKeys(Connection connection) {
		return this.database.executeGeneratedKeys(
				connection,
				this.query,
				this.parameters,
				this.table,
				this.columns);
	}

	public List<List<Object>> asGeneratedKeys() {
		return this.asGeneratedKeys(null);
	}

	public ResultSet asResultSet(Connection connection) {
		return this.database.executeResultSet(
				connection,
				this.query,
				this.parameters);
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

	public ModelType asModel(Connection connection, ModelType model) {
		return this.database.executeModel(
				connection,
				this.table,
				this.model,
				this.columns,
				model,
				this.query,
				this.parameters);
	}

	public ModelType asModel(Connection connection) {
		return this.asModel(connection, null);
	}

	public ModelType asModel(ModelType model) {
		return this.asModel(null, model);
	}

	public ModelType asModel() {
		return this.asModel(null, null);
	}

	public List<ModelType> asModelList(Connection connection) {
		return this.database.executeModelList(
				connection,
				this.table,
				this.model,
				this.columns,
				this.query,
				this.parameters);
	}

	public List<ModelType> asModelList() {
		return this.asModelList(null);
	}

}

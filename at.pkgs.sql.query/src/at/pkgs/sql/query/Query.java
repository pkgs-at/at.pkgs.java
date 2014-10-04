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

public class Query<TableType extends Enum<?>, ModelType>
extends AbstractQuery<TableType, ModelType> {

	private final Database database;

	private final Class<TableType> table;

	private final Class<ModelType> model;

	Query(Database database, Class<TableType> table, Class<ModelType> model) {
		this.database = database;
		this.table = table;
		this.model = model;
	}

	@Override
	protected Database getDatabase() {
		return this.database;
	}

	@Override
	protected Class<TableType> getTableType() {
		return this.table;
	}

	@Override
	protected Class<ModelType> getModelType() {
		return this.model;
	}

}

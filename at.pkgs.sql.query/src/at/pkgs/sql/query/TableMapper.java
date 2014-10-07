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

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collections;

class TableMapper<TableType, ModelType> {

	private final TableDefinition<TableType> table;

	private final Class<ModelType> type;

	private final Map<TableType, ColumnMapper<TableType, ModelType>> columns;

	TableMapper(TableDefinition<TableType> table, Class<ModelType> type) {
		Map<TableType, ColumnMapper<TableType, ModelType>> columns;

		try {
			type.getConstructor();
		}
		catch (NoSuchMethodException throwable) {
			throw new Database.Exception(
					throwable,
					"model type without default constructor: %s",
					type.getName());
		}
		catch (SecurityException throwable) {
			throw new Database.Exception(throwable);
		}
		this.table = table;
		this.type = type;
		columns =
				new LinkedHashMap
				<TableType, ColumnMapper<TableType, ModelType>>();
		for (TableType column : this.table.getColumns().keySet()) {
			ColumnDefinition<TableType> definition;
			ColumnMapper<TableType, ModelType> mapper;

			definition = this.table.getColumn(column);
			mapper = new ColumnMapper<TableType, ModelType>(this, definition);
			if (!mapper.hasField()) continue;
			columns.put(column, mapper);
			
		}
		this.columns = Collections.unmodifiableMap(columns);
	}

	Class<ModelType> getType() {
		return this.type;
	}

	Map<TableType, ColumnMapper<TableType, ModelType>> getColumns() {
		return this.columns;
	}

	ColumnMapper<TableType, ModelType> getColumn(TableType column) {
		if (!this.columns.containsKey(column))
			throw new Database.Exception(
					"no mapping for %s in %s",
					column,
					this.type.getName());
		return this.columns.get(column);
	}

	ModelType setValues(
			ModelType model,
			Iterable<TableType> columns,
			Iterable<Object> values) {
		Iterator<TableType> columnIterator;
		Iterator<Object> valueIterator;

		if (model == null) {
			try {
				model = this.type.newInstance();
			}
			catch (Exception throwable) {
				throw new Database.Exception(throwable);
			}
		}
		if (columns == null) {
			columns = this.table.getColumns().keySet();
		}
		columnIterator = columns.iterator();
		valueIterator = values.iterator();
		while (columnIterator.hasNext() && valueIterator.hasNext())
			this.getColumn(columnIterator.next())
					.setValue(model, valueIterator.next());
		return model;
	}

	List<Object> getValues(
			ModelType model,
			Iterable<TableType> columns,
			List<Object> values) {
		if (columns == null) {
			columns = this.table.getColumns().keySet();
		}
		if (values == null) {
			values = new ArrayList<Object>();
		}
		for (TableType column : columns)
			values.add(this.getColumn(column).getValue(model));
		return values;
	}

}

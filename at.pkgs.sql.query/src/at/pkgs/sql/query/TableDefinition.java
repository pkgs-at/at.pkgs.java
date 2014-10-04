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

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

class TableDefinition<TableType> {

	private final Database.Table annotation;

	private final Map<TableType, ColumnDefinition<TableType>> columns;

	private final ConcurrentMap<Class<?>, TableMapper<TableType, ?>> mappers;

	TableDefinition(Class<TableType> type) {
		Map<TableType, ColumnDefinition<TableType>> columns;

		this.annotation = type.getAnnotation(Database.Table.class);
		if (this.annotation == null)
			throw new Database.Exception(
					"table without Database.Table annotation: %s",
					type.getName());
		columns = new LinkedHashMap<TableType, ColumnDefinition<TableType>>();
		for (TableType key : type.getEnumConstants())
			columns.put(key, new ColumnDefinition<TableType>(this, key));
		this.columns = Collections.unmodifiableMap(columns);
		this.mappers =
				new ConcurrentHashMap<Class<?>, TableMapper<TableType, ?>>();
	}

	String getSchema() {
		String value;

		value = this.annotation.schema();
		return (value == null || value.length() <= 0) ? null : value;
	}

	String getName() {
		return this.annotation.name();
	}

	Map<TableType, ColumnDefinition<TableType>> getColumns() {
		return this.columns;
	}

	ColumnDefinition<TableType> getColumn(
			TableType column) {
		return this.columns.get(column);
	}

	@SuppressWarnings("unchecked")
	<ModelType> TableMapper<TableType, ModelType> getMapper(
			Class<ModelType> type) {
		if (!this.mappers.containsKey(type))
			this.mappers.putIfAbsent(
					type,
					new TableMapper<TableType, ModelType>(this, type));
		return (TableMapper<TableType, ModelType>)this.mappers.get(type);
	}

}

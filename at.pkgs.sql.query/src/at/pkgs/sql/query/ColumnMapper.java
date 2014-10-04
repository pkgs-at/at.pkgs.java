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

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Time;

class ColumnMapper<TableType, ModelType> {

	private final TableMapper<TableType, ModelType> table;

	private ColumnDefinition<TableType> column;

	private final Field field;

	private final Class<?> type;

	private final Database.Null nullValue;

	private final Map<Object, Object> map;

	ColumnMapper(
			TableMapper<TableType, ModelType> table,
			ColumnDefinition<TableType> column) {
		Class<?> type;
		Field field;
		String name;

		this.table = table;
		this.column = column;
		name = this.column.getFieldName().toUpperCase();
		field = null;
		for (Field candidate : this.table.getType().getFields()) {
			if (!candidate.getName().toUpperCase().equals(name)) continue;
			field  = candidate;
			break;
		}
		if (field == null) {
			this.field = null;
			this.type = null;
			this.nullValue = null;
			this.map = null;
			return;
		}
		if (!field.isAccessible()) field.setAccessible(true);
		this.field = field;
		type = this.field.getType();
		if (Enum.class.isAssignableFrom(type)) {
			Map<Object, Object> map;

			map = new HashMap<Object, Object>();
			if (!Database.EnumValue.class.isAssignableFrom(type)) {
				Enum<?>[] values;

				this.type = String.class;
				values = (Enum<?>[])type.getEnumConstants();
				for (Enum<?> value : values) {
					map.put(value, value.name());
					map.put(value.name(), value);
				}
			}
			else {
				Database.EnumValue<?>[] values;

				values = (Database.EnumValue<?>[])type.getEnumConstants();
				if (values.length < 1)
					this.type = String.class;
				else
					this.type = values[0].getDatabaseType();
				for (Database.EnumValue<?> value : values) {
					Object mapped;

					mapped = value.getDatabaseValue();
					map.put(value, mapped);
					if (mapped != null) map.put(mapped, value);
				}
			}
			this.map = Collections.unmodifiableMap(map);
		}
		else {
			this.type = this.field.getType();
			this.map = null;
		}
		this.nullValue = this.detectNullValue();
	}

	private Database.Null detectNullValue() {
		if (this.type == BigDecimal.class)
			return Database.Null.BigDecimal;
		if (this.type == Boolean.class || this.type == boolean.class)
			return Database.Null.Boolean;
		if (this.type == Byte.class || this.type == byte.class)
			return Database.Null.Byte;
		if (this.type == Byte[].class || this.type == byte[].class)
			return Database.Null.Bytes;
		if (this.type == Date.class)
			return Database.Null.Date;
		if (this.type == Double.class || this.type == double.class)
			return Database.Null.Double;
		if (this.type == Float.class || this.type == float.class)
			return Database.Null.Float;
		if (this.type == Integer.class || this.type == int.class)
			return Database.Null.Integer;
		if (this.type == Long.class || this.type == long.class)
			return Database.Null.Long;
		if (this.type == String.class)
			return Database.Null.String;
		if (this.type == Time.class)
			return Database.Null.Time;
		if (this.type == Timestamp.class)
			return Database.Null.Timestamp;
		return null;
	}

	boolean hasField() {
		return this.field != null;
	}

	void setValue(ModelType model, Object value) {
		if (!this.hasField())
			throw new Database.Exception(
					"no mapping for %s in %s",
					this.column.getFieldName(),
					this.table.getType().getName());
		if (value instanceof Database.Null) value = null;
		if (value != null && this.map != null) value = this.map.get(value);
		try {
			this.field.set(model, value);
		}
		catch (Exception throwable) {
			throw new Database.Exception(
					throwable,
					"cannot set field value: %s.%s",
					this.field.getDeclaringClass().getName(),
					this.field.getName());
		}
	}

	Object getValue(ModelType model) {
		Object value;

		if (!this.hasField())
			throw new Database.Exception(
					"no mapping for %s in %s",
					this.column.getFieldName(),
					this.table.getType().getName());
		try {
			value = this.field.get(model);
		}
		catch (Exception throwable) {
			throw new Database.Exception(
					throwable,
					"cannot get field value: %s.%s",
					this.field.getDeclaringClass().getName(),
					this.field.getName());
		}
		if (value != null && this.map != null) value = this.map.get(value);
		return value == null ? this.nullValue : value;
	}

}

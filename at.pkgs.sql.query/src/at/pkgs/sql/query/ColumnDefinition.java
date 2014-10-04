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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

class ColumnDefinition<TableType> {

	private final Enum<?> entry;

	private final Database.Column annotation;

	ColumnDefinition(
			TableDefinition<TableType> table,
			TableType column) {
		Class<?> type;
		Field field;
		Database.Column annotation;

		this.entry = (Enum<?>)column;
		type = this.entry.getDeclaringClass();
		try {
			field = type.getDeclaredField(this.entry.name());
		}
		catch (Exception throwable) {
			throw new Database.Exception(throwable);
		}
		annotation = field.getAnnotation(Database.Column.class);
		if (annotation == null) annotation = new Database.Column() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return this.getClass();
			}

			@Override
			public String name() {
				return ColumnDefinition.this.getFieldName();
			}

		};
		this.annotation = annotation;
	}

	String getName() {
		return this.annotation.name();
	}

	String getFieldName() {
		return this.entry.name();
	}

}

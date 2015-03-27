/*
 * Copyright (c) 2009-2015, Architector Inc., Japan
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

package at.pkgs.sql.trifle;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Model<ColumnType extends Model.Column> {

	public static interface Column {

		public String column();

	}

	public static abstract class Via {

		private List<Column> columns;

		private Constructor<?> constructor;

		private List<Column> columns() {
			List<Column> columns;
			Class<?> model;

			columns = new ArrayList<Column>();
			model = this.getClass().getEnclosingClass();
			for (Class<?> content : model.getDeclaredClasses()) {
				if (!content.isEnum()) continue;
				if (!Column.class.isAssignableFrom(content)) continue;
				if (content.getDeclaringClass() != model) continue;
				for (Object constant : content.getEnumConstants())
					columns.add((Column)constant);
			}
			return columns;
		}

		@SuppressWarnings("unchecked")
		public <ColumnType extends Column> List<ColumnType> getColumns() {
			if (this.columns == null) {
				synchronized (this) {
					if (this.columns == null)
						this.columns = this.columns();
				}
			}
			return (List<ColumnType>)this.columns;
		}

		private Constructor<?> constructor() {
			try {
				return this.getClass()
						.getEnclosingClass()
						.getConstructor();
			}
			catch (Exception cause) {
				throw new RuntimeException(cause);
			}
		}

		@SuppressWarnings("unchecked")
		public <ModelType> Constructor<ModelType> getConstructor() {
			if (this.constructor == null) {
				synchronized (this) {
					if (this.constructor == null)
						this.constructor = this.constructor();
				}
			}
			return (Constructor<ModelType>)this.constructor;
		}

		protected <ModelType> ModelType model() {
			try {
				return this.<ModelType>getConstructor().newInstance();
			}
			catch (RuntimeException cause) {
				throw cause;
			}
			catch (Exception cause) {
				throw new RuntimeException(cause);
			}
		}

		@SuppressWarnings("unchecked")
		protected <ModelType> ModelType populate(
				List<Column> columns,
				Object... values) {
			Model<?> model;
			Map<Object, Object> map;

			model = this.model();
			map = (Map<Object, Object>)model.values;
			for (int index = 0; index < values.length; index ++)
				map.put(columns.get(index), values[index]);
			return (ModelType)model;
		}

		@SuppressWarnings("unchecked")
		protected <ModelType> List<ModelType> populate(
				List<Column> columns,
				ResultSet result)
						throws SQLException {
			List<ModelType> models;
			Object[] values;

			models = new ArrayList<ModelType>();
			values = new Object[result.getMetaData().getColumnCount()];
			while (result.next()) {
				for (int index = 0; index < values.length; index ++)
					values[index] = result.getObject(index + 1);
				models.add((ModelType)this.populate(columns, values));
			}
			return models;
		}

	}

	private final Map<ColumnType, Object> values;

	public Model() {
		this.values = new LinkedHashMap<ColumnType, Object>();
	}

	public Map<ColumnType, Object> getValues() {
		return Collections.unmodifiableMap(this.values);
	}

	@SuppressWarnings("unchecked")
	public <ValueType> ValueType get(ColumnType column) {
		return (ValueType)this.values.get(column);
	}

	protected void set(ColumnType column, Object value) {
		this.values.put(column, value);
	}

}

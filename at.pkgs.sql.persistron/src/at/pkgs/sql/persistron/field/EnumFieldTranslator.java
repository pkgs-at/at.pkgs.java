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

package at.pkgs.sql.persistron.field;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import at.pkgs.sql.persistron.Column;
import at.pkgs.sql.persistron.DatabaseException;

public class EnumFieldTranslator extends FieldTranslator {

	private Map<String, Enum<?>> map;

	@Override
	public void initialize(
			Field field,
			Enum<?> identifier,
			Column column) {
		Map<String, Enum<?>> map;

		super.initialize(field, identifier, column);
		map = new HashMap<String, Enum<?>>();
		for (Object constant : (field.getType()).getEnumConstants()) {
			Enum<?> value;

			value = (Enum<?>)constant;
			map.put(value.name(), value);
		}
		this.map = Collections.unmodifiableMap(map);
	}

	@Override
	public void set(
			PreparedStatement statement,
			int index,
			Object value) throws Exception {
		if (value == null)
			statement.setString(index + 1, null);
		else
			statement.setString(index + 1, ((Enum<?>)value).name());
	}

	@Override
	public void populate(
			Object target,
			ResultSet result,
			int index) throws Exception {
		String name;
		Enum<?> value;

		name = result.getString(index + 1);
		value = null;
		if (name != null) {
			value = this.map.get(name);
			if (value == null)
				throw new DatabaseException(
						"%s is not valid name for %s",
						name,
						this.field().getType().getName());
		}
		this.field().set(target, value);
	}

}

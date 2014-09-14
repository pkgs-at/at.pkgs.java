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

package at.pkgs.sql.persistron;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import at.pkgs.sql.persistron.AbstractDatabase;
import at.pkgs.sql.persistron.field.FieldTranslator;
import at.pkgs.sql.persistron.field.FieldTranslatorFactory;

public class Translator<EntityType> {

	private final Class<EntityType> type;

	private final Table table;

	protected final Enum<?>[] identifiers;

	protected final Enum<?>[] primaryKeys;

	protected final Enum<?>[] nonPrimaryKeys;

	protected final Enum<?>[] generatedKeys;

	protected final Enum<?>[] nonGeneratedKeys;

	protected final List<FieldTranslator> fields;

	protected final Map<Enum<?>, FieldTranslator> fieldByIdentifier;

	public Translator(
			AbstractDatabase database,
			Class<EntityType> type) {
		FieldTranslatorFactory factory;
		List<FieldTranslator> fields;
		List<Enum<?>> identifiers;
		List<Enum<?>> primaryKeys;
		List<Enum<?>> nonPrimaryKeys;
		List<Enum<?>> generatedKeys;
		List<Enum<?>> nonGeneratedKeys;
		Map<Enum<?>, FieldTranslator> fieldByIdentifier;

		this.type = type;
		this.table = this.type.getAnnotation(Table.class);
		if (this.table == null)
			throw new DatabaseException(
					"%s is not Table annotated",
					type.getName());
		factory = database.fieldTranslatorFactory();
		identifiers = new ArrayList<Enum<?>>();
		primaryKeys = new ArrayList<Enum<?>>();
		nonPrimaryKeys = new ArrayList<Enum<?>>();
		generatedKeys = new ArrayList<Enum<?>>();
		nonGeneratedKeys = new ArrayList<Enum<?>>();
		fields = new ArrayList<FieldTranslator>();
		fieldByIdentifier = new HashMap<Enum<?>, FieldTranslator>();
		for (Enum<?> identifier : this.table.fields().getEnumConstants()) {
			try {
				Field enumField;
				Field entityField;
				FieldTranslator field;
				Column column;
				PrimaryKey primaryKey;

				entityField =
						this.table.entity().getDeclaredField(
								identifier.name());
				enumField =
						this.table.fields().getDeclaredField(
								identifier.name());
				column = enumField.getAnnotation(Column.class);
				if (column == null)
					throw new DatabaseException(
							"%s not Column annotated",
							identifier.name());
				primaryKey = enumField.getAnnotation(PrimaryKey.class);
				field = factory.create(
						type,
						entityField,
						identifier,
						enumField.getAnnotation(Column.class));
				identifiers.add(identifier);
				if (primaryKey != null) {
					primaryKeys.add(identifier);
					if (primaryKey.generated()) {
						generatedKeys.add(identifier);
					}
					else {
						nonGeneratedKeys.add(identifier);
					}
				}
				else {
					nonPrimaryKeys.add(identifier);
					nonGeneratedKeys.add(identifier);
				}
				fields.add(field);
				fieldByIdentifier.put(identifier, field);
			}
			catch (Exception throwable) {
				throw new DatabaseException(
						throwable,
						"%s.%s is not accesable",
						type.getName(),
						identifier.name());
			}
		}
		this.identifiers = identifiers.toArray(new Enum<?>[0]);
		this.primaryKeys = primaryKeys.toArray(new Enum<?>[0]);
		this.nonPrimaryKeys = nonPrimaryKeys.toArray(new Enum<?>[0]);
		this.generatedKeys = generatedKeys.toArray(new Enum<?>[0]);
		this.nonGeneratedKeys = nonGeneratedKeys.toArray(new Enum<?>[0]);
		this.fields = Collections.unmodifiableList(fields);
		this.fieldByIdentifier =
				Collections.unmodifiableMap(fieldByIdentifier);
	}

	public Class<EntityType> type() {
		return this.type;
	}

	public Table table() {
		return this.table;
	}

	public Enum<?>[] identifiers() {
		return Arrays.copyOf(
				this.identifiers,
				this.identifiers.length);
	}

	public boolean hasPrimaryKeys() {
		return this.primaryKeys.length > 0;
	}

	public Enum<?>[] primaryKeys() {
		return Arrays.copyOf(
				this.primaryKeys,
				this.primaryKeys.length);
	}

	public Enum<?>[] nonPrimaryKeys() {
		return Arrays.copyOf(
				this.nonPrimaryKeys,
				this.nonPrimaryKeys.length);
	}

	public boolean hasGeneratedKeys() {
		return this.generatedKeys.length > 0;
	}

	public Enum<?>[] generatedKeys() {
		return Arrays.copyOf(
				this.generatedKeys,
				this.generatedKeys.length);
	}

	public Enum<?>[] nonGeneratedKeys() {
		return Arrays.copyOf(
				this.nonGeneratedKeys,
				this.nonGeneratedKeys.length);
	}

	public List<FieldTranslator> fields() {
		return this.fields;
	}

	public FieldTranslator get(Enum<?> identifier) {
		FieldTranslator field;

		field = this.fieldByIdentifier.get(identifier);
		if (field == null)
			throw new DatabaseException(
					"%s is an invalid field",
					identifier.name());
		return field;
	}

	public void write(
			Enum<?>[] identifiers,
			PreparedStatement statement,
			int offset,
			EntityType entity) {
		try {
			for (int index = 0; index < identifiers.length; index ++) {
				FieldTranslator field;

				field = this.fieldByIdentifier.get(identifiers[index]);
				if (field == null)
					throw new DatabaseException(
							"%s is an invalid field",
							identifiers[index].name());
				field.set(statement, offset + index, field.get(entity));
			}
		}
		catch (DatabaseException throwable) {
			throw throwable;
		}
		catch (Exception throwable) {
			throw new DatabaseException(
					throwable,
					"cannot write PreparedStatement");
		}
	}

	public void read(
			Enum<?>[] fields,
			ResultSet result,
			EntityType entity) {
		try {
			for (int index = 0; index < fields.length; index ++) {
				try {
					this.get(fields[index]).populate(entity, result, index);
				}
				catch (Exception throwable) {
					try {
						throw new DatabaseException(
								throwable,
								"cannot read ResultSet at %d:%d",
								result.getRow() - 1, index);
					}
					catch (Exception ignored) {
						throw new DatabaseException(
								throwable,
								"cannot read ResultSet at unknown:%d",
								index);
					}
				}
			}
		}
		catch (DatabaseException throwable) {
			throw throwable;
		}
		catch (Exception throwable) {
			throw new DatabaseException(
					throwable,
					"cannot read ResultSet");
		}
	}

	public void readTo(
			Enum<?>[] fields,
			ResultSet result,
			EntityType entity) {
		try {
			if (!result.next())
				throw new DatabaseException(
						"no more rows in ResultSet");
		}
		catch (Exception throwable) {
			throw new DatabaseException(
					throwable,
					"cannot go next ResultSet");
		}
		this.read(fields, result, entity);
	}

	public EntityType readOne(Enum<?>[] fields, ResultSet result) {
		try {
			if (result.next()) {
				EntityType entity;

				entity = this.type.newInstance();
				this.read(fields, result, entity);
				return entity;
			}
			else {
				return null;
			}
		}
		catch (DatabaseException throwable) {
			throw throwable;
		}
		catch (Exception throwable) {
			throw new DatabaseException(
					throwable,
					"cannot access ResultSet");
		}
	}

	public List<EntityType> readAll(Enum<?>[] fields, ResultSet result) {
		try {
			List<EntityType> list;

			list = new ArrayList<EntityType>();
			while (result.next()) {
				EntityType entity;

				entity = this.type.newInstance();
				this.read(fields, result, entity);
				list.add(entity);
			}
			return list;
		}
		catch (DatabaseException throwable) {
			throw throwable;
		}
		catch (Exception throwable) {
			throw new DatabaseException(
					throwable,
					"cannot access ResultSet");
		}
	}

}

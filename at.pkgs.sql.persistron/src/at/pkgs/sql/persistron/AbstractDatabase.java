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

import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.sql.DataSource;
import at.pkgs.logging.Logger;
import at.pkgs.sql.persistron.dialect.Dialect;
import at.pkgs.sql.persistron.field.FieldTranslatorFactory;

public abstract class AbstractDatabase {

	private final Logger logger;

	private final ConcurrentMap<Class<?>, Translator<?>> translatorCache;

	private DataSource dataSource;

	private Dialect dialect;

	private FieldTranslatorFactory fieldTranslatorFactory;

	public AbstractDatabase() {
		this.logger = Logger.of(this);
		this.translatorCache =
				new ConcurrentHashMap<Class<?>, Translator<?>>();
	}

	protected void initialize(
			DataSource dataSource,
			Dialect dialect,
			FieldTranslatorFactory factory) {
		if (this.logger.debug()) this.logger.debug(
				"initialize with:\n\t%s\n\t%s\n\t%s",
				dataSource.getClass().getName(),
				dialect.getClass().getName(),
				factory.getClass().getName());
		this.dataSource = dataSource;
		this.dialect = dialect;
		this.fieldTranslatorFactory = factory;
	}

	public DataSource dataSource() {
		return this.dataSource;
	}

	public Dialect dialect() {
		return this.dialect;
	}

	public FieldTranslatorFactory fieldTranslatorFactory() {
		return this.fieldTranslatorFactory;
	}

	public <EntityType> Translator<EntityType> buildTranslator(
			Class<EntityType> type) {
		return new Translator<EntityType>(this, type);
	}

	@SuppressWarnings("unchecked")
	public <EntityType> Translator<EntityType> translator(
			Class<EntityType> type) {
		if (!this.translatorCache.containsKey(type))
			this.translatorCache.put(type, this.buildTranslator(type));
		return (Translator<EntityType>)this.translatorCache.get(type);
	}

	protected StatementRunner prepare(
			Class<?> type,
			String query,
			Object option) {
		Connection connection;
		PreparedStatement statement;

		connection = null;
		statement = null;
		try {
			if (this.logger.debug()) this.logger.debug(
					"prepare\n\t%s\n\t%s",
					type.getName(),
					query);
			connection = this.dataSource.getConnection();
			if (option == null) {
				statement =
						connection.prepareStatement(query);
			}
			else if (option instanceof Integer) {
				statement =
						connection.prepareStatement(query, (Integer)option);
			}
			else if (option instanceof String[]) {
				statement =
						connection.prepareStatement(query, (String[])option);
			}
			else if (option instanceof int[]) {
				int[] indexes;

				indexes = (int[])option;
				indexes = Arrays.copyOf(indexes, indexes.length);
				for (int index = 0; index < indexes.length; index ++)
					indexes[index] ++;
				statement = connection.prepareStatement(query, indexes);
			}
			return new StatementRunner(
					this.translator(type),
					connection,
					statement);
		}
		catch (Throwable throwable) {
			if (statement != null) {
				try {
					statement.close();
				}
				catch (Exception ignored) {
					this.logger.error(
							ignored,
							"ignored error at close statement");
				}
			}
			if (connection != null) {
				try {
					connection.close();
				}
				catch (Exception ignored) {
					this.logger.error(
							ignored,
							"ignored error at close connection");
				}
			}
			throw new DatabaseException(
					throwable,
					"failed on prepare statement: %s",
					query);
		}
	}

	public StatementRunner prepare(
			Class<?> type,
			String query) {
		return this.prepare(type, query, (Object)null);
	}

	public StatementRunner prepare(
			Class<?> type,
			String query,
			int option) {
		return this.prepare(type, query, (Object)option);
	}

	public StatementRunner prepare(
			Class<?> type,
			String query,
			String[] generatedKeyColumns) {
		return this.prepare(type, query, (Object)generatedKeyColumns);
	}

	public StatementRunner prepare(
			Class<?> type,
			String query,
			int[] generatedKeyIndexes) {
		return this.prepare(type, query, (Object)generatedKeyIndexes);
	}

	@SuppressWarnings("unchecked")
	public <
	DatabaseType extends AbstractDatabase,
	QueryType extends AbstractQuery<DatabaseType, ?, ?, ?>>
	QueryType query(
			AbstractQuery<DatabaseType, QueryType, ?, ?> query) {
		query.initialize((DatabaseType)this);
		query.bind();
		return (QueryType)query;
	}

	@SuppressWarnings("unchecked")
	public <EntityType> void insert(EntityType entity) {
		Translator<EntityType> translator;

		translator = this.translator((Class<EntityType>)entity.getClass());
		if (translator.hasGeneratedKeys()) {
			this.dialect.insertWithGeneratedKeys(this, translator, entity);
		}
		else {
			StatementRunner runner;

			runner = null;
			try {
				Enum<?>[] fields;
				QueryBuilder builder;
				int affected;

				fields = translator.identifiers();
				builder = new QueryBuilder(translator, this.dialect());
				builder.append("INSERT INTO ");
				builder.identifier(translator.table().name());
				builder.append('(');
				builder.fields(fields);
				builder.append(") VALUES(");
				for (int index = 0; index < fields.length; index ++) {
					if (index > 0) builder.append(", ");
					builder.append('?');
				}
				builder.append(')');
				runner = this.prepare(entity.getClass(), builder.toString());
				runner.bind(fields, entity);
				affected = runner.update();
				if (affected != 1) {
					throw new DatabaseException(
							"update affected %s rows",
							affected);
				}
			}
			finally {
				if (runner != null) runner.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public <EntityType> boolean update(Object entity) {
		StatementRunner runner;

		runner = null;
		try {
			Translator<EntityType> translator;
			Enum<?>[] primaryKeys;
			Enum<?>[] nonPrimaryKeys;
			QueryBuilder builder;
			int affected;

			translator = this.translator((Class<EntityType>)entity.getClass());
			primaryKeys = translator.primaryKeys;
			nonPrimaryKeys = translator.nonPrimaryKeys;
			if (primaryKeys.length <= 0)
				throw new DatabaseException(
						"cannot update no primary key table");
			builder = new QueryBuilder(translator, this.dialect());
			builder.append("UPDATE ");
			builder.identifier(translator.table().name());
			builder.append(" SET ");
			for (int index = 0; index < nonPrimaryKeys.length; index ++) {
				if (index > 0) builder.append(", ");
				builder.field(nonPrimaryKeys[index]);
				builder.append(" = ?");
			}
			builder.append(" WHERE ");
			for (int index = 0; index < primaryKeys.length; index ++) {
				if (index > 0) builder.append(" AND ");
				builder.field(primaryKeys[index]);
				builder.append(" = ?");
			}
			runner = this.prepare(entity.getClass(), builder.toString());
			runner.bind(nonPrimaryKeys, entity);
			runner.bind(primaryKeys, entity);
			affected = runner.update();
			if (affected == 1) return true;
			if (affected == 0) return false;
			throw new DatabaseException("update affected %s rows", affected);
		}
		finally {
			if (runner != null) runner.close();
		}
	}

}

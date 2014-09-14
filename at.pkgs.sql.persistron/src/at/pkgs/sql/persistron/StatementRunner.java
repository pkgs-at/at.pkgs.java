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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import at.pkgs.sql.persistron.field.FieldTranslator;

public class StatementRunner {

	private Translator<?> translator;

	private Connection connection;

	private PreparedStatement statement;

	private int index;

	public StatementRunner(
			Translator<?> translator,
			Connection connection,
			PreparedStatement statement) {
		this.translator = translator;
		this.connection = connection;
		this.statement = statement;
		this.index = 0;
	}

	public StatementRunner bind(
			String value) {
		try {
			this.statement.setString(this.index ++, value);
		}
		catch (Exception throwable) {
			throw new DatabaseException(
					throwable,
					"faild on bind parameter %s",
					value);
		}
		return this;
	}

	public StatementRunner bind(
			Enum<?> field,
			Object... values) {
		FieldTranslator translator;

		translator = this.translator.get(field);
		for (Object value : values) {
			try {
				translator.set(this.statement, this.index ++, value);
			}
			catch (Exception throwable) {
				throw new DatabaseException(
						throwable,
						"faild on bind parameter %s: %s",
						field.name(),
						value);
			}
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	public <EntityType> StatementRunner bind(
			Enum<?>[] fields,
			EntityType entity) {
		((Translator<EntityType>)this.translator).write(
				fields,
				this.statement,
				this.index,
				entity);
		this.index += fields.length;
		return this;
	}

	public ResultSet query() {
		try {
			return this.statement.executeQuery();
		}
		catch (SQLException throwable) {
			throw new DatabaseException(
					throwable,
					"failed on query");
		}
	}

	public ResultSet queryGeneratedKeys() {
		try {
			this.statement.executeUpdate();
			return this.statement.getGeneratedKeys();
		}
		catch (SQLException throwable) {
			throw new DatabaseException(
					throwable,
					"failed on update and getGeneratedKeys");
		}
	}

	public int update() {
		try {
			return this.statement.executeUpdate();
		}
		catch (SQLException throwable) {
			throw new DatabaseException(
					throwable,
					"failed on update");
		}
	}

	public void close() {
		Exception exception;

		exception = null;
		try {
			this.statement.close();
		}
		catch (SQLException throwable) {
			exception = throwable;
		}
		try {
			this.connection.close();
		}
		catch (SQLException throwable) {
			exception = throwable;
		}
		if (exception != null)
			throw new DatabaseException(
					exception,
					"failed on closing StatementBuilder");
	}

}

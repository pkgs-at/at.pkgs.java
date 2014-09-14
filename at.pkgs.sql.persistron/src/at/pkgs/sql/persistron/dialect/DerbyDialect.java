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

package at.pkgs.sql.persistron.dialect;

import at.pkgs.sql.persistron.AbstractDatabase;
import at.pkgs.sql.persistron.QueryBuilder;
import at.pkgs.sql.persistron.StatementRunner;
import at.pkgs.sql.persistron.Translator;

public class DerbyDialect implements Dialect {

	public void identifier(StringBuilder builder, String name) {
		builder.append('"');
		builder.append(name);
		builder.append('"');
	}

	public void range(StringBuilder builder, long offset, long limit) {
		if (offset < 0) offset = 0;
		builder.append("OFFSET ");
		builder.append(offset);
		builder.append(" ROWS");
		if (limit > 0) {
			builder.append(" FETCH NEXT ");
			builder.append(limit);
			builder.append(" ROWS ONLY");
		}
	}

	@Override
	public <EntityType> void insertWithGeneratedKeys(
			AbstractDatabase database,
			Translator<EntityType> translator,
			EntityType entity) {
		StatementRunner runner;

		runner = null;
		try {
			Enum<?>[] generatedKeys;
			Enum<?>[] nonGeneratedKeys;
			QueryBuilder builder;

			generatedKeys = translator.generatedKeys();
			nonGeneratedKeys = translator.nonGeneratedKeys();
			builder = new QueryBuilder(translator, this);
			builder.append("INSERT INTO ");
			builder.identifier(translator.table().name());
			builder.append('(');
			builder.fields(nonGeneratedKeys);
			builder.append(") VALUES(");
			for (int index = 0; index < nonGeneratedKeys.length; index ++) {
				if (index > 0) builder.append(", ");
				builder.append('?');
			}
			builder.append(')');
			runner = database.prepare(
					entity.getClass(),
					builder.toString(),
					java.sql.Statement.RETURN_GENERATED_KEYS);
			runner.bind(nonGeneratedKeys, entity);
			translator.readTo(
					generatedKeys,
					runner.queryGeneratedKeys(),
					entity);
		}
		finally {
			if (runner != null) runner.close();
		}
	}

}

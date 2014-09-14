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

import java.util.List;
import java.util.ArrayList;
import at.pkgs.sql.persistron.criterion.Criteria;
import at.pkgs.sql.persistron.criterion.Criterion;

public abstract class AbstractQuery<
DatabaseType extends AbstractDatabase,
QueryType extends AbstractQuery<?, ?, ?, ?>,
EntityType,
FieldType extends Enum<?>> {

	private DatabaseType database;

	private FieldType[] fields;

	private Criteria filter;

	private List<Sort> sort;

	private long offset;

	private long limit;

	public void initialize(DatabaseType database) {
		this.database = database;
		this.fields = null;
		this.filter = new Criteria(Criteria.Type.AND);
		this.sort = new ArrayList<Sort>();
		this.offset = 0;
		this.limit = 0;
	}

	public DatabaseType database() {
		return this.database;
	}

	@SuppressWarnings("unchecked")
	public QueryType field(FieldType... fields) {
		this.fields = fields;
		return (QueryType)this;
	}

	@SuppressWarnings("unchecked")
	public QueryType filter(Criterion... criterions) {
		this.filter.add(criterions);
		return (QueryType)this;
	}

	@SuppressWarnings("unchecked")
	public QueryType sort(Sort... sorts) {
		for (Sort sort : sorts)
			this.sort.add(sort);
		return (QueryType)this;
	}

	@SuppressWarnings("unchecked")
	public QueryType offset(long offset) {
		this.offset = offset;
		return (QueryType)this;
	}

	@SuppressWarnings("unchecked")
	public QueryType limit(long limit) {
		this.limit = limit;
		return (QueryType)this;
	}

	public Criteria and(Criterion... criterions) {
		Criteria criteria;

		criteria = new Criteria(Criteria.Type.AND);
		criteria.add(criterions);
		return criteria;
	}

	public Criteria or(Criterion... criterions) {
		Criteria criteria;

		criteria = new Criteria(Criteria.Type.OR);
		criteria.add(criterions);
		return criteria;
	}

	protected abstract Class<EntityType> type();

	protected abstract void bind();

	public EntityType findOne() {
		StatementRunner runner;

		runner = null;
		try {
			Class<EntityType> type;
			Translator<EntityType> translator;
			Enum<?>[] fields;
			QueryBuilder builder;

			type = this.type();
			translator = this.database.translator(type);
			fields = this.fields;
			if (fields == null) fields = translator.identifiers();
			builder = new QueryBuilder(translator, this.database.dialect());
			builder.append("SELECT ALL ");
			builder.fields(fields);
			builder.append(" FROM ");
			builder.identifier(translator.table().name());
			if (this.filter.size() > 0) {
				builder.append(" WHERE ");
				this.filter.build(builder);
			}
			builder.append(' ');
			builder.range(0, 1);
			runner = this.database.prepare(type, builder.toString());
			this.filter.bind(runner);
			return translator.readOne(fields, runner.query());
		}
		finally {
			if (runner != null) runner.close();
		}
	}

	public List<EntityType> findAll() {
		StatementRunner runner;

		runner = null;
		try {
			Class<EntityType> type;
			Translator<EntityType> translator;
			Enum<?>[] fields;
			QueryBuilder builder;

			type = this.type();
			translator = this.database.translator(type);
			fields = this.fields;
			if (fields == null) fields = translator.identifiers();
			builder = new QueryBuilder(translator, this.database.dialect());
			builder.append("SELECT ALL ");
			builder.fields(fields);
			builder.append(" FROM ");
			builder.identifier(translator.table().name());
			if (this.filter.size() > 0) {
				builder.append(" WHERE ");
				this.filter.build(builder);
			}
			if (this.sort.size() > 0) {
				builder.append(" ORDER BY ");
				for (int index = 0; index < this.sort.size(); index ++) {
					if (index > 0) builder.append(", ");
					this.sort.get(index).build(builder);
				}
			}
			if (this.offset > 0 || this.limit > 0) {
				builder.append(' ');
				builder.range(this.offset, this.limit);
			}
			runner =  this.database.prepare(type, builder.toString());
			this.filter.bind(runner);
			return translator.readAll(fields, runner.query());
		}
		finally {
			if (runner != null) runner.close();
		}
	}

	public long update(EntityType entity) {
		StatementRunner runner;

		runner = null;
		try {
			Class<EntityType> type;
			Translator<EntityType> translator;
			Enum<?>[] fields;
			QueryBuilder builder;

			type = this.type();
			translator = this.database.translator(type);
			fields = this.fields;
			if (fields == null) fields = translator.identifiers();
			builder = new QueryBuilder(translator, this.database.dialect());
			builder.append("UPDATE ");
			builder.identifier(translator.table().name());
			builder.append(" SET ");
			for (int index = 0; index < fields.length; index ++) {
				if (index > 0) builder.append(", ");
				builder.field(fields[index]);
				builder.append(" = ?");
			}
			if (this.filter.size() > 0) {
				builder.append(" WHERE ");
				this.filter.build(builder);
			}
			runner = this.database.prepare(type, builder.toString());
			this.filter.bind(runner);
			return runner.update();
		}
		finally {
			if (runner != null) runner.close();
		}
	}

	public long delete() {
		StatementRunner runner;

		runner = null;
		try {
			Class<EntityType> type;
			Translator<EntityType> translator;
			QueryBuilder builder;

			type = this.type();
			translator = this.database.translator(type);
			builder = new QueryBuilder(translator, this.database.dialect());
			builder.append("DELETE FROM ");
			builder.identifier(translator.table().name());
			if (this.filter.size() > 0) {
				builder.append(" WHERE ");
				this.filter.build(builder);
			}
			runner = this.database.prepare(type, builder.toString());
			this.filter.bind(runner);
			return runner.update();
		}
		finally {
			if (runner != null) runner.close();
		}
	}

	/*

for (Property property : database.query(new PropertyQuery() {

	@Override
	protected void build() {
		filter(
			field.name,
			field.value);
		filter(by.module.in("a", "b", "c"));
		filter(or(
			by.xxx.isNull(),
			and(
				by.xxx.greaterThan(xxx)
				by.xxx.lessThan(yyy))));
		sort(by.name.ascending);
		sort(by.createdAt.descending);
		offset(10);
		limit(100);
	}

}).findAll()) {
	property.getName();
	...
}

	 */

}

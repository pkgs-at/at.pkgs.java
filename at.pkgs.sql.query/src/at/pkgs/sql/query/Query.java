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

import java.util.List;
import java.util.Arrays;
import java.sql.Connection;
import at.pkgs.sql.query.dialect.Dialect;

public abstract class Query<TableType extends Enum<?>, ModelType>
implements Criterion.Parent<TableType, ModelType> {

	protected abstract class Expression
	implements Database.Expression<TableType> {

		// nothing

	}

	protected abstract class Values
	extends Database.Values<TableType> {

		// nothing

	}

	protected abstract class And
	extends Database.And<TableType, ModelType> {

		// nothing

	}

	protected abstract class Or
	extends Database.Or<TableType, ModelType> {

		// nothing

	}

	protected abstract class OrderBy
	extends Database.OrderBy<TableType> {

		// nothing

	}

	private final Class<TableType> table;

	private final Class<ModelType> model;

	private boolean distinct;

	private Iterable<TableType> columns;

	private Database.Values<TableType> values;

	private Criterion<TableType, ModelType> where;

	private Database.OrderBy<TableType> order;

	private int offset;

	private int limit;

	private Database database;

	protected Query(Class<TableType> table, Class<ModelType> model) {
		this.table = table;
		this.model = model;
		this.distinct = false;
		this.columns = null;
		this.values = null;
		this.where = null;
		this.order = null;
		this.offset = (-1);
		this.limit = (-1);
	}

	void prepare(Database database) {
		this.database = database;
	}

	protected Query<TableType, ModelType> distinct() {
		this.distinct = true;
		return this;
	}

	protected Query<TableType, ModelType> columns(
			Iterable<TableType> columns) {
		this.columns = columns;
		return this;
	}

	protected Query<TableType, ModelType> columns(
			TableType... columns) {
		return this.columns(Arrays.asList(columns));
	}

	public Query<TableType, ModelType> set(
			Database.Values<TableType> values) {
		this.values = values;
		return this;
	}

	public Query<TableType, ModelType> set(
			TableType column, Object value) {
		if (this.values == null) {
			this.values = new Database.Values<TableType>() {

				// nothing

			};
		}
		this.values.with(column, value);
		return this;
	}

	Query<TableType, ModelType> where(
			Criterion<TableType, ModelType> criterion) {
		this.where = criterion;
		return this;
	}

	public Query<TableType, ModelType> where(
			Criteria<TableType, ModelType> criterion) {
		return this.where((Criterion<TableType, ModelType>)criterion);
	}

	public at.pkgs.sql.query.Expression<TableType, ModelType>
	where(TableType column) {
		return new at.pkgs.sql.query.Expression<TableType, ModelType>(
				this,
				column);
	}

	public Query<TableType, ModelType> sort(
			Database.OrderBy<TableType> order) {
		this.order = order;
		return this;
	}

	public Query<TableType, ModelType> orderBy(
			boolean ascending, TableType... columns) {
		if (this.order == null) {
			this.order = new Database.OrderBy<TableType>() {

				// nothing

			};
		}
		this.order.with(ascending, columns);
		return this;
	}

	public Query<TableType, ModelType> orderByAscending(
			TableType... columns) {
		return this.orderBy(true, columns);
	}

	public Query<TableType, ModelType> orderByDescending(
			TableType... columns) {
		return this.orderBy(false, columns);
	}

	public Query<TableType, ModelType> limit(int value) {
		this.limit = value;
		return this;
	}

	public Query<TableType, ModelType> offset(int value) {
		this.offset = value;
		return this;
	}

	public Query<TableType, ModelType> offset(int offset, int limit) {
		this.offset = offset;
		this.limit = limit;
		return this;
	}

	public Query<TableType, ModelType> page(int size, int index) {
		this.offset = size * index;
		this.limit = size;
		return this;
	}

	QueryBuilder<TableType> buildSelectQuery() {
		QueryBuilder<TableType> builder;
		Dialect.SelectVisitor<TableType> visitor;

		if (this.columns == null) {
			TableMapper<TableType, ModelType> mapper;

			mapper = this.database.getTable(this.table).getMapper(this.model);
			this.columns = mapper.getColumns().keySet();
		}
		builder = this.database.newQueryBuilder(this.table);
		visitor = this.database.getDialect().newSelectVisitor();
		visitor.initialize(builder, this.offset, this.limit);
		if (!visitor.select())
			builder.append("SELECT");
		if (!visitor.allOrDistinct())
			builder.append(this.distinct ? " DISTINCT" : " ALL");
		if (!visitor.selectList())
			builder.append(' ').columns(this.columns);
		if (!visitor.from())
			builder.append(" FROM ").qualifiedTableName();
		if (!visitor.where() && this.where != null)
			this.where.build(builder, true);
		if (!visitor.orderBy() && this.order != null)
			this.order.build(builder);
		visitor.afterAll();
		return builder;
	}

	public Query<TableType, ModelType> dumpSelectIf(
			boolean enabled,
			Database.DumpCollector sink) {
		if (!enabled) return this;
		this.buildSelectQuery().dumpIf(true, sink);
		return this;
	}

	public List<ModelType> select(Connection connection) {
		return this.buildSelectQuery()
				.execute(
						this.model,
						this.columns)
				.asModelList(connection);
	}

	public List<ModelType> select() {
		return this.select(null);
	}

	QueryBuilder<TableType> buildSelectOneQuery() {
		int limit;

		limit = this.limit;
		try {
			return this.limit(1).buildSelectQuery();
		}
		finally {
			this.limit = limit;
		}
	}

	public Query<TableType, ModelType> dumpSelectOneIf(
			boolean enabled,
			Database.DumpCollector sink) {
		if (!enabled) return this;
		this.buildSelectOneQuery().dumpIf(true, sink);
		return this;
	}

	public ModelType selectOne(Connection connection) {
		return this.buildSelectOneQuery()
				.execute(
						this.model,
						this.columns)
				.asModel(connection);
	}

	public ModelType selectOne() {
		return this.selectOne(null);
	}

	QueryBuilder<TableType> buildCountQuery() {
		QueryBuilder<TableType> builder;
		Dialect.CountVisitor<TableType> visitor;

		builder = this.database.newQueryBuilder(this.table);
		visitor = this.database.getDialect().newCountVisitor();
		visitor.initialize(builder, this.offset, this.limit);
		if (!visitor.selectAll())
			builder.append("SELECT ALL");
		if (!visitor.count()) {
			builder.append(" COUNT(");
			if (this.columns == null) {
				builder.append('*');
			}
			else {
				if (!visitor.allOrDistinct())
					builder.append(this.distinct ? "DISTINCT" : "ALL");
				if (!visitor.columns())
					builder.append(' ').columns(this.columns);
			}
			builder.append(")");
		}
		if (!visitor.from())
			builder.append(" FROM ").qualifiedTableName();
		if (!visitor.where() && this.where != null)
			this.where.build(builder, true);
		visitor.afterAll();
		return builder;
	}

	public Query<TableType, ModelType> dumpCountIf(
			boolean enabled,
			Database.DumpCollector sink) {
		if (!enabled) return this;
		this.buildCountQuery().dumpIf(true, sink);
		return this;
	}

	public Long count(Connection connection) {
		Object value;

		value = this.buildCountQuery()
				.execute()
				.asScalar(connection, Object.class);
		if (value instanceof Integer)
			return ((Integer)value).longValue();
		else
			return (Long)value;
	}

	public Long count() {
		return this.count(null);
	}

	QueryBuilder<TableType> buildInsertQuery() {
		QueryBuilder<TableType> builder;
		Dialect.InsertVisitor<TableType> visitor;

		builder = this.database.newQueryBuilder(this.table);
		visitor = this.database.getDialect().newInsertVisitor();
		visitor.initialize(builder, this.columns);
		if (!visitor.insert())
			builder.append("INSERT");
		if (!visitor.into() && this.values != null)
			this.values.buildIntoClause(builder, visitor);
		if (!visitor.values() && this.values != null)
			this.values.buildValuesClause(builder, visitor);
		visitor.afterAll();
		return builder;
	}

	public Query<TableType, ModelType> dumpInsertIf(
			boolean enabled,
			Database.DumpCollector sink) {
		if (!enabled) return this;
		this.buildInsertQuery().dumpIf(true, sink);
		return this;
	}

	public int insert(Connection connection) {
		return this.buildInsertQuery()
				.execute()
				.asAffectedRows(connection);
	}

	public int insert() {
		return this.insert(null);
	}

	QueryBuilder<TableType> buildUpdateQuery() {
		QueryBuilder<TableType> builder;
		Dialect.UpdateVisitor<TableType> visitor;

		builder = this.database.newQueryBuilder(this.table);
		visitor = this.database.getDialect().newUpdateVisitor();
		visitor.initialize(builder, this.columns);
		if (!visitor.update())
			builder.append("UPDATE");
		if (!visitor.table())
			builder.append(' ').qualifiedTableName();
		if (!visitor.set() && this.values != null)
			this.values.buildSetClause(builder, visitor);
		if (!visitor.where() && this.where != null)
			this.where.build(builder, true);
		visitor.afterAll();
		return builder;
	}

	public Query<TableType, ModelType> dumpUpdateIf(
			boolean enabled,
			Database.DumpCollector sink) {
		if (!enabled) return this;
		this.buildUpdateQuery().dumpIf(true, sink);
		return this;
	}

	public int update(Connection connection) {
		return this.buildUpdateQuery()
				.execute()
				.asAffectedRows(connection);
	}

	public int update() {
		return this.update(null);
	}

	QueryBuilder<TableType> buildDeleteQuery() {
		QueryBuilder<TableType> builder;
		Dialect.DeleteVisitor<TableType> visitor;

		builder = this.database.newQueryBuilder(this.table);
		visitor = this.database.getDialect().newDeleteVisitor();
		visitor.initialize(builder);
		if (!visitor.delete())
			builder.append("DELETE");
		if (!visitor.from())
			builder.append(" FROM ").qualifiedTableName();
		if (!visitor.where() && this.where != null)
			this.where.build(builder, true);
		visitor.afterAll();
		return builder;
	}

	public Query<TableType, ModelType> dumpDeleteIf(
			boolean enabled,
			Database.DumpCollector sink) {
		if (!enabled) return this;
		this.buildDeleteQuery().dumpIf(true, sink);
		return this;
	}

	public int delete(Connection connection) {
		return this.buildDeleteQuery()
				.execute()
				.asAffectedRows(connection);
	}

	public int delete() {
		return this.delete(null);
	}

}

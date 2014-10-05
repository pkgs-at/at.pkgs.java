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

	Query<TableType, ModelType> where(
			Criterion<TableType, ModelType> criterion) {
		this.where = criterion;
		return this;
	}

	public Query<TableType, ModelType> where(
			Criteria<TableType, ModelType> criterion) {
		return this.where((Criterion<TableType, ModelType>)criterion);
	}

	public Expression<TableType, ModelType> where(TableType column) {
		return new Expression<TableType, ModelType>(this, column);
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

	void buildSelectListClause(QueryBuilder<TableType> builder) {
		boolean first;

		first = true;
		for (TableType colmun : this.columns) {
			if (first) first = false;
			else builder.append(',');
			builder.append(' ').append(colmun);
		}
	}

	void buildFromClause(QueryBuilder<TableType> builder) {
		builder.append(" FROM ").appendQualifiedTableName();
	}

	void buildWhereClause(QueryBuilder<TableType> builder) {
		if (this.where == null) return;
		this.where.build(builder, true);
	}

	void buildOrderByClause(QueryBuilder<TableType> builder) {
		if (this.order == null) return;
		this.order.build(builder);
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
			this.buildSelectListClause(builder);
		if (!visitor.from())
			this.buildFromClause(builder);
		if (!visitor.where())
			this.buildWhereClause(builder);
		if (!visitor.orderBy())
			this.buildOrderByClause(builder);
		visitor.afterAll();
		return builder;
	}

	public String buildSelectStatement() {
		return this.buildSelectQuery().getQuery();
	}

	public ModelType selectOne(Connection connection) {
		return this.limit(1).buildSelectQuery()
				.execute(
						this.model,
						this.columns)
				.asModel(connection);
	}

	public ModelType selectOne() {
		return this.selectOne(null);
	}

	public List<ModelType> selectAll(Connection connection) {
		return this.buildSelectQuery()
				.execute(
						this.model,
						this.columns)
				.asModelList(connection);
	}

	public List<ModelType> selectAll() {
		return this.selectAll(null);
	}

}

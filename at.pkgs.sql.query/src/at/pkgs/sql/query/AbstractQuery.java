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

public abstract class AbstractQuery<TableType extends Enum<?>, ModelType>
implements Criterion.Parent<TableType, ModelType> {

	private Criterion<TableType, ModelType> where;

	private Database.OrderBy<TableType> order;

	AbstractQuery() {
		// do nothing
	}

	protected abstract Database getDatabase();

	protected abstract Class<TableType> getTableType();

	protected abstract Class<ModelType> getModelType();

	AbstractQuery<TableType, ModelType> where(
			Criterion<TableType, ModelType> criterion) {
		this.where = criterion;
		return this;
	}

	public AbstractQuery<TableType, ModelType> where(
			Criteria<TableType, ModelType> criterion) {
		return this.where((Criterion<TableType, ModelType>)criterion);
	}

	public Expression<TableType, ModelType> where(TableType column) {
		return new Expression<TableType, ModelType>(this, column);
	}

	public AbstractQuery<TableType, ModelType> sort(
			Database.OrderBy<TableType> order) {
		this.order = order;
		return this;
	}

	public AbstractQuery<TableType, ModelType> orderBy(
			boolean ascending, TableType... columns) {
		if (this.order == null) {
			this.order = new Database.OrderBy<TableType>() {

				// nothing

			};
		}
		this.order.with(ascending, columns);
		return this;
	}

	public AbstractQuery<TableType, ModelType> orderByAscending(
			TableType... columns) {
		return this.orderBy(true, columns);
	}

	public AbstractQuery<TableType, ModelType> orderByDescending(
			TableType... columns) {
		return this.orderBy(false, columns);
	}

	void buildWhereClause(QueryBuilder<TableType> builder) {
		if (this.where == null) return;
		this.where.build(builder, true);
	}

	void buildOrderByClause(QueryBuilder<TableType> builder) {
		if (this.order == null) return;
		this.order.build(builder);
	}

	protected QueryBuilder<TableType> newQueryBuilder() {
		Database database;

		database = this.getDatabase();
		return new QueryBuilder<TableType>(
				database.getDialect(),
				database.getTable(this.getTableType()));
	}

	public String buildSelectStatement() {
		QueryBuilder<TableType> builder;

		builder = this.newQueryBuilder();
		this.buildWhereClause(builder);
		this.buildOrderByClause(builder);
		return builder.toString();
	}

}

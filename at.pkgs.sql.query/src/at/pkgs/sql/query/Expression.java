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

import java.util.Arrays;

public class Expression<TableType extends Enum<?>, ModelType> {

	private abstract class AbstractCriterion
	extends Criterion<TableType, ModelType> {

		protected abstract void build(QueryBuilder<TableType> builder);

		AbstractCriterion(Parent<TableType, ModelType> parent) {
			super(parent);
		}

		@Override
		boolean isEmpty() {
			return false;
		}

		@Override
		void build(QueryBuilder<TableType> builder, boolean where) {
			if (where) builder.append(" WHERE ");
			this.build(builder);
		}

	}

	private final Criteria.Parent<TableType, ModelType> parent;

	private final TableType column;

	Expression(
			Criteria.Parent<TableType, ModelType> parent,
			TableType column) {
		this.parent = parent;
		this.column = column;
	}

	public Criterion<TableType, ModelType> isNull() {
		return new AbstractCriterion(this.parent) {

			@Override
			protected void build(QueryBuilder<TableType> builder) {
				builder.column(
						Expression.this.column,
						" IS NULL");
			}

		};
	}

	public Criterion<TableType, ModelType> isNotNull() {
		return new AbstractCriterion(this.parent) {

			@Override
			protected void build(QueryBuilder<TableType> builder) {
				builder.column(
						Expression.this.column,
						" IS NOT NULL");
			}

		};
	}

	public Criterion<TableType, ModelType> is(
			final Object value) {
		return new AbstractCriterion(this.parent) {

			@Override
			boolean isEmpty() {
				return value == null;
			}

			@Override
			protected void build(QueryBuilder<TableType> builder) {
				builder.column(
						Expression.this.column,
						" = ?",
						value);
			}

		};
	}

	public Criterion<TableType, ModelType> isNot(
			final Object value) {
		return new AbstractCriterion(this.parent) {

			@Override
			boolean isEmpty() {
				return value == null;
			}

			@Override
			protected void build(QueryBuilder<TableType> builder) {
				builder.column(
						Expression.this.column,
						" <> ?",
						value);
			}

		};
	}

	public Criterion<TableType, ModelType> lessThan(
			final Object value) {
		return new AbstractCriterion(this.parent) {

			@Override
			boolean isEmpty() {
				return value == null;
			}

			@Override
			protected void build(QueryBuilder<TableType> builder) {
				builder.column(
						Expression.this.column,
						" < ?",
						value);
			}

		};
	}

	public Criterion<TableType, ModelType> atMost(
			final Object value) {
		return new AbstractCriterion(this.parent) {

			@Override
			boolean isEmpty() {
				return value == null;
			}

			@Override
			protected void build(QueryBuilder<TableType> builder) {
				builder.column(
						Expression.this.column,
						" <= ?",
						value);
			}

		};
	}

	public Criterion<TableType, ModelType> greaterThan(
			final Object value) {
		return new AbstractCriterion(this.parent) {

			@Override
			boolean isEmpty() {
				return value == null;
			}

			@Override
			protected void build(QueryBuilder<TableType> builder) {
				builder.column(
						Expression.this.column,
						" > ?",
						value);
			}

		};
	}

	public Criterion<TableType, ModelType> atLeast(
			final Object value) {
		return new AbstractCriterion(this.parent) {

			@Override
			boolean isEmpty() {
				return value == null;
			}

			@Override
			protected void build(QueryBuilder<TableType> builder) {
				builder.column(
						Expression.this.column,
						" >= ?",
						value);
			}

		};
	}

	public Criterion<TableType, ModelType> between(
			final Object left,
			final Object right) {
		return new AbstractCriterion(this.parent) {

			@Override
			protected void build(QueryBuilder<TableType> builder) {
				builder.column(
						Expression.this.column,
						" BETWEEN ? AND ?",
						left,
						right);
			}

		};
	}

	public Criterion<TableType, ModelType> notBetween(
			final Object left,
			final Object right) {
		return new AbstractCriterion(this.parent) {

			@Override
			protected void build(QueryBuilder<TableType> builder) {
				builder.column(
						Expression.this.column,
						" NOT BETWEEN ? AND ?",
						left,
						right);
			}

		};
	}

	public Criterion<TableType, ModelType> oneOf(
			final Iterable<Object> values) {
		return new AbstractCriterion(this.parent) {

			@Override
			boolean isEmpty() {
				return values == null;
			}

			@Override
			protected void build(QueryBuilder<TableType> builder) {
				builder.column(
						Expression.this.column,
						" IN(");
				builder.append(null, values);
				builder.append(")");
			}

		};
	}

	public Criterion<TableType, ModelType> oneOf(
			Object... values) {
		return this.oneOf(Arrays.asList(values));
	}

	public Criterion<TableType, ModelType> noneOf(
			final Iterable<Object> values) {
		return new AbstractCriterion(this.parent) {

			@Override
			boolean isEmpty() {
				return values == null;
			}

			@Override
			protected void build(QueryBuilder<TableType> builder) {
				builder.column(
						Expression.this.column,
						" NOT IN(");
				builder.append(null, values);
				builder.append(")");
			}

		};
	}

	public Criterion<TableType, ModelType> noneOf(
			Object... values) {
		return this.noneOf(Arrays.asList(values));
	}

	public Criterion<TableType, ModelType> evaluate(
			final Database.Criterion<TableType> criterion) {
		return new AbstractCriterion(this.parent) {

			@Override
			protected void build(QueryBuilder<TableType> builder) {
				criterion.build(builder);
			}

		};
	}

}

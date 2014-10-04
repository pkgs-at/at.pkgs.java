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

public class Expression<TableType extends Enum<?>> {

	private abstract class AbstractCriterion extends Criterion<TableType> {

		protected abstract void build(QueryBuilder<TableType> builder);

		AbstractCriterion(Parent<TableType> parent) {
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

	private final Criteria.Parent<TableType> parent;

	private final TableType column;

	Expression(Criteria.Parent<TableType> parent, TableType column) {
		this.parent = parent;
		this.column = column;
	}

	public Criterion<TableType> isNull() {
		return new AbstractCriterion(this.parent) {

			@Override
			protected void build(QueryBuilder<TableType> builder) {
				builder.append(
						Expression.this.column,
						" IS NULL");
			}

		};
	}

	public Criterion<TableType> isNotNull() {
		return new AbstractCriterion(this.parent) {

			@Override
			protected void build(QueryBuilder<TableType> builder) {
				builder.append(
						Expression.this.column,
						" IS NOT NULL");
			}

		};
	}

	public Criterion<TableType> is(
			final Object value) {
		return new AbstractCriterion(this.parent) {

			@Override
			boolean isEmpty() {
				return value == null;
			}

			@Override
			protected void build(QueryBuilder<TableType> builder) {
				builder.append(
						Expression.this.column,
						" = ?",
						value);
			}

		};
	}

	public Criterion<TableType> isNot(
			final Object value) {
		return new AbstractCriterion(this.parent) {

			@Override
			boolean isEmpty() {
				return value == null;
			}

			@Override
			protected void build(QueryBuilder<TableType> builder) {
				builder.append(
						Expression.this.column,
						" <> ?",
						value);
			}

		};
	}

	public Criterion<TableType> between(
			final Object left,
			final Object right) {
		return new AbstractCriterion(this.parent) {

			@Override
			protected void build(QueryBuilder<TableType> builder) {
				builder.append(
						Expression.this.column,
						" BETWEEN ? AND ?",
						left,
						right);
			}

		};
	}

	public Criterion<TableType> notBetween(
			final Object left,
			final Object right) {
		return new AbstractCriterion(this.parent) {

			@Override
			protected void build(QueryBuilder<TableType> builder) {
				builder.append(
						Expression.this.column,
						" NOT BETWEEN ? AND ?",
						left,
						right);
			}

		};
	}

	public Criterion<TableType> oneOf(
			final Iterable<Object> values) {
		return new AbstractCriterion(this.parent) {

			@Override
			boolean isEmpty() {
				return values == null;
			}

			@Override
			protected void build(QueryBuilder<TableType> builder) {
				boolean first;

				first = true;
				for (Object value : values) {
					if (first) {
						first = false;
						builder.append(
								Expression.this.column,
								" IN(?",
								value);
					}
					else {
						builder.append(
								", ?",
								value);
					}
				}
				builder.append(")");
			}

		};
	}

	public Criterion<TableType> oneOf(
			Object... values) {
		return this.oneOf(Arrays.asList(values));
	}

	public Criterion<TableType> noneOf(
			final Iterable<Object> values) {
		return new AbstractCriterion(this.parent) {

			@Override
			boolean isEmpty() {
				return values == null;
			}

			@Override
			protected void build(QueryBuilder<TableType> builder) {
				boolean first;

				first = true;
				for (Object value : values) {
					if (first) {
						first = false;
						builder.append(
								Expression.this.column,
								" NOT IN(?",
								value);
					}
					else {
						builder.append(
								", ?",
								value);
					}
				}
				builder.append(")");
			}

		};
	}

	public Criterion<TableType> noneOf(
			Object... values) {
		return this.noneOf(Arrays.asList(values));
	}

}

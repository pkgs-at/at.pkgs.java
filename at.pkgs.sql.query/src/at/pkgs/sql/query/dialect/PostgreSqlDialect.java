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

package at.pkgs.sql.query.dialect;

/**
 * PostgreSQL dialect class for version >= 8.2.
 */
public class PostgreSqlDialect extends AbstractDialect {

	@Override
	public boolean hasReturningSupport() {
		return true;
	}

	@Override
	protected char getIdentifierOpenCharactor() {
		return '"';
	}

	@Override
	protected char getIdentifierCloseCharactor() {
		return '"';
	}

	@Override
	public <TableType extends Enum<?>>
	SelectVisitor<TableType> newSelectVisitor() {
		return new SelectVisitor<TableType>() {

			@Override
			public void afterAll() {
				if (this.limit > 0)
					this.builder.append(
							" LIMIT ?",
							this.limit);
				if (this.offset > 0)
					this.builder.append(
							" OFFSET ?",
							this.offset);
			}

		};
	}

	@Override
	public <TableType extends Enum<?>>
	InsertVisitor<TableType> newInsertVisitor() {
		return new InsertVisitor<TableType>() {

			@Override
			public void afterAll() {
				boolean first;

				if (this.columns == null) return;
				this.builder.append(" RETURNING ");
				first = true;
				for (TableType column : this.columns) {
					if (first) first = false;
					else this.builder.append(", ");
					this.builder.column(column);
				}
			}

		};
	}

	@Override
	public <TableType extends Enum<?>>
	UpdateVisitor<TableType> newUpdateVisitor() {
		return new UpdateVisitor<TableType>() {

			@Override
			public void afterAll() {
				boolean first;

				if (this.columns == null) return;
				this.builder.append(" RETURNING ");
				first = true;
				for (TableType column : this.columns) {
					if (first) first = false;
					else this.builder.append(", ");
					this.builder.column(column);
				}
			}

		};
	}

}

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

import at.pkgs.sql.query.Database;

public class SqlServerDialect extends AbstractDialect {

	@Override
	protected char getIdentifierOpenCharactor() {
		return '[';
	}

	@Override
	protected char getIdentifierCloseCharactor() {
		return ']';
	}

	@Override
	public <TableType extends Enum<?>>
	SelectVisitor<TableType> newSelectVisitor() {
		return new SelectVisitor<TableType>() {

			@Override
			protected void initialize() {
				if (this.offset > 0)
					throw new Database.Exception(
							"SqlServerDialect not support offset");
			}

			@Override
			public boolean selectList() {
				if (this.limit > 0)
					this.builder.append(" TOP(?)", this.limit);
				return false;
			}

		};
	}

	@Override
	public <TableType extends Enum<?>>
	InsertVisitor<TableType> newInsertVisitor() {
		return new InsertVisitor<TableType>() {

			// nothing

		};
	}

	@Override
	public <TableType extends Enum<?>>
	UpdateVisitor<TableType> newUpdateVisitor() {
		return new UpdateVisitor<TableType>() {

			// nothing

		};
	}

	@Override
	public <TableType extends Enum<?>>
	DeleteVisitor<TableType> newDeleteVisitor() {
		return new DeleteVisitor<TableType>() {

			// nothing

		};
	}

}

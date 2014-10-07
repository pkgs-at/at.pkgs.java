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
 * Oracle MySQL dialect class for version >= 5.5.
 */
public class MySqlDialect extends AbstractDialect {

	@Override
	public boolean hasReturningSupport() {
		return false;
	}

	@Override
	protected char getIdentifierOpenCharactor() {
		return '`';
	}

	@Override
	protected char getIdentifierCloseCharactor() {
		return '`';
	}

	@Override
	public <TableType extends Enum<?>>
	SelectVisitor<TableType> newSelectVisitor() {
		return new SelectVisitor<TableType>() {

			@Override
			public void afterAll() {
				if (this.offset < 0 && this.limit < 0) return;
				if (this.offset < 0) this.offset = 0;
				if (this.limit <= 0) this.limit = Integer.MAX_VALUE;
				this.builder.append(
						" LIMIT ? OFFSET ?",
						this.limit,
						this.offset);
			}

		};
	}

}

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

public abstract class AbstractDialect implements Dialect {

	protected char getIdentifierOpenCharactor() {
		return '"';
	}

	protected char getIdentifierCloseCharactor() {
		return '"';
	}

	@Override
	public void appendIdentifier(StringBuilder builder, String name) {
		char close;
		int length;
		int index;

		builder.append(this.getIdentifierOpenCharactor());
		close = this.getIdentifierCloseCharactor();
		length = name.length();
		for (index = 0; index < length; index ++) {
			char charactor;

			charactor = name.charAt(index);
			if (charactor == close) builder.append(close);
			builder.append(charactor);
		}
		builder.append(close);
	}

	@Override
	public void currentTimestamp(StringBuilder builder) {
		builder.append("CURRENT_TIMESTAMP");
	}

	@Override
	public void currentDate(StringBuilder builder) {
		builder.append("CURRENT_DATE");
	}

	@Override
	public void currentTime(StringBuilder builder) {
		builder.append("CURRENT_TIME");
	}

	@Override
	public <TableType extends Enum<?>>
	CountVisitor<TableType> newCountVisitor() {
		return new CountVisitor<TableType>() {

			// nothing

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

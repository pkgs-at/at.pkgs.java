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
import java.util.ArrayList;

class OrderByClause<TableType> {

	private static enum Direction {

		ASCENDING("ASC"),

		DESCENDING("DESC");

		private final String text;

		private Direction(String text) {
			this.text = text;
		}

	}

	private class Entry {

		private TableType column;

		private Direction direction;

		private Entry(TableType column, Direction direction) {
			this.column = column;
			this.direction = direction;
		}

		void build(QueryBuilder<TableType> builder) {
			builder.append(this.column);
			builder.append(' ');
			builder.append(this.direction.text);
		}

	}

	private final List<Entry> list;

	protected OrderByClause() {
		this.list = new ArrayList<Entry>();
	}

	protected void ascending(TableType column) {
		this.list.add(new Entry(column, Direction.ASCENDING));
	}

	protected void descending(TableType column) {
		this.list.add(new Entry(column, Direction.DESCENDING));
	}

	void build(QueryBuilder<TableType> builder) {
		boolean first;

		if (this.list.size() <= 0) return;
		first = true;
		for (Entry entry : this.list) {
			if (first) {
				first = false;
				builder.append(" ORDER BY ");
			}
			else {
				builder.append(", ");
			}
			entry.build(builder);
		}
	}

}

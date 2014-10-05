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

import at.pkgs.sql.query.QueryBuilder;

public interface Dialect {

	public static abstract class SelectVisitor<TableType extends Enum<?>> {

		protected QueryBuilder<TableType> builder;

		protected int offset;

		protected int limit;

		protected void initialize() {
			// do nothing
		}

		public void initialize(
				QueryBuilder<TableType> builder,
				int offset,
				int limit) {
			this.builder = builder;
			this.offset = offset;
			this.limit = limit;
			this.initialize();
		}

		public boolean select() {
			return false;
		}

		public boolean allOrDistinct() {
			return false;
		}

		public boolean selectList() {
			return false;
		}

		public boolean from() {
			return false;
		}

		public boolean where() {
			return false;
		}

		public boolean orderBy() {
			return false;
		}

		public void afterAll() {
			// do nothing
		}

	}

	public void appendIdentifier(StringBuilder builder, String name);

	public <TableType extends Enum<?>>
	SelectVisitor<TableType> newSelectVisitor();

}

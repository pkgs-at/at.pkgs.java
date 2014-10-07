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

	public static abstract class InsertVisitor<TableType extends Enum<?>> {

		protected QueryBuilder<TableType> builder;

		protected void initialize() {
			// do nothing
		}

		public void initialize(
				QueryBuilder<TableType> builder) {
			this.builder = builder;
			this.initialize();
		}

		public boolean insert() {
			return false;
		}

		public boolean into() {
			return false;
		}

		public boolean into(TableType column, Object value) {
			return false;
		}

		public boolean values() {
			return false;
		}

		public boolean values(TableType column, Object value) {
			return false;
		}

		public void afterAll() {
			// do nothing
		}

	}

	public static abstract class UpdateVisitor<TableType extends Enum<?>> {

		protected QueryBuilder<TableType> builder;

		protected void initialize() {
			// do nothing
		}

		public void initialize(
				QueryBuilder<TableType> builder) {
			this.builder = builder;
			this.initialize();
		}

		public boolean update() {
			return false;
		}

		public boolean table() {
			return false;
		}

		public boolean set() {
			return false;
		}

		public boolean set(TableType column, Object value) {
			return false;
		}

		public boolean where() {
			return false;
		}

		public void afterAll() {
			// do nothing
		}

	}

	public static abstract class DeleteVisitor<TableType extends Enum<?>> {

		protected QueryBuilder<TableType> builder;

		protected void initialize() {
			// do nothing
		}

		public void initialize(
				QueryBuilder<TableType> builder) {
			this.builder = builder;
			this.initialize();
		}

		public boolean delete() {
			return false;
		}

		public boolean from() {
			return false;
		}

		public boolean where() {
			return false;
		}

		public void afterAll() {
			// do nothing
		}

	}

	public boolean hasReturningSupport();

	public void appendIdentifier(StringBuilder builder, String name);

	public void currentTimestamp(StringBuilder builder);

	public void currentDate(StringBuilder builder);

	public void currentTime(StringBuilder builder);

	public <TableType extends Enum<?>>
	SelectVisitor<TableType> newSelectVisitor();

	public <TableType extends Enum<?>>
	InsertVisitor<TableType> newInsertVisitor();

	public <TableType extends Enum<?>>
	UpdateVisitor<TableType> newUpdateVisitor();

	public <TableType extends Enum<?>>
	DeleteVisitor<TableType> newDeleteVisitor();

}

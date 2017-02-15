/*
 * Copyright (c) 2009-2015, Architector Inc., Japan
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

package at.pkgs.sql.trifle.sample;

import java.sql.Connection;
import at.pkgs.sql.trifle.AbstractDatabase;
import at.pkgs.sql.trifle.Model;
import at.pkgs.sql.trifle.Query;
import at.pkgs.sql.trifle.dialect.Dialect;
import at.pkgs.sql.trifle.dialect.PostgreSQL;

public class Sample extends Model<Sample.Column> {

	public static enum Column implements Model.Column {

		FIELD1("COLUMN1"),

		FIELD2("COLUMN2");

		private final String column;

		private Column(String column) {
			this.column = column;
		}

		@Override
		public void expression(Query query) {
			query.identifier(this.column);
		}

		@Override
		public Object serialize(Object value) {
			return value;
		}

		@Override
		public Object deserialize(Object serial) {
			return serial;
		}

	}

	public static final Dialect DIALECT = new PostgreSQL();

	public static final AbstractDatabase DATABASE = new AbstractDatabase() {

		@Override
		protected Dialect getDialect() {
			return Sample.DIALECT;
		}

	};

	public static class Via extends Model.Via<Sample> {

		@Override
		protected AbstractDatabase getDatabase() {
			return Sample.DATABASE;
		}

		@Override
		protected Connection getConnection() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void from(Query query) {
			query.identifier("TABLE1");
		}

		public void test() {
			Query query;

			query = this.query();
			query.clause(
					"SELECT ALL ",
					", ",
					null,
					null,
					Column.FIELD1,
					Column.FIELD2);
			query.clause(
					" WHERE ",
					" AND ",
					null,
					null,
					new Query.And(
							new Query.Or(
									new Query.Equal(
											Column.FIELD1,
											null),
									new Query.NotEqual(
											Column.FIELD2,
											null)),
							new Query.Or(
									new Query.Equal(
											Column.FIELD1,
											"aaa")),
									new Query.NotEqual(
											Column.FIELD2,
											"bbb")));
			System.out.println(query);
		}

	}

	private static final long serialVersionUID = 1L;

	public static final Via VIA = new Via();

	public String getField1() {
		return this.get(Column.FIELD1);
	}

	public String getField2() {
		return this.get(Column.FIELD2);
	}

	public static void main(String... arguments) {
		VIA.test();
	}

}

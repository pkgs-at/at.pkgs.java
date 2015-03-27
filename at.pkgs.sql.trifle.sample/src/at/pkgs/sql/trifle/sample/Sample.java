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

import java.util.List;
import at.pkgs.sql.trifle.Query;
import at.pkgs.sql.trifle.Model;

public class Sample extends Model<Sample.Column> {

	public static enum Column implements Model.Column {

		FIELD1("COLUMN1"),

		FIELD2("COLUMN2");

		private final String column;

		private Column(String column) {
			this.column = column;
		}

		@Override
		public String column() {
			return this.column;
		}

	}

	public static class Via extends Model.Via {

		public List<Sample> retrieve(/* TODO */) {
			Query builder;

			builder = new Query();
			builder.toString();
			return null;
		}

		public List<Sample> retrieve(Query.Criteria criteria) {
			return this.populate(null);
		}

		public Sample test() {
			return this.populate(
					this.getColumns(),
					"fc1",
					"fc2");
		}

	}

	public static final Via VIA = new Via();

	public String getField1() {
		return this.get(Column.FIELD1);
	}

	public String getField2() {
		return this.get(Column.FIELD2);
	}

	public static void main(String... arguments) {
		Sample model;
		Query query;

		model = Sample.VIA.test();
		System.out.println(model.getField1());
		System.out.println(model.getField2());
		query = new Query();
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
										new Query.StringValue(
												"aaa")),
								new Query.NotEqual(
										Column.FIELD2,
										new Query.StringValue(
												"bbb")))));
		System.out.println(query);
	}

}

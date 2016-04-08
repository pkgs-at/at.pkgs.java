/*
 * Copyright (c) 2009-2016, Architector Inc., Japan
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

package at.pkgs.sql.trifle.dialect;

import java.util.List;
import at.pkgs.sql.trifle.Model;
import at.pkgs.sql.trifle.Query;

public abstract class Dialect {

	public static interface Table {

		public void from(Query query);

		public List<Model.Column> getColumns();

	}

	public void identifier(
			Query query,
			String name) {
		query.append('"');
		for (char character : name.toCharArray()) {
			if (character == '"') query.append('"');
			query.append(character);
		}
		query.append('"');
	}

	public void quoted(
			Query query,
			String value) {
		if (value == null) {
			query.append("NULL");
			return;
		}
		query.append('\'');
		for (char character : value.toCharArray()) {
			if (character == '\'') query.append('\'');
			query.append(character);
		}
		query.append('\'');
	}

	public void select(
			Query query,
			Table table,
			boolean distinct,
			Query.Criteria criteria,
			Query.OrderBy sort,
			long offset,
			long length) {
		if (offset > 0L) throw new UnsupportedOperationException();
		if (length >= 0L) throw new UnsupportedOperationException();
		query.append("SELECT ").append(distinct ? "DISTINCT" : "ALL");
		query.append(' ').join(", ", table.getColumns());
		table.from(query.append(" FROM "));
		if (criteria != null) query.append(" WHERE ").append(criteria);
		if (sort != null) query.append(sort);
	}

	public void count(
			Query query,
			Table table,
			Query.Criteria criteria) {
		query.append("SELECT ALL COUNT(*)");
		table.from(query.append(" FROM "));
		if (criteria != null) query.append(" WHERE ").append(criteria);
	}

}

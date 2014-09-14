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

package at.pkgs.sql.persistron.criterion;

import java.util.List;
import java.util.ArrayList;
import at.pkgs.sql.persistron.QueryBuilder;
import at.pkgs.sql.persistron.StatementRunner;

public class Criteria implements Criterion {

	public enum Type {
		AND,
		OR;
	}

	private Type type;

	private List<Criterion> list;

	public Criteria(Type type) {
		this.type = type;
		this.list = new ArrayList<Criterion>();
	}

	public void add(Criterion... criterions) {
		for (Criterion criterion : criterions)
			this.list.add(criterion);
	}

	public int size() {
		return this.list.size();
	}

	@Override
	public void build(QueryBuilder builder) {
		builder.append('(');
		for (int index = 0; index < list.size(); index ++) {
			if (index > 0) {
				builder.append(' ');
				builder.append(this.type.name());
				builder.append(' ');
			}
			this.list.get(index).build(builder);
		}
		builder.append(')');
	}

	@Override
	public void bind(StatementRunner runner) {
		for (int index = 0; index < list.size(); index ++)
			this.list.get(index).bind(runner);
	}

}

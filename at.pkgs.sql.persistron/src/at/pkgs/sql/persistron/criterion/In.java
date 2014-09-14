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

import java.util.Collection;
import at.pkgs.sql.persistron.QueryBuilder;
import at.pkgs.sql.persistron.StatementRunner;

public class In<FieldType> implements Criterion {

	private Enum<?> field;

	private FieldType[] values;

	public In(Enum<?> field, FieldType... values) {
		this.field = field;
		this.values = values;
	}

	@SuppressWarnings("unchecked")
	public In(Enum<?> field, Collection<FieldType> values) {
		this.field = field;
		this.values = (FieldType[])values.toArray();
	}

	@Override
	public void build(QueryBuilder builder) {
		builder.field(this.field);
		builder.append(" IN(");
		for (int index = 0; index < this.values.length; index ++) {
			if (index > 0)
				builder.append(", ?");
			else
				builder.append('?');
		}
		builder.append(')');
	}

	@Override
	public void bind(StatementRunner runner) {
		runner.bind(this.field, this.values);
	}

}

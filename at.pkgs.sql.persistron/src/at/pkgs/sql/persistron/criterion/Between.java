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

import at.pkgs.sql.persistron.QueryBuilder;
import at.pkgs.sql.persistron.StatementRunner;

public class Between<FieldType> implements Criterion {

	private Enum<?> field;

	private FieldType value1;

	private FieldType value2;

	public Between(Enum<?> field, FieldType value1, FieldType value2) {
		this.field = field;
		this.value1 = value1;
		this.value2 = value2;
	}

	@Override
	public void build(QueryBuilder builder) {
		builder.field(this.field);
		builder.append(" BETWEEN ? AND ?");
	}

	@Override
	public void bind(StatementRunner runner) {
		runner.bind(this.field, this.value1, this.value2);
	}

}

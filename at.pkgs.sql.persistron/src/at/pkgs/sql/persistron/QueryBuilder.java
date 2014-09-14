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

package at.pkgs.sql.persistron;

import at.pkgs.sql.persistron.dialect.Dialect;

public class QueryBuilder {

	private Translator<?> translator;

	private Dialect dialect;

	private StringBuilder builder;

	public QueryBuilder(Translator<?> translator, Dialect dialect) {
		this.translator = translator;
		this.dialect = dialect;
		this.builder = new StringBuilder();
	}

	@Override
	public String toString() {
		return this.builder.toString();
	}

	public QueryBuilder append(char value) {
		this.builder.append(value);
		return this;
	}

	public QueryBuilder append(String value) {
		this.builder.append(value);
		return this;
	}

	public QueryBuilder identifier(String value) {
		this.dialect.identifier(this.builder, value);
		return this;
	}

	public QueryBuilder field(Enum<?> value) {
		this.dialect.identifier(
				this.builder,
				this.translator.get(value).column().name());
		return this;
	}

	public QueryBuilder fields(Enum<?>[] values) {
		for (int index = 0; index < values.length; index ++) {
			if (index > 0) this.builder.append(", ");
			this.field(values[index]);
		}
		return this;
	}

	public QueryBuilder range(long offset, long limit) {
		this.dialect.range(this.builder, offset, limit);
		return this;
	}

}

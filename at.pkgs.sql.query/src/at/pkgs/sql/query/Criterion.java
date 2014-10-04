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

public abstract class Criterion<TableType extends Enum<?>, ModelType> {

	static interface Parent<TableType, ModelType> {

		// nothing

	}

	private final Parent<TableType, ModelType> parent;

	Criterion(Parent<TableType, ModelType> parent) {
		this.parent = parent;
		if (parent instanceof AbstractQuery)
			((AbstractQuery<TableType, ModelType>)parent).where(this);
		if (parent instanceof Criteria)
			((Criteria<TableType, ModelType>)parent).add(this);
	}

	public Query<TableType, ModelType> query() {
		if (!(this.parent instanceof Query))
			throw new UnsupportedOperationException(
					"unsupported in this context");
		return (Query<TableType, ModelType>)this.parent;
	}

	abstract boolean isEmpty();

	abstract void build(QueryBuilder<TableType> builder, boolean where);

}

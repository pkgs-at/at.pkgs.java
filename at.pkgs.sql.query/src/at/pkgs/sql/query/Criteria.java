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

import java.util.List;
import java.util.ArrayList;

abstract class Criteria<TableType extends Enum<?>, ModelType>
extends Criterion<TableType, ModelType>
implements Criterion.Parent<TableType, ModelType> {

	private final List<Criterion<TableType, ModelType>> children; 

	protected Criteria() {
		super(null);
		this.children = new ArrayList<Criterion<TableType, ModelType>>();
	}

	abstract void appendJoint(QueryBuilder<TableType> builder);

	void add(Criterion<TableType, ModelType> child) {
		if (child.isEmpty()) return;
		this.children.add(child);
	}

	protected Expression<TableType, ModelType> with(TableType column) {
		return new Expression<TableType, ModelType>(this, column);
	}

	protected void with(Criteria<TableType, ModelType> criteria) {
		this.add(criteria);
	}

	@Override
	boolean isEmpty() {
		return this.children.size() <= 0;
	}

	@Override
	void build(QueryBuilder<TableType> builder, boolean where) {
		if (where) {
			boolean first;

			if (this.isEmpty()) return;
			first = true;
			builder.append(" WHERE ");
			for (Criterion<TableType, ModelType> child : this.children) {
				if (first) first = false;
				else this.appendJoint(builder);
				child.build(builder, false);
			}
		}
		else if (this.children.size() <= 1) {
			this.children.get(0).build(builder, false);
		}
		else {
			boolean first;

			first = true;
			builder.append('(');
			for (Criterion<TableType, ModelType> child : this.children) {
				if (first) first = false;
				else this.appendJoint(builder);
				child.build(builder, false);
			}
			builder.append(')');
		}
	}

}

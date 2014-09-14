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
import at.pkgs.sql.persistron.Sort;

public class CriterionFactory<FieldType extends Enum<?>, ValueType> {

	private FieldType field;

	public CriterionFactory(FieldType field) {
		this.field = field;
	}

	public IsNull isNull() {
		return new IsNull(this.field);
	}

	public IsNotNull isNotNull() {
		return new IsNotNull(this.field);
	}

	public Equal<ValueType> equal(
			ValueType value) {
		return new Equal<ValueType>(this.field, value);
	}

	public NotEqual<ValueType> notEqual(
			ValueType value) {
		return new NotEqual<ValueType>(this.field, value);
	}

	public LessThan<ValueType> lessThan(
			ValueType value) {
		return new LessThan<ValueType>(this.field, value);
	}

	public LessEqual<ValueType> lessEqual(
			ValueType value) {
		return new LessEqual<ValueType>(this.field, value);
	}

	public GreaterThan<ValueType> greaterThan(
			ValueType value) {
		return new GreaterThan<ValueType>(this.field, value);
	}

	public GreaterEqual<ValueType> greaterEqual(
			ValueType value) {
		return new GreaterEqual<ValueType>(this.field, value);
	}

	public In<ValueType> in(
			ValueType... values) {
		return new In<ValueType>(this.field, values);
	}

	public In<ValueType> in(
			Collection<ValueType> values) {
		return new In<ValueType>(this.field, values);
	}

	public NotIn<ValueType> notIn(
			ValueType... values) {
		return new NotIn<ValueType>(this.field, values);
	}

	public NotIn<ValueType> notIn(
			Collection<ValueType> values) {
		return new NotIn<ValueType>(this.field, values);
	}

	public Between<ValueType> between(
			ValueType value1,
			ValueType value2) {
		return new Between<ValueType>(this.field, value1, value2);
	}

	public NotBetween<ValueType> notBetween(
			ValueType value1,
			ValueType value2) {
		return new NotBetween<ValueType>(this.field, value1, value2);
	}

	public Like<ValueType> like(
			ValueType value,
			char escape) {
		return new Like<ValueType>(this.field, value, escape);
	}

	public NotLike<ValueType> notLike(
			ValueType value,
			char escape) {
		return new NotLike<ValueType>(this.field, value, escape);
	}

	public Sort ascending() {
		return new Sort(this.field, Sort.Direction.ASC);
	}

	public Sort descending() {
		return new Sort(this.field, Sort.Direction.DESC);
	}

}

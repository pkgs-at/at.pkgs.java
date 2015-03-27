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

package at.pkgs.sql.trifle;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Query {

	public static interface Variable {

		public boolean isNull();

		public void bind(
				PreparedStatement statement,
				int order)
						throws SQLException;

	}

	public static abstract class Value<ValueType> implements Variable {

		private final ValueType value;

		public Value(ValueType value) {
			this.value = value;
		}

		public ValueType getValue() {
			return this.value;
		}

		public abstract void bind(
				PreparedStatement statement,
				int order,
				ValueType value)
						throws SQLException;

		@Override
		public void bind(
				PreparedStatement statement,
				int order)
						throws SQLException {
			this.bind(statement, order, this.value);
		}

	}

	public static class StringValue extends Value<String> {

		public StringValue(String value) {
			super(value);
		}

		@Override
		public String toString() {
			return this.getValue();
		}

		@Override
		public boolean isNull() {
			return this.getValue() == null;
		}

		@Override
		public void bind(
				PreparedStatement statement,
				int order,
				String value)
						throws SQLException {
			statement.setString(order, value);
		}

	}

	public static interface Visitor {

		public boolean applicable();

		public void apply(Query query);

	}

	public static class Group implements Visitor {

		private final Object leading;

		private final Object glue;

		private final Object trailing;

		private final Object alternative;

		private final List<Object> contents;

		public Group(
				Object leading,
				Object glue,
				Object trailing,
				Object alternative,
				Object... contents) {
			this.leading = leading;
			this.glue = glue;
			this.trailing = trailing;
			this.alternative = alternative;
			this.contents = new ArrayList<Object>();
			for (Object content : contents) this.add(content);
		}

		@SuppressWarnings("unchecked")
		public void add(Object value) {
			if (value == null) {
				this.contents.add(null);
				return;
			}
			if (value.getClass().isArray()) {
				int length;

				length = Array.getLength(value);
				for (int index = 0; index < length; index ++)
					this.add(Array.get(value, index));
				return;
			}
			if (value instanceof Collection) {
				for (Object item : (Collection<Object>)value)
					this.add(item);
				return;
			}
			this.contents.add(value);
		}

		public void add(Object... values) {
			for (Object value : values) this.add(value);
		}

		@Override
		public boolean applicable() {
			if (this.alternative != null) return true;
			for (Object content : contents) {
				if (!(content instanceof Visitor)) return true;
				if (((Visitor)content).applicable()) return true;
			}
			return false;
		}

		@Override
		public void apply(Query query) {
			List<Object> contents;

			contents = new ArrayList<Object>();
			for (Object content : this.contents) {
				if (content instanceof Visitor) {
					if (!((Visitor)content).applicable()) continue;
				}
				contents.add(content);
			}
			if (contents.size() <= 0) {
				if (this.alternative != null)
					query.append(this.alternative);
				return;
			}
			if (this.leading != null)
				query.append(this.leading);
			for (int index = 0; index < contents.size(); index ++) {
				if (index > 0) query.append(this.glue);
				query.append(contents.get(index));
			}
			if (this.trailing != null)
				query.append(this.trailing);
		}

	}

	public static interface Criteria extends Visitor {

		// nothing

	}

	public static class And extends Group implements Criteria {

		public And(Criteria... contents) {
			super("(", " AND ", ")", null, (Object[])contents);
		}

	}

	public static class Or extends Group implements Criteria {

		public Or(Criteria... contents) {
			super("(", " OR ", ")", null, (Object[])contents);
		}

	}

	public static class Equal implements Criteria {

		private final Object column;

		private final Object value;

		public Equal(Object column, Object value) {
			this.column = column;
			this.value = value;
		}

		@Override
		public boolean applicable() {
			return true;
		}

		@Override
		public void apply(Query query) {
			Object value;

			if (this.value instanceof Variable)
				value = ((Variable)this.value).isNull() ? null : this.value;
			else
				value = this.value;
			if (value == null)
				query.append(this.column).append(" IS NULL");
			else
				query.append(this.column).append(" = ").append(value);
		}

	}

	public static class NotEqual implements Criteria {

		private final Object column;

		private final Object value;

		public NotEqual(Object column, Object value) {
			this.column = column;
			this.value = value;
		}

		@Override
		public boolean applicable() {
			return true;
		}

		@Override
		public void apply(Query query) {
			Object value;

			if (this.value instanceof Variable)
				value = ((Variable)this.value).isNull() ? null : this.value;
			else
				value = this.value;
			if (value == null)
				query.append(this.column).append(" IS NOT NULL");
			else
				query.append(this.column).append(" <> ").append(value);
		}

	}

	private final StringBuilder query;

	private final List<Variable> variables;

	public Query() {
		this.query = new StringBuilder();
		this.variables = new ArrayList<Variable>();
	}

	@Override
	public String toString() {
		StringBuilder builder;

		builder = new StringBuilder(this.query);
		for (Variable variable : this.variables)
			builder.append(", ").append(variable);
		return builder.toString();
	}

	protected List<Variable> variables() {
		return Collections.unmodifiableList(this.variables);
	}

	public String identifier(String name) {
		return name;
	}

	public Query append(boolean value) {
		this.query.append(value ? "TRUE" : "FALSE");
		return this;
	}

	public Query append(char value) {
		this.query.append(value);
		return this;
	}

	public Query append(float value) {
		this.query.append(value);
		return this;
	}

	public Query append(int value) {
		this.query.append(value);
		return this;
	}

	public Query append(long value) {
		this.query.append(value);
		return this;
	}

	public Query append(String value) {
		this.query.append(value);
		return this;
	}

	public Query append(Model.Column column) {
		return this.append(this.identifier(column.column()));
	}

	public Query append(Variable value) {
		this.append('?');
		this.variables.add(value);
		return this;
	}

	public Query append(Visitor value) {
		value.apply(this);
		return this;
	}

	public Query append(Object value) {
		if (value == null)
			return this.append("NULL");
		if (value instanceof Boolean)
			return this.append((boolean)((Boolean)value));
		if (value instanceof Character)
			return this.append((char)((Character)value));
		if (value instanceof Integer)
			return this.append((int)((Integer)value));
		if (value instanceof Long)
			return this.append((long)((Long)value));
		if (value instanceof String)
			return this.append((String)value);
		if (value instanceof Model.Column)
			return this.append((Model.Column)value);
		if (value instanceof Variable)
			return this.append((Variable)value);
		if (value instanceof Visitor)
			return this.append((Visitor)value);
		return this.append(value.toString());
	}

	public Query append(Object... arguments) {
		for (Object argument : arguments) this.append(argument);
		return this;
	}

	public Query clause(
			Object leading,
			Object glue,
			Object trailing,
			Object alternative,
			Object... contents) {
		return this.append(
				new Group(
						leading,
						glue,
						trailing,
						alternative,
						contents));
	}

	public PreparedStatement prepare(
			Connection connection)
					throws SQLException {
		PreparedStatement statement;

		//System.out.println(this);
		// TODO commons logging
		statement = connection.prepareStatement(this.query.toString());
		for (int index = 0; index < this.variables.size();)
			this.variables.get(index).bind(statement, ++ index);
		return statement;
	}

}

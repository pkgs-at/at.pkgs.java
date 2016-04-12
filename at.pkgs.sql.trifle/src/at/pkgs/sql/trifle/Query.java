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
import java.util.Arrays;
import java.util.Collections;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import at.pkgs.sql.trifle.dialect.Dialect;

public class Query {

	public static interface Variable {

		public boolean isNull();

		public void bind(
				PreparedStatement statement,
				int order)
						throws SQLException;

	}

	public static class ValueHolder<Type> {

		private Type value;

		public Type get() {
			return this.value;
		}

		public void set(Type value) {
			this.value = value;
		}

		@Override
		public String toString() {
			if (value == null)
				return "NULL";
			else
				return this.value.toString();
		}

	}

	public static class Value implements Variable {

		private final Object value;

		public Value(Object value) {
			this.value = value;
		}

		public Object getValue() {
			return this.value;
		}

		@Override
		public boolean isNull() {
			if (this.value instanceof ValueHolder)
				return ((ValueHolder<?>)this.value).get() == value;
			else
				return this.getValue() == null;
		}

		@Override
		public String toString() {
			return this.isNull() ? "NULL" : this.getValue().toString();
		}

		public void bind(
				PreparedStatement statement,
				int order,
				Object value)
						throws SQLException {
			if (value instanceof ValueHolder)
				statement.setObject(order, ((ValueHolder<?>)value).get());
			else
				statement.setObject(order, value);
		}

		@Override
		public void bind(
				PreparedStatement statement,
				int order)
						throws SQLException {
			this.bind(statement, order, this.getValue());
		}

	}

	public static interface Visitor {

		public boolean applicable();

		public void apply(Query query);

	}

	public static class Identifier implements Visitor {

		private final String name;

		public Identifier(String name) {
			this.name = name;
		}

		@Override
		public boolean applicable() {
			return true;
		}

		@Override
		public void apply(Query query) {
			query.identifier(name);
		}

	}

	public static class Parts implements Visitor {

		private final Object[] parts;

		public Parts(Object... parts) {
			this.parts = parts;
		}

		@Override
		public boolean applicable() {
			return true;
		}

		@Override
		public void apply(Query query) {
			for (Object part : this.parts) query.append(part);
		}

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

		public static Equal value(Object column, Object value) {
			return new Equal(column, new Value(value));
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

		public static NotEqual value(Object column, Object value) {
			return new NotEqual(column, new Value(value));
		}

	}

	public static class LessThan implements Criteria {

		private final Object column;

		private final Object value;

		public LessThan(Object column, Object value) {
			this.column = column;
			this.value = value;
		}

		@Override
		public boolean applicable() {
			return value != null;
		}

		@Override
		public void apply(Query query) {
			query.append(this.column).append(" < ").append(this.value);
		}

		public static LessThan value(Object column, Object value) {
			return new LessThan(column, new Value(value));
		}

	}

	public static class GreaterThan implements Criteria {

		private final Object column;

		private final Object value;

		public GreaterThan(Object column, Object value) {
			this.column = column;
			this.value = value;
		}

		@Override
		public boolean applicable() {
			return value != null;
		}

		@Override
		public void apply(Query query) {
			query.append(this.column).append(" > ").append(this.value);
		}

		public static GreaterThan value(Object column, Object value) {
			return new GreaterThan(column, new Value(value));
		}

	}

	public static class LessEqual implements Criteria {

		private final Object column;

		private final Object value;

		public LessEqual(Object column, Object value) {
			this.column = column;
			this.value = value;
		}

		@Override
		public boolean applicable() {
			return value != null;
		}

		@Override
		public void apply(Query query) {
			query.append(this.column).append(" <= ").append(this.value);
		}

		public static LessEqual value(Object column, Object value) {
			return new LessEqual(column, new Value(value));
		}

	}

	public static class GreaterEqual implements Criteria {

		private final Object column;

		private final Object value;

		public GreaterEqual(Object column, Object value) {
			this.column = column;
			this.value = value;
		}

		@Override
		public boolean applicable() {
			return value != null;
		}

		@Override
		public void apply(Query query) {
			query.append(this.column).append(" >= ").append(this.value);
		}

		public static GreaterEqual value(Object column, Object value) {
			return new GreaterEqual(column, new Value(value));
		}

	}

	public static class In implements Criteria {

		private final Object column;

		private final Iterable<?> values;

		public In(Object column, Iterable<?> values) {
			this.column = column;
			this.values = values;
		}

		public In(Object column, Object... values) {
			this(column, Arrays.asList(values));
		}

		@Override
		public boolean applicable() {
			for (Object value : this.values) {
				if (value instanceof Variable) {
					if (!((Variable)value).isNull()) return true;
					continue;
				}
				if (value != null) return true;
			}
			return false;
		}

		@Override
		public void apply(Query query) {
			boolean first;

			query.append(this.column).append(" IN(");
			first = true;
			for (Object value : this.values) {
				if (value instanceof Variable && ((Variable)value).isNull())
					continue;
				if (value == null)
					continue;
				if (!first) query.append(", ");
				query.append(value);
				first = false;
			}
			query.append(')');
		}

		public static In values(Object column, Iterable<?> values) {
			List<Value> list;

			list = new ArrayList<Value>();
			for (Object value : values)
				list.add(new Value(value));
			return new In(column, list);
		}

		public static In values(Object column, Object... values) {
			return In.values(column, Arrays.asList(values));
		}

	}

	public static class NotIn implements Criteria {

		private final Object column;

		private final Iterable<?> values;

		public NotIn(Object column, Iterable<?> values) {
			this.column = column;
			this.values = values;
		}

		public NotIn(Object column, Object... values) {
			this(column, Arrays.asList(values));
		}

		@Override
		public boolean applicable() {
			for (Object value : this.values) {
				if (value instanceof Variable) {
					if (!((Variable)value).isNull()) return true;
					continue;
				}
				if (value != null) return true;
			}
			return false;
		}

		@Override
		public void apply(Query query) {
			boolean first;

			query.append(this.column).append(" NOT IN(");
			first = true;
			for (Object value : this.values) {
				if (value instanceof Variable && ((Variable)value).isNull())
					continue;
				if (value == null)
					continue;
				if (!first) query.append(", ");
				query.append(value);
				first = false;
			}
			query.append(')');
		}

		public static NotIn values(Object column, Iterable<?> values) {
			List<Value> list;

			list = new ArrayList<Value>();
			for (Object value : values)
				list.add(new Value(value));
			return new NotIn(column, list);
		}

		public static NotIn values(Object column, Object... values) {
			return NotIn.values(column, Arrays.asList(values));
		}

	}

	public static class Between implements Criteria {

		private final Object left;

		private final Object first;

		private final Object second;

		public Between(Object left, Object first, Object second) {
			this.left = left;
			this.first = first;
			this.second = second;
		}

		@Override
		public boolean applicable() {
			return true;
		}

		@Override
		public void apply(Query query) {
			query
					.append(this.left)
					.append(" BETWEEN ")
					.append(this.first)
					.append(" AND ")
					.append(this.second);
		}

		public static Between value(
				Object value,
				Object first,
				Object second) {
			return new Between(
					new Value(value),
					first,
					second);
		}

		public static Between values(
				Object column,
				Object first,
				Object second) {
			return new Between(
					column,
					new Value(first),
					new Value(second));
		}

	}

	public static class NotBetween implements Criteria {

		private final Object left;

		private final Object first;

		private final Object second;

		public NotBetween(Object left, Object first, Object second) {
			this.left = left;
			this.first = first;
			this.second = second;
		}

		@Override
		public boolean applicable() {
			return true;
		}

		@Override
		public void apply(Query query) {
			query
					.append(this.left)
					.append(" NOT BETWEEN ")
					.append(this.first)
					.append(" AND ")
					.append(this.second);
		}

		public static NotBetween value(
				Object value,
				Object first,
				Object second) {
			return new NotBetween(
					new Value(value),
					first,
					second);
		}

		public static NotBetween values(
				Object column,
				Object first,
				Object second) {
			return new NotBetween(
					column,
					new Value(first),
					new Value(second));
		}

	}

	public static class OrderBy implements Visitor {

		public static abstract class Entity implements Visitor {

			private final Model.Column column;

			private final String direction;

			protected Entity(Model.Column column, String direction) {
				this.column = column;
				this.direction = direction;
			}

			@Override
			public boolean applicable() {
				return true;
			}

			@Override
			public void apply(Query query) {
				query.append(this.column).append(' ').append(this.direction);
			}

		}

		public static class Ascending extends Entity {

			public Ascending(Model.Column column) {
				super(column, "ASC");
			}

		}

		public static class Descending extends Entity {

			public Descending(Model.Column column) {
				super(column, "DESC");
			}

		}

		private final List<Entity> list;

		public OrderBy() {
			this.list = new ArrayList<Entity>();
		}

		public OrderBy add(Entity entity) {
			this.list.add(entity);
			return this;
		}

		public OrderBy ascending(Model.Column column) {
			return this.add(new Ascending(column));
		}

		public OrderBy descending(Model.Column column) {
			return this.add(new Descending(column));
		}

		@Override
		public boolean applicable() {
			return this.list.size() > 0;
		}

		@Override
		public void apply(Query query) {
			query.clause(" ORDER BY", ", ", null, null, this.list.toArray());
		}

	}

	public static final boolean DEBUG = Boolean.getBoolean(
			Query.class.getName() + ".debug");

	public static final String NEWLINE = String.format("%n");

	private final Dialect dialect;

	private final StringBuilder query;

	private final List<Variable> variables;

	public Query(Dialect dialect) {
		this.dialect = dialect;
		this.query = new StringBuilder();
		this.variables = new ArrayList<Variable>();
	}

	@Override
	public String toString() {
		StringBuilder builder;

		builder = new StringBuilder(this.query);
		if (this.variables.size() > 0) {
			builder.append(Query.NEWLINE).append("\tvalues: ");
			for (Variable variable : this.variables)
				builder.append(variable).append(", ");
			builder.setLength(builder.length() - 2);
		}
		return builder.toString();
	}

	protected List<Variable> variables() {
		return Collections.unmodifiableList(this.variables);
	}

	public Query identifier(String name) {
		this.dialect.identifier(this, name);
		return this;
	}

	public Query quoted(String value) {
		this.dialect.quoted(this, value);
		return this;
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
		column.expression(this);
		return this;
	}

	public Query append(Variable value) {
		this.append('?');
		this.variables.add(value);
		return this;
	}

	public Query append(Visitor value) {
		if (value.applicable()) value.apply(this);
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

	public Query value(Object value) {
		return this.append(new Value(value));
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

	public Query join(
			Object glue,
			Object... contents) {
		return this.append(
				new Group(
						null,
						glue,
						null,
						null,
						contents));
	}

	public PreparedStatement bind(
			PreparedStatement statement)
					throws SQLException {
		if (Query.DEBUG) System.out.println(this);
		for (int index = 0; index < this.variables.size();)
			this.variables.get(index).bind(statement, ++ index);
		return statement;
	}

	public PreparedStatement prepare(
			Connection connection)
					throws SQLException {
		return this.bind(connection.prepareStatement(this.query.toString()));
	}

	public static Value valueOf(Object value) {
		return new Value(value);
	}

	public static Identifier identifierOf(String name) {
		return new Identifier(name);
	}

}

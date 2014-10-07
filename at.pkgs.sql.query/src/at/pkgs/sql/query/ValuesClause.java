package at.pkgs.sql.query;

import java.util.List;
import java.util.ArrayList;
import at.pkgs.sql.query.dialect.Dialect;

abstract class ValuesClause<TableType extends Enum<?>> {

	private class Entry {

		private TableType column;

		private Object value;

		private Entry(TableType column, Object value) {
			this.column = column;
			this.value = value;
		}

		@SuppressWarnings("unchecked")
		void build(QueryBuilder<TableType> builder) {
			if (this.value instanceof Database.Expression) {
				((Database.Expression<TableType>)this.value).build(builder);
			}
			else if (this.value instanceof Database.ColumnValue) {
				switch ((Database.ColumnValue)this.value) {
				case DefaultValue :
					builder.append(" DEFAULT");
					break;
				case CurrentTimestamp :
					builder.currentTimestamp();
					break;
				case CurrentDate :
					builder.currentDate();
					break;
				case CurrentTime :
					builder.currentTime();
					break;
				default :
					throw new Database.Exception(
							"%s not supported in Query insert / update",
							this.value);
				}
			}
			else if (builder.isColumn(this.value)) {
				builder.column((TableType)this.value);
			}
			else {
				builder.append("?", this.value);
			}
		}

	}

	private final List<Entry> list;

	protected ValuesClause() {
		this.list = new ArrayList<Entry>();
	}

	protected void with(TableType column, Object value) {
		if (value == Database.ColumnValue.KeepOriginal) return;
		this.list.add(new Entry(column, value));
	}

	void buildIntoClause(
			QueryBuilder<TableType> builder,
			Dialect.InsertVisitor<TableType> visitor) {
		boolean first;

		builder.append(" INTO ");
		builder.qualifiedTableName();
		builder.append('(');
		first = true;
		for (Entry entry : this.list) {
			if (visitor.into(entry.column, entry.value)) continue;
			if (entry.value == Database.ColumnValue.DefaultValue) continue;
			if (first) first = false;
			else builder.append(", ");
			builder.column(entry.column);
		}
		builder.append(')');
	}

	void buildValuesClause(
			QueryBuilder<TableType> builder,
			Dialect.InsertVisitor<TableType> visitor) {
		boolean first;

		builder.append(" VALUES(");
		first = true;
		for (Entry entry : this.list) {
			if (visitor.values(entry.column, entry.value)) continue;
			if (entry.value == Database.ColumnValue.DefaultValue) continue;
			if (first) first = false;
			else builder.append(", ");
			entry.build(builder);
		}
		builder.append(')');
	}

	void buildSetClause(
			QueryBuilder<TableType> builder,
			Dialect.UpdateVisitor<TableType> visitor) {
		boolean first;

		if (this.list.size() <= 0)
			throw new Database.Exception("empty update values");
		first = true;
		for (Entry entry : this.list) {
			if (visitor.set(entry.column, entry.value)) continue;
			if (first) {
				first = false;
				builder.append(" SET ");
			}
			else {
				builder.append(", ");
			}
			builder.column(entry.column, " = ");
			entry.build(builder);
		}
	}

}

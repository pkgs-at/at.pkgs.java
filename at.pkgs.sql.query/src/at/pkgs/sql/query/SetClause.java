package at.pkgs.sql.query;

import java.util.List;
import java.util.ArrayList;

abstract class SetClause<TableType extends Enum<?>> {

	private class Entry {

		private TableType column;

		private Object value;

		private Entry(TableType column, Object value) {
			this.column = column;
			this.value = value;
		}

		@SuppressWarnings("unchecked")
		void build(QueryBuilder<TableType> builder) {
			builder.column(this.column, " = ");
			if (this.value instanceof Database.Expression) {
				((Database.Expression<TableType>)this.value).build(builder);
			}
			else if (this.value instanceof Database.ColumnValue) {
				switch ((Database.ColumnValue)this.value) {
				case DefaultValue :
					builder.defaultValue(this.column);
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
							"%s not supported in Query update",
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

	protected SetClause() {
		this.list = new ArrayList<Entry>();
	}

	protected void with(TableType column, Object value) {
		if (value == Database.ColumnValue.KeepOriginal) return;
		this.list.add(new Entry(column, value));
	}

	void build(QueryBuilder<TableType> builder) {
		boolean first;

		if (this.list.size() <= 0) return;
		first = true;
		for (Entry entry : this.list) {
			if (first) {
				first = false;
				builder.append(" SET ");
			}
			else {
				builder.append(", ");
			}
			entry.build(builder);
		}
	}

}

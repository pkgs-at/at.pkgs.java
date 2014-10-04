package at.pkgs.sql.query;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import at.pkgs.sql.query.dialect.Dialect;

public class Database {

	public static class Exception extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public Exception(Throwable cause, String message) {
			super(message, cause);
		}

		public Exception(Throwable cause) {
			super(cause);
		}

		public Exception(String message) {
			super(message);
		}

		public Exception(Throwable cause, String format, Object... arguments) {
			this(cause, String.format(format, arguments));
		}

		public Exception(String format, Object... arguments) {
			this(String.format(format, arguments));
		}

	}

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Table {

		public String schema() default "";

		public String name();

		public Class<?> type();

	}

	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Column {

		public String name();

	}

	public static abstract class And<TableType> extends Criteria<TableType> {

		void appendJoint(QueryBuilder<TableType> builder) {
			builder.append(" AND ");
		}

	}

	public static abstract class Or<TableType> extends Criteria<TableType> {

		void appendJoint(QueryBuilder<TableType> builder) {
			builder.append(" OR ");
		}

	}

	public static abstract class OrderBy<TableType> {

		enum Direction {

			ASCENDING("ASC"),

			DESCENDING("DESC");

			private final String text;

			private Direction(String text) {
				this.text = text;
			}

		}

		class Entry {

			private TableType column;

			private Direction direction;

			private Entry(TableType column, Direction direction) {
				this.column = column;
				this.direction = direction;
			}

			void build(QueryBuilder<TableType> builder) {
				builder.append(this.column);
				builder.append(' ');
				builder.append(this.direction.text);
			}

		}

		private final List<Entry> list;

		public OrderBy() {
			this.list = new ArrayList<Entry>();
		}

		protected void ascending(TableType column) {
			this.list.add(new Entry(column, Direction.ASCENDING));
		}

		protected void descending(TableType column) {
			this.list.add(new Entry(column, Direction.DESCENDING));
		}

		void build(QueryBuilder<TableType> builder) {
			boolean first;

			if (this.list.size() <= 0) return;
			first = true;
			for (Entry entry : this.list) {
				if (first) {
					first = false;
					builder.append(" ORDER BY ");
				}
				else {
					builder.append(", ");
				}
				entry.build(builder);
			}
		}

	}

	private final Dialect dialect;

	private final DataSource source;

	private final ConcurrentMap<Class<?>, TableDefinition<?>> tables;

	public Database(Dialect dialect, DataSource source) {
		this.dialect = dialect;
		this.source = source;
		this.tables = new ConcurrentHashMap<Class<?>, TableDefinition<?>>();
	}

	Dialect getDialect() {
		return this.dialect;
	}

	@SuppressWarnings("unchecked")
	<TableType extends Enum<?>> TableDefinition<TableType> getTable(
			Class<TableType> type) {
		if (!this.tables.containsKey(type))
			this.tables.putIfAbsent(
					type,
					new TableDefinition<TableType>(type));
		return (TableDefinition<TableType>)this.tables.get(type);
	}

	public <TableType extends Enum<?>> Query<TableType> query(
			Class<TableType> type) {
		return new Query<TableType>(this, this.getTable(type));
	}

}

package at.pkgs.sql.query;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

class ColumnDefinition<TableType> {

	private final Database.Column annotation;

	ColumnDefinition(
			TableDefinition<TableType> table,
			TableType column) {
		final Enum<?> entry;
		Database.Column annotation;
		Field field;

		entry = (Enum<?>)column;
		try {
			field = entry.getDeclaringClass().getDeclaredField(entry.name());
		}
		catch (Exception throwable) {
			throw new Database.Exception(throwable);
		}
		annotation = field.getAnnotation(Database.Column.class);
		if (annotation == null) annotation = new Database.Column() {

			@Override
			public Class<? extends Annotation> annotationType() {
				return this.getClass();
			}

			@Override
			public String name() {
				return entry.name();
			}

		};
		this.annotation = annotation;
	}

	String getName() {
		return this.annotation.name();
	}

}

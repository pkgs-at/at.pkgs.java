package at.pkgs.sql.query.sample;

import at.pkgs.sql.query.Database;
import at.pkgs.sql.query.dialect.SqlServerDialect;

public class Program {

	private static class PreferenceQuery
	extends Database.Query<Preference.Table, Preference> {

		protected PreferenceQuery() {
			super(Preference.Table.class, Preference.class);
		}

	}

	public static void main(String[] arguments) {
		Database database;

		database = new Database(new SqlServerDialect(), null);
		System.out.println(
				database.query(Preference.Table.class, Preference.class)
						.buildSelectStatement());
		System.out.println(
				database.query(Preference.Table.class, Preference.class)
						.where(Preference.Table.Key).is("AAA")
						.query().buildSelectStatement());
		System.out.println(
				database.query(Preference.Table.class, Preference.class)
						.where(new Database.And<Preference.Table, Preference>(){{
							with(Preference.Table.Key).is("AAA");
						}})
						.buildSelectStatement());
		System.out.println(
				database.query(Preference.Table.class, Preference.class)
						.where(new Database.And<Preference.Table, Preference>(){{
							with(Preference.Table.Key).is(null);
						}})
						.buildSelectStatement());
		System.out.println(
				database.query(new PreferenceQuery() {{
					where(Preference.Table.Key).is("AAA");
				}}).buildSelectStatement());
		System.out.println(
				database.query(new PreferenceQuery() {{
					where(new And() {{
						with(Preference.Table.Key).is("AAA");
						with(Preference.Table.Value).isNotNull();
					}});
				}}).buildSelectStatement());
		System.out.println(
				database.query(new PreferenceQuery() {{
					where(new And() {{
						with(Preference.Table.Key).is("AAA");
						with(Preference.Table.Value).isNotNull();
						with(new Or() {{
						}});
					}});
					orderBy(true, Preference.Table.CreatedAt);
					orderBy(false, Preference.Table.UpdatedAt);
				}}).buildSelectStatement());
		System.out.println(
				database.query(new PreferenceQuery() {{
					where(new And() {{
						with(Preference.Table.Key).is("AAA");
						with(Preference.Table.Value).isNotNull();
						with(new Or() {{
							with(Preference.Table.Value).is("ZZ");
							with(Preference.Table.Value).is("YY");
						}});
					}});
					sort(new OrderBy() {{
						ascending(Preference.Table.CreatedAt);
						descending(Preference.Table.UpdatedAt);
					}});
					distinct();
					limit(1);
				}}).buildSelectStatement());
	}

}

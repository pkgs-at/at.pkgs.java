package at.pkgs.sql.query.sample;

import java.sql.DriverManager;
import java.sql.Connection;
import at.pkgs.sql.query.Database;
import at.pkgs.sql.query.dialect.DerbyDialect;

public class Program {

	private static class PreferenceQuery
	extends Database.Query<Preference.Table, Preference> {

		protected PreferenceQuery() {
			super(Preference.Table.class, Preference.class);
		}

	}

	public static void main(String[] arguments) throws Exception {
		Connection connection;
		Database database;

		Class.forName(
				"org.apache.derby.jdbc.EmbeddedDriver");
		connection = DriverManager.getConnection(
				"jdbc:derby:memory:test;create=true");
		database = new Database(new DerbyDialect(), null);
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
		database.executeAffectedRows(
				connection,
				database
						.newQueryBuilder(Preference.Table.class)
						.append("CREATE TABLE ")
						.appendQualifiedTableName()
						.append('(')
						.append(Preference.Table.Key)
						.append(" CHARACTER VARYING(255) NOT NULL")
						.append(", ")
						.append(Preference.Table.Value)
						.append(" CHARACTER VARYING(4095) NOT NULL")
						.append(", ")
						.append(Preference.Table.CreatedAt)
						.append(" TIMESTAMP NOT NULL")
						.append(", ")
						.append(Preference.Table.UpdatedAt)
						.append(" TIMESTAMP NOT NULL")
						.append(')')
						.dump(System.out));
		database.executeAffectedRows(
				connection,
				database
						.newQueryBuilder(Preference.Table.class)
						.append("INSERT INTO ")
						.appendQualifiedTableName()
						.append('(')
						.append(Preference.Table.Key)
						.append(", ")
						.append(Preference.Table.Value)
						.append(", ")
						.append(Preference.Table.CreatedAt)
						.append(", ")
						.append(Preference.Table.UpdatedAt)
						.append(')')
						.append(
								" VALUES (?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
								"AAA",
								"VVV")
						.dump(System.out));
		for (Preference model : database.query(new PreferenceQuery() {{
			
		}}).selectAll(connection)) {
			System.out.println(model);
		}
		connection.close();
	}

}

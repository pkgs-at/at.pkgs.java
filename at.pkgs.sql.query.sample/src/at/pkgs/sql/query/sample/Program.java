package at.pkgs.sql.query.sample;

import java.sql.DriverManager;
import java.sql.Connection;
import at.pkgs.sql.query.Database;
import at.pkgs.sql.query.dialect.DerbyDialect;

public class Program {

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
						.where(Preference.Table.Key).oneOf("AAA", "BBB")
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
				database.query(new Preference.Query() {{
					where(Preference.Table.Key).is("AAA");
				}}).buildSelectStatement());
		System.out.println(
				database.query(new Preference.Query() {{
					where(new And() {{
						with(Preference.Table.Key).is("AAA");
						with(Preference.Table.Value).isNotNull();
					}});
				}}).buildSelectStatement());
		System.out.println(
				database.query(new Preference.Query() {{
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
				database.query(new Preference.Query() {{
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
		database.newQueryBuilder(Preference.Table.class)
				.append("CREATE TABLE ")
				.qualifiedTableName()
				.append('(')
				.column(Preference.Table.Key)
				.append(" CHARACTER VARYING(255) NOT NULL")
				.append(", ")
				.column(Preference.Table.Value)
				.append(" CHARACTER VARYING(4095) NOT NULL")
				.append(", ")
				.column(Preference.Table.CreatedAt)
				.append(" TIMESTAMP NOT NULL")
				.append(", ")
				.column(Preference.Table.UpdatedAt)
				.append(" TIMESTAMP NOT NULL")
				.append(')')
				.dump(System.out)
				.execute()
				.asAffectedRows(connection);
		database.newQueryBuilder(Preference.Table.class)
				.append("INSERT INTO ")
				.qualifiedTableName()
				.append('(')
				.columns(
						Preference.Table.Key,
						Preference.Table.Value,
						Preference.Table.CreatedAt,
						Preference.Table.UpdatedAt)
				.append(')')
				.append(
						" VALUES (?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
						"AAA",
						"VVV")
				.dump(System.out)
				.execute()
				.asAffectedRows(connection);
		for (Preference model : database.query(new Preference.Query() {{
			
		}}).selectAll(connection)) {
			System.out.println(model);
		}
		connection.close();
	}

}

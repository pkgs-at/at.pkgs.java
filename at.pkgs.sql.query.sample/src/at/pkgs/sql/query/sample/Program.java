package at.pkgs.sql.query.sample;

import java.sql.DriverManager;
import java.sql.Connection;
import at.pkgs.sql.query.Database;
import at.pkgs.sql.query.QueryBuilder;
import at.pkgs.sql.query.dialect.DerbyDialect;
import at.pkgs.sql.query.dialect.PostgreSqlDialect;
import at.pkgs.sql.query.dialect.MySqlDialect;
import at.pkgs.sql.query.dialect.SqlServerDialect;
import at.pkgs.sql.query.sample.Preference.Table;

public class Program {

	public static void main(String[] arguments) throws Exception {
		Connection connection;
		Database database;

		Class.forName(
				"org.apache.derby.jdbc.EmbeddedDriver");
		connection = DriverManager.getConnection(
				"jdbc:derby:memory:test;create=true");
		database = new Database(new DerbyDialect(), null);
		database.setInsertDumpCollector(Database.DumpCollector.out);
		database.setUpdateDumpCollector(Database.DumpCollector.out);
		database.setDeleteDumpCollector(Database.DumpCollector.out);
		database.query(Preference.Table.class, Preference.class)
				.dumpSelectIf(true, Database.DumpCollector.out);
		database.query(Preference.Table.class, Preference.class)
				.where(Preference.Table.Key).oneOf("AAA", "BBB")
				.query()
				.dumpSelectIf(true, Database.DumpCollector.out);
		database.query(Preference.Table.class, Preference.class)
				.where(new Database.And<Preference.Table, Preference>(){{
					with(Preference.Table.Key).is("AAA");
				}})
				.dumpSelectIf(true, Database.DumpCollector.out);
		database.query(Preference.Table.class, Preference.class)
				.where(new Database.And<Preference.Table, Preference>(){{
					with(Preference.Table.Key).is(null);
				}})
				.dumpSelectIf(true, Database.DumpCollector.out);
		database.query(new Preference.Query() {{
			where(Preference.Table.Key).is("AAA");
		}}).dumpSelectIf(true, Database.DumpCollector.out);
		database.query(new Preference.Query() {{
			where(new And() {{
				with(Preference.Table.Key).is("AAA");
				with(Preference.Table.Value).isNotNull();
			}});
		}}).dumpDeleteIf(true, Database.DumpCollector.out);
		database.query(new Preference.Query() {{
			where(new And() {{
				with(Preference.Table.Key).is("AAA");
				with(Preference.Table.Value).isNotNull();
				with(new Or() {{
				}});
			}});
			orderBy(true, Preference.Table.CreatedAt);
			orderBy(false, Preference.Table.UpdatedAt);
		}}).dumpSelectIf(true, Database.DumpCollector.out);
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
		}}).dumpSelectIf(true, Database.DumpCollector.out);
		database.query(new Preference.Query() {{
		}}).dumpCountIf(true, Database.DumpCollector.out);
		database.query(new Preference.Query() {{
			distinct();
			columns(Preference.Table.Value);
			where(Preference.Table.Key).greaterThan("B");
		}}).dumpCountIf(true, Database.DumpCollector.out);
		database.query(new Preference.Query() {{
			set(new Values() {{
				with(Preference.Table.Value, new Expression() {

					@Override
					public void build(QueryBuilder<Table> builder) {
						builder.append("CONCAT(");
						builder.column(Preference.Table.Key, ", ?)", "'s default");
					}

				});
				with(Preference.Table.CreatedAt, Database.ColumnValue.KeepOriginal);
				with(Preference.Table.UpdatedAt, Database.ColumnValue.DefaultValue);
			}});
			where(new And() {{
				with(Preference.Table.Key).is("AAA");
			}});
		}}).dumpUpdateIf(true, Database.DumpCollector.out);
		database.query(new Preference.Query() {{
			set(Preference.Table.Key, "AAB");
			set(Preference.Table.Value, Preference.Table.Key);
			set(Preference.Table.UpdatedAt, Database.ColumnValue.CurrentTimestamp);
			where(new And() {{
				with(Preference.Table.Key).is("AAA");
			}});
		}}).dumpUpdateIf(true, Database.DumpCollector.out);
		database.newQueryBuilder(Preference.Table.class)
				.append("CREATE TABLE ")
				.qualifiedTableName()
				.append('(')
				.column(
						Preference.Table.PreferenceId,
						" INTEGER NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY(START WITH 1, INCREMENT BY 1),")
				.column(
						Preference.Table.Key,
						" CHARACTER VARYING(255) NOT NULL,")
				.column(
						Preference.Table.Value,
						" CHARACTER VARYING(4095) NOT NULL,")
				.column(
						Preference.Table.CreatedAt,
						" TIMESTAMP NOT NULL,")
				.column(Preference.Table.UpdatedAt,
						" TIMESTAMP NOT NULL)")
				.dumpIf(true, Database.DumpCollector.out)
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
				.dumpIf(true, Database.DumpCollector.out)
				.execute()
				.asAffectedRows(connection);
		database.query(new Preference.Query() {{
			set(new Values() {{
				with(Preference.Table.Key, "BB");
				with(Preference.Table.Value, "BB value");
				with(Preference.Table.CreatedAt, Database.ColumnValue.CurrentTimestamp);
				with(Preference.Table.UpdatedAt, Database.ColumnValue.CurrentTimestamp);
			}});
		}}).dumpInsertIf(true, Database.DumpCollector.out).insert(connection);
		Preference model0 = new Preference("KEY", "VALUE");
		System.out.println(
				database.insert(
						connection,
						Preference.Table.class,
						model0));
		System.out.println(model0);
		Thread.sleep(1000L);
		System.out.println(
				database.update(
						connection,
						Preference.Table.class,
						model0));
		System.out.println(model0);
		System.out.println(
				database.delete(
						connection,
						Preference.Table.class,
						model0));
		for (Preference model : database.query(new Preference.Query() {{
			
		}}).select(connection)) {
			System.out.println(model);
		}
		connection.close();
		database = new Database(new PostgreSqlDialect(), null);
		database.query(new Preference.Query() {{
			set(new Values() {{
				with(Preference.Table.Key, "KEY");
				with(Preference.Table.Value, "VALUE");
			}});
			where(Preference.Table.PreferenceId).is(1);
		}}).dumpSelectOneIf(true, Database.DumpCollector.out);
		database.query(new Preference.Query() {{
			set(new Values() {{
				with(Preference.Table.Key, "KEY");
				with(Preference.Table.Value, "VALUE");
			}});
			columns(
					Preference.Table.PreferenceId,
					Preference.Table.CreatedAt,
					Preference.Table.UpdatedAt);
		}}).dumpInsertIf(true, Database.DumpCollector.out);
		database.query(new Preference.Query() {{
			set(new Values() {{
				with(Preference.Table.Key, "KEY");
				with(Preference.Table.Value, "VALUE");
			}});
			where(Preference.Table.PreferenceId).is(1);
			columns(
					Preference.Table.PreferenceId,
					Preference.Table.CreatedAt,
					Preference.Table.UpdatedAt);
		}}).dumpUpdateIf(true, Database.DumpCollector.out);
		database = new Database(new MySqlDialect(), null);
		database.query(new Preference.Query() {{
			set(new Values() {{
				with(Preference.Table.Key, "KEY");
				with(Preference.Table.Value, "VALUE");
			}});
			where(Preference.Table.PreferenceId).is(1);
		}}).dumpSelectOneIf(true, Database.DumpCollector.out);
		database.query(new Preference.Query() {{
			set(new Values() {{
				with(Preference.Table.Key, "KEY");
				with(Preference.Table.Value, "VALUE");
			}});
			columns(
					Preference.Table.PreferenceId,
					Preference.Table.CreatedAt,
					Preference.Table.UpdatedAt);
		}}).dumpInsertIf(true, Database.DumpCollector.out);
		database.query(new Preference.Query() {{
			set(new Values() {{
				with(Preference.Table.Key, "KEY");
				with(Preference.Table.Value, "VALUE");
			}});
			where(Preference.Table.PreferenceId).is(1);
			columns(
					Preference.Table.PreferenceId,
					Preference.Table.CreatedAt,
					Preference.Table.UpdatedAt);
		}}).dumpUpdateIf(true, Database.DumpCollector.out);
		database = new Database(new SqlServerDialect(), null);
		database.query(new Preference.Query() {{
			set(new Values() {{
				with(Preference.Table.Key, "KEY");
				with(Preference.Table.Value, "VALUE");
			}});
			where(Preference.Table.PreferenceId).is(1);
		}}).dumpSelectOneIf(true, Database.DumpCollector.out);
		database.query(new Preference.Query() {{
			set(new Values() {{
				with(Preference.Table.Key, "KEY");
				with(Preference.Table.Value, "VALUE");
			}});
			columns(
					Preference.Table.PreferenceId,
					Preference.Table.CreatedAt,
					Preference.Table.UpdatedAt);
		}}).dumpInsertIf(true, Database.DumpCollector.out);
		database.query(new Preference.Query() {{
			set(new Values() {{
				with(Preference.Table.Key, "KEY");
				with(Preference.Table.Value, "VALUE");
			}});
			where(Preference.Table.PreferenceId).is(1);
			columns(
					Preference.Table.PreferenceId,
					Preference.Table.CreatedAt,
					Preference.Table.UpdatedAt);
		}}).dumpUpdateIf(true, Database.DumpCollector.out);
	}

}

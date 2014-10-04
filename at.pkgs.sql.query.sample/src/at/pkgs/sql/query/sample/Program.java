package at.pkgs.sql.query.sample;

import at.pkgs.sql.query.*;
import at.pkgs.sql.query.Database.*;
import at.pkgs.sql.query.dialect.*;
import at.pkgs.sql.query.sample.Preference.Table;

public class Program {

	public static void main(String[] arguments) {
		Database database;

		database = new Database(new SqlServerDialect(), null);
		System.out.println(
				database.query(Table.class)
						.buildSelectStatement());
		System.out.println(
				database.query(Table.class)
						.where(Table.Key).is("AAA")
						.query().buildSelectStatement());
		System.out.println(
				database.query(Table.class)
						.where(new And<Table>(){{
							with(Table.Key).is("AAA");
						}})
						.buildSelectStatement());
		System.out.println(
				database.query(Table.class)
						.where(new And<Table>(){{
							with(Table.Key).is(null);
						}})
						.buildSelectStatement());
		System.out.println(
				database.query(Table.class)
						.where(Table.Key).is("AAA")
						.query().buildSelectStatement());
		System.out.println(
				database.query(Table.class)
						.where(new And<Table>(){{
							with(Table.Key).is("AAA");
							with(Table.Value).isNotNull();
						}})
						.buildSelectStatement());
		System.out.println(
				database.query(Table.class)
						.where(new And<Table>() {{
							with(Table.Key).is("AAA");
							with(Table.Value).isNotNull();
							with(new Or<Table>() {{
							}});
						}})
						.buildSelectStatement());
		System.out.println(
				database.query(Table.class)
						.where(new And<Table>() {{
							with(Table.Key).is("AAA");
							with(Table.Value).isNotNull();
							with(new Or<Table>() {{
								with(Table.Value).is("ZZ");
								with(Table.Value).is("YY");
							}});
						}})
						.sort(new OrderBy<Table>() {{
							ascending(Table.CreatedAt);
							descending(Table.UpdatedAt);
						}})
						.buildSelectStatement());
	}

}

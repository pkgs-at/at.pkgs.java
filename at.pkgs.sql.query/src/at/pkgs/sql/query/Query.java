package at.pkgs.sql.query;

public class Query<TableType> implements Criterion.Parent<TableType> {

	private final Database database;

	private final TableDefinition<TableType> table;

	private Criterion<TableType> where;

	private Database.OrderBy<TableType> order;

	Query(Database database, TableDefinition<TableType> table) {
		this.database = database;
		this.table = table;
	}

	Query<TableType> where(Criterion<TableType> criterion) {
		this.where = criterion;
		return this;
	}

	public Query<TableType> where(Criteria<TableType> criterion) {
		return this.where((Criterion<TableType>)criterion);
	}

	public Expression<TableType> where(TableType column) {
		return new Expression<TableType>(this, column);
	}

	public Query<TableType> sort(Database.OrderBy<TableType> order) {
		this.order = order;
		return this;
	}

	void buildWhereClause(QueryBuilder<TableType> builder) {
		if (this.where == null) return;
		this.where.build(builder, true);
	}

	void buildOrderByClause(QueryBuilder<TableType> builder) {
		if (this.order == null) return;
		this.order.build(builder);
	}

	public String buildSelectStatement() {
		QueryBuilder<TableType> builder;

		builder = new QueryBuilder<TableType>(
				this.database.getDialect(),
				this.table);
		this.buildWhereClause(builder);
		this.buildOrderByClause(builder);
		return builder.toString();
	}

}

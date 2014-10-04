package at.pkgs.sql.query;

public abstract class Criterion<TableType> {

	static interface Parent<TableType> {

		// nothing

	}

	private final Parent<TableType> parent;

	Criterion(Parent<TableType> parent) {
		this.parent = parent;
		if (parent instanceof Query)
			((Query<TableType>)parent).where(this);
		if (parent instanceof Criteria)
			((Criteria<TableType>)parent).add(this);
	}

	public Query<TableType> query() {
		if (!(this.parent instanceof Query))
			throw new UnsupportedOperationException(
					"unsupported in this context");
		return (Query<TableType>)this.parent;
	}

	abstract boolean isEmpty();

	abstract void build(QueryBuilder<TableType> builder, boolean where);

}

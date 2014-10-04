package at.pkgs.sql.query;

import java.util.List;
import java.util.ArrayList;

public abstract class Criteria<TableType>
extends Criterion<TableType>
implements Criterion.Parent<TableType> {

	private final List<Criterion<TableType>> children; 

	protected Criteria() {
		super(null);
		this.children = new ArrayList<Criterion<TableType>>();
	}

	abstract void appendJoint(QueryBuilder<TableType> builder);

	void add(Criterion<TableType> child) {
		if (child.isEmpty()) return;
		this.children.add(child);
	}

	protected Expression<TableType> with(TableType column) {
		return new Expression<TableType>(this, column);
	}

	protected void with(Criteria<TableType> criteria) {
		this.add(criteria);
	}

	@Override
	boolean isEmpty() {
		return this.children.size() <= 0;
	}

	@Override
	void build(QueryBuilder<TableType> builder, boolean where) {
		if (where) {
			boolean first;

			if (this.isEmpty()) return;
			first = true;
			builder.append(" WHERE ");
			for (Criterion<TableType> child : this.children) {
				if (first) first = false;
				else this.appendJoint(builder);
				child.build(builder, false);
			}
		}
		else if (this.children.size() <= 1) {
			this.children.get(0).build(builder, false);
		}
		else {
			boolean first;

			first = true;
			builder.append('(');
			for (Criterion<TableType> child : this.children) {
				if (first) first = false;
				else this.appendJoint(builder);
				child.build(builder, false);
			}
			builder.append(')');
		}
	}

}

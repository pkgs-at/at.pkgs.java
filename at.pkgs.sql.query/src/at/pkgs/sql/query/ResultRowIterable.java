package at.pkgs.sql.query;

import java.util.Iterator;
import java.sql.ResultSet;
import java.sql.SQLException;

class ResultRowIterable implements Iterable<Object> {

	private final ResultSet result;

	private final int length;

	ResultRowIterable(ResultSet result, int length) {
		this.result = result;
		this.length = length;
	}

	@Override
	public Iterator<Object> iterator() {
		return new Iterator<Object>() {

			private int index;

			{
				index = 0;
			}

			@Override
			public boolean hasNext() {
				return this.index < ResultRowIterable.this.length;
			}

			@Override
			public Object next() {
				try {
					return ResultRowIterable.this.result.getObject(
							++ this.index);
				}
				catch (SQLException throwable) {
					throw new Database.Exception(throwable);
				}
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}

}

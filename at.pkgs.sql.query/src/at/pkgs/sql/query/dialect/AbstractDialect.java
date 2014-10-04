package at.pkgs.sql.query.dialect;

public abstract class AbstractDialect implements Dialect {

	protected char getIdentifierOpenCharactor() {
		return '"';
	}

	protected char getIdentifierCloseCharactor() {
		return '"';
	}

	@Override
	public void appendIdentifier(StringBuilder builder, String name) {
		char close;
		int length;
		int index;

		builder.append(this.getIdentifierOpenCharactor());
		close = this.getIdentifierCloseCharactor();
		length = name.length();
		for (index = 0; index < length; index ++) {
			char charactor;

			charactor = name.charAt(index);
			if (charactor == close) builder.append(close);
			builder.append(charactor);
		}
		builder.append(close);
	}

}

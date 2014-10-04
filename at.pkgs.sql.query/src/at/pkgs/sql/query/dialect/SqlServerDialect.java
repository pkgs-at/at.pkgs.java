package at.pkgs.sql.query.dialect;

public class SqlServerDialect extends AbstractDialect {

	@Override
	protected char getIdentifierOpenCharactor() {
		return '[';
	}

	@Override
	protected char getIdentifierCloseCharactor() {
		return ']';
	}

}

package at.pkgs.sql.query.dialect;

public interface Dialect {

	public void appendIdentifier(StringBuilder builder, String name);

}

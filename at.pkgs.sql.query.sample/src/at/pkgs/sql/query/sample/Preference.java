package at.pkgs.sql.query.sample;

import java.sql.Timestamp;
import at.pkgs.sql.query.Database;

public class Preference {

	@Database.Table(schema = "dbo", name = "t_preference")
	public static enum Table {

		@Database.Column(name = "key")
		Key,

		@Database.Column(name = "value")
		Value,

		@Database.Column(name = "created_at")
		CreatedAt,

		@Database.Column(name = "updated_at")
		UpdatedAt;

	}

	public static class Query extends Database.Query<Table, Preference> {

		protected Query() {
			super(Table.class, Preference.class);
		}

	}

	private String key;

	private String value;

	private Timestamp createdAt;

	private Timestamp updatedAt;

	@Override
	public String toString() {
		StringBuilder builder;

		builder = new StringBuilder("[Preference]");
		builder.append(" key: ").append(this.key);
		builder.append(" value: ").append(this.value);
		builder.append(" created_at: ").append(this.createdAt);
		builder.append(" updated_at: ").append(this.updatedAt);
		return builder.toString();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}

}

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

	private String key;

	private String value;

	private Timestamp createdAt;

	private Timestamp updatedAt;

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

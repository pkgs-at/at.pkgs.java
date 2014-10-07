package at.pkgs.sql.query.sample;

import java.sql.Timestamp;
import at.pkgs.sql.query.Database;

public class Preference {

	@Database.Table(schema = "dbo", name = "t_preference")
	public static enum Table {

		@Database.Column(
				name = "preference_id",
				primaryKey = true,
				insertWith = Database.ColumnValue.DefaultValue,
				updateWith = Database.ColumnValue.KeepOriginal,
				returning = true)
		PreferenceId,

		@Database.Column(
				name = "key")
		Key,

		@Database.Column(
				name = "value")
		Value,

		@Database.Column(
				name = "created_at",
				insertWith = Database.ColumnValue.CurrentTimestamp,
				updateWith = Database.ColumnValue.KeepOriginal,
				returning = true)
		CreatedAt,

		@Database.Column(
				name = "updated_at",
				insertWith = Database.ColumnValue.CurrentTimestamp,
				updateWith = Database.ColumnValue.CurrentTimestamp,
				returning = true)
		UpdatedAt;

	}

	public static class Query extends Database.Query<Table, Preference> {

		protected Query() {
			super(Table.class, Preference.class);
		}

	}

	private int preferenceId;

	private String key;

	private String value;

	private Timestamp createdAt;

	private Timestamp updatedAt;

	public Preference() {
		// do nothing
	}

	public Preference(String key, String value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuilder builder;

		builder = new StringBuilder("[Preference]");
		builder.append(" preference_id: ").append(this.preferenceId);
		builder.append(" key: ").append(this.key);
		builder.append(" value: ").append(this.value);
		builder.append(" created_at: ").append(this.createdAt);
		builder.append(" updated_at: ").append(this.updatedAt);
		return builder.toString();
	}

	public int getPreferenceId() {
		return preferenceId;
	}

	public void setPreferenceId(int preferenceId) {
		this.preferenceId = preferenceId;
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

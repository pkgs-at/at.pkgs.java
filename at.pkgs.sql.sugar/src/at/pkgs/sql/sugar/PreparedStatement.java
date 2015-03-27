/*
 * Copyright (c) 2009-2015, Architector Inc., Japan
 * All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package at.pkgs.sql.sugar;

import java.math.BigDecimal;
import java.util.Calendar;
import java.net.URL;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Time;
import java.sql.SQLException;

public class PreparedStatement
extends Statement
implements java.sql.PreparedStatement {

	public PreparedStatement(
			Connection connection,
			java.sql.PreparedStatement wrapped) {
		super(connection, wrapped);
	}

	public java.sql.PreparedStatement unwrap() {
		return (java.sql.PreparedStatement)super.unwrap();
	}

	@Override
	public ResultSetMetaData getMetaData() {
		try {
			return this.wrap(
					this.unwrap().getMetaData());
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public ParameterMetaData getParameterMetaData() {
		try {
			return this.wrap(
					this.unwrap().getParameterMetaData());
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void addBatch() {
		try {
			this.unwrap().addBatch();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean execute() {
		try {
			return this.unwrap().execute();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int executeUpdate() {
		try {
			return this.unwrap().executeUpdate();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public ResultSet executeQuery() {
		try {
			return this.wrap(
					this.unwrap().executeQuery());
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void clearParameters() {
		try {
			this.unwrap().clearParameters();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setObject(
			int parameterIndex,
			Object value,
			int targetSqlType,
			int scaleOrLength) {
		try {
			this.unwrap().setObject(
					parameterIndex,
					value,
					targetSqlType,
					scaleOrLength);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setObject(
			int parameterIndex,
			Object value,
			int targetSqlType) {
		try {
			this.unwrap().setObject(
					parameterIndex,
					value,
					targetSqlType);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setObject(
			int parameterIndex,
			Object value) {
		try {
			this.unwrap().setObject(
					parameterIndex,
					value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setRowId(int parameterIndex, java.sql.RowId  value) {
		try {
			this.unwrap().setRowId(parameterIndex, this.unwrap(value));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setRef(int parameterIndex, java.sql.Ref value) {
		try {
			this.unwrap().setRef(parameterIndex, this.unwrap(value));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setBoolean(int parameterIndex, boolean value) {
		try {
			this.unwrap().setBoolean(parameterIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setByte(int parameterIndex, byte value) {
		try {
			this.unwrap().setByte(parameterIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setShort(int parameterIndex, short value) {
		try {
			this.unwrap().setShort(parameterIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setInt(int parameterIndex, int value) {
		try {
			this.unwrap().setInt(parameterIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setLong(int parameterIndex, long value) {
		try {
			this.unwrap().setLong(parameterIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setFloat(int parameterIndex, float value) {
		try {
			this.unwrap().setFloat(parameterIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setDouble(int parameterIndex, double value) {
		try {
			this.unwrap().setDouble(parameterIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setBigDecimal(int parameterIndex, BigDecimal value) {
		try {
			this.unwrap().setBigDecimal(parameterIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setTimestamp(
			int parameterIndex,
			Timestamp value,
			Calendar calendar) {
		try {
			this.unwrap().setTimestamp(parameterIndex, value, calendar);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setTimestamp(
			int parameterIndex,
			Timestamp value) {
		try {
			this.unwrap().setTimestamp(parameterIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setDate(
			int parameterIndex,
			Date value,
			Calendar calendar) {
		try {
			this.unwrap().setDate(parameterIndex, value, calendar);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setDate(
			int parameterIndex,
			Date value) {
		try {
			this.unwrap().setDate(parameterIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setTime(
			int parameterIndex,
			Time value,
			Calendar calendar) {
		try {
			this.unwrap().setTime(parameterIndex, value, calendar);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setTime(
			int parameterIndex,
			Time value) {
		try {
			this.unwrap().setTime(parameterIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setBytes(int parameterIndex, byte[] value) {
		try {
			this.unwrap().setBytes(parameterIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setString(int parameterIndex, String value) {
		try {
			this.unwrap().setString(parameterIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setNString(int parameterIndex, String value) {
		try {
			this.unwrap().setNString(parameterIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setURL(int parameterIndex, URL value) {
		try {
			this.unwrap().setURL(parameterIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setBinaryStream(
			int parameterIndex,
			InputStream value,
			int length) {
		try {
			this.unwrap().setBinaryStream(parameterIndex, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setBinaryStream(
			int parameterIndex,
			InputStream value,
			long length) {
		try {
			this.unwrap().setBinaryStream(parameterIndex, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setBinaryStream(
			int parameterIndex,
			InputStream value) {
		try {
			this.unwrap().setBinaryStream(parameterIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setAsciiStream(
			int parameterIndex,
			InputStream value,
			int length) {
		try {
			this.unwrap().setAsciiStream(parameterIndex, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setAsciiStream(
			int parameterIndex,
			InputStream value,
			long length) {
		try {
			this.unwrap().setAsciiStream(parameterIndex, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setAsciiStream(
			int parameterIndex,
			InputStream value) {
		try {
			this.unwrap().setAsciiStream(parameterIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	@Deprecated
	public void setUnicodeStream(
			int parameterIndex,
			InputStream value,
			int length) {
		try {
			this.unwrap().setUnicodeStream(parameterIndex, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setCharacterStream(
			int parameterIndex,
			Reader value,
			int length) {
		try {
			this.unwrap().setCharacterStream(parameterIndex, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setCharacterStream(
			int parameterIndex,
			Reader value,
			long length) {
		try {
			this.unwrap().setCharacterStream(parameterIndex, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setCharacterStream(
			int parameterIndex,
			Reader value) {
		try {
			this.unwrap().setCharacterStream(parameterIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setNCharacterStream(
			int parameterIndex,
			Reader value,
			long length) {
		try {
			this.unwrap().setNCharacterStream(parameterIndex, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setNCharacterStream(
			int parameterIndex,
			Reader value) {
		try {
			this.unwrap().setNCharacterStream(parameterIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setArray(int parameterIndex, java.sql.Array value) {
		try {
			this.unwrap().setArray(parameterIndex, this.unwrap(value));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setBlob(int parameterIndex, java.sql.Blob value) {
		try {
			this.unwrap().setBlob(parameterIndex, this.unwrap(value));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setBlob(
			int parameterIndex,
			InputStream value,
			long length) {
		try {
			this.unwrap().setBlob(parameterIndex, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setBlob(
			int parameterIndex,
			InputStream value) {
		try {
			this.unwrap().setBlob(parameterIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setClob(int parameterIndex, java.sql.Clob value) {
		try {
			this.unwrap().setClob(parameterIndex, this.unwrap(value));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setClob(
			int parameterIndex,
			Reader value,
			long length) {
		try {
			this.unwrap().setClob(parameterIndex, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setClob(
			int parameterIndex,
			Reader value) {
		try {
			this.unwrap().setClob(parameterIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setNClob(int parameterIndex, java.sql.NClob value) {
		try {
			this.unwrap().setNClob(parameterIndex, this.unwrap(value));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setNClob(
			int parameterIndex,
			Reader value,
			long length) {
		try {
			this.unwrap().setNClob(parameterIndex, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setNClob(
			int parameterIndex,
			Reader value) {
		try {
			this.unwrap().setNClob(parameterIndex, value);;
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setSQLXML(int parameterIndex, java.sql.SQLXML value) {
		try {
			this.unwrap().setSQLXML(parameterIndex, this.unwrap(value));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setNull(int parameterIndex, int sqlType, String typeName) {
		try {
			this.unwrap().setNull(parameterIndex, sqlType, typeName);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setNull(int parameterIndex, int sqlType) {
		try {
			this.unwrap().setNull(parameterIndex, sqlType);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

}

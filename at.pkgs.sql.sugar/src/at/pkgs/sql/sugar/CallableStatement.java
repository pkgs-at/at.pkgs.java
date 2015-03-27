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
import java.util.Map;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Time;
import java.sql.SQLException;

public class CallableStatement
extends PreparedStatement
implements java.sql.CallableStatement {

	public CallableStatement(
			Connection connection,
			java.sql.CallableStatement wrapped) {
		super(connection, wrapped);
	}

	@Override
	public java.sql.CallableStatement unwrap() {
		return (java.sql.CallableStatement)super.unwrap();
	}

	@Override
	public void registerOutParameter(
			int parameterIndex,
			int sqlType,
			int scale) {
		try {
			this.unwrap().registerOutParameter(
					parameterIndex,
					sqlType,
					scale);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void registerOutParameter(
			int parameterIndex,
			int sqlType,
			String typeName) {
		try {
			this.unwrap().registerOutParameter(
					parameterIndex,
					sqlType,
					typeName);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void registerOutParameter(
			int parameterIndex,
			int sqlType) {
		try {
			this.unwrap().registerOutParameter(
					parameterIndex,
					sqlType);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void registerOutParameter(
			String parameterName,
			int sqlType,
			int scale) {
		try {
			this.unwrap().registerOutParameter(
					parameterName,
					sqlType,
					scale);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void registerOutParameter(
			String parameterName,
			int sqlType,
			String typeName) {
		try {
			this.unwrap().registerOutParameter(
					parameterName,
					sqlType,
					typeName);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void registerOutParameter(
			String parameterName,
			int sqlType) {
		try {
			this.unwrap().registerOutParameter(
					parameterName,
					sqlType);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Object getObject(
			int parameterIndex,
			Map<String, Class<?>> map) {
		try {
			return this.unwrap().getObject(parameterIndex, map);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Object getObject(
			int parameterIndex) {
		try {
			return this.unwrap().getObject(parameterIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Object getObject(
			String parameterName,
			Map<String, Class<?>> map) {
		try {
			return this.unwrap().getObject(parameterName, map);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Object getObject(
			String parameterName) {
		try {
			return this.unwrap().getObject(parameterName);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setObject(
			String parameterName,
			Object value,
			int targetSqlType,
			int scale) {
		try {
			this.unwrap().setObject(
					parameterName,
					value,
					targetSqlType,
					scale);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setObject(
			String parameterName,
			Object value,
			int targetSqlType) {
		try {
			this.unwrap().setObject(
					parameterName,
					value,
					targetSqlType);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setObject(
			String parameterName,
			Object value) {
		try {
			this.unwrap().setObject(
					parameterName,
					value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public RowId getRowId(int parameterIndex) {
		try {
			return this.wrap(
					this.unwrap().getRowId(parameterIndex));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public RowId getRowId(String parameterName) {
		try {
			return this.wrap(
					this.unwrap().getRowId(parameterName));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setRowId(String parameterName, java.sql.RowId value) {
		try {
			this.unwrap().setRowId(parameterName, this.unwrap(value));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Ref getRef(int parameterIndex) {
		try {
			return this.wrap(
					this.unwrap().getRef(parameterIndex));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Ref getRef(String parameterName) {
		try {
			return this.wrap(
					this.unwrap().getRef(parameterName));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean getBoolean(int parameterIndex) {
		try {
			return this.unwrap().getBoolean(parameterIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean getBoolean(String parameterName) {
		try {
			return this.unwrap().getBoolean(parameterName);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setBoolean(String parameterName, boolean value) {
		try {
			this.unwrap().setBoolean(parameterName, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public byte getByte(int parameterIndex) {
		try {
			return this.unwrap().getByte(parameterIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public byte getByte(String parameterName) {
		try {
			return this.unwrap().getByte(parameterName);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setByte(String parameterName, byte value) {
		try {
			this.unwrap().setByte(parameterName, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public short getShort(int parameterIndex) {
		try {
			return this.unwrap().getShort(parameterIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public short getShort(String parameterName) {
		try {
			return this.unwrap().getShort(parameterName);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setShort(String parameterName, short value) {
		try {
			this.unwrap().setShort(parameterName, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int getInt(int parameterIndex) {
		try {
			return this.unwrap().getInt(parameterIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int getInt(String parameterName) {
		try {
			return this.unwrap().getInt(parameterName);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setInt(String parameterName, int value) {
		try {
			this.unwrap().setInt(parameterName, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public long getLong(int parameterIndex) {
		try {
			return this.unwrap().getLong(parameterIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public long getLong(String parameterName) {
		try {
			return this.unwrap().getLong(parameterName);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setLong(String parameterName, long value) {
		try {
			this.unwrap().setLong(parameterName, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public float getFloat(int parameterIndex) {
		try {
			return this.unwrap().getFloat(parameterIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public float getFloat(String parameterName) {
		try {
			return this.unwrap().getFloat(parameterName);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setFloat(String parameterName, float value) {
		try {
			this.unwrap().setFloat(parameterName, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public double getDouble(int parameterIndex) {
		try {
			return this.unwrap().getDouble(parameterIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public double getDouble(String parameterName) {
		try {
			return this.unwrap().getDouble(parameterName);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setDouble(String parameterName, double value) {
		try {
			this.unwrap().setDouble(parameterName, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	@Deprecated
	public BigDecimal getBigDecimal(int parameterIndex, int scale) {
		try {
			return this.unwrap().getBigDecimal(parameterIndex, scale);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public BigDecimal getBigDecimal(int parameterIndex) {
		try {
			return this.unwrap().getBigDecimal(parameterIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public BigDecimal getBigDecimal(String parameterName) {
		try {
			return this.unwrap().getBigDecimal(parameterName);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setBigDecimal(String parameterName, BigDecimal value) {
		try {
			this.unwrap().setBigDecimal(parameterName, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Timestamp getTimestamp(int parameterIndex, Calendar calendar) {
		try {
			return this.unwrap().getTimestamp(parameterIndex, calendar);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Timestamp getTimestamp(int parameterIndex) {
		try {
			return this.unwrap().getTimestamp(parameterIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Timestamp getTimestamp(String parameterName, Calendar calendar) {
		try {
			return this.unwrap().getTimestamp(parameterName, calendar);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Timestamp getTimestamp(String parameterName) {
		try {
			return this.unwrap().getTimestamp(parameterName);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setTimestamp(
			String parameterName,
			Timestamp value,
			Calendar calendar) {
		try {
			this.unwrap().setTimestamp(parameterName, value, calendar);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setTimestamp(
			String parameterName,
			Timestamp value) {
		try {
			this.unwrap().setTimestamp(parameterName, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Date getDate(int parameterIndex, Calendar calendar) {
		try {
			return this.unwrap().getDate(parameterIndex, calendar);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Date getDate(int parameterIndex) {
		try {
			return this.unwrap().getDate(parameterIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Date getDate(String parameterName, Calendar calendar) {
		try {
			return this.unwrap().getDate(parameterName, calendar);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Date getDate(String parameterName) {
		try {
			return this.unwrap().getDate(parameterName);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setDate(
			String parameterName,
			Date value,
			Calendar calendar) {
		try {
			this.unwrap().setDate(parameterName, value, calendar);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setDate(
			String parameterName,
			Date value) {
		try {
			this.unwrap().setDate(parameterName, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Time getTime(int parameterIndex, Calendar calendar) {
		try {
			return this.unwrap().getTime(parameterIndex, calendar);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Time getTime(int parameterIndex) {
		try {
			return this.unwrap().getTime(parameterIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Time getTime(String parameterName, Calendar calendar) {
		try {
			return this.unwrap().getTime(parameterName, calendar);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Time getTime(String parameterName) {
		try {
			return this.unwrap().getTime(parameterName);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setTime(
			String parameterName,
			Time value,
			Calendar calendar) {
		try {
			this.unwrap().setTime(parameterName, value, calendar);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setTime(
			String parameterName,
			Time value) {
		try {
			this.unwrap().setTime(parameterName, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public byte[] getBytes(int parameterIndex) {
		try {
			return this.unwrap().getBytes(parameterIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public byte[] getBytes(String parameterName) {
		try {
			return this.unwrap().getBytes(parameterName);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setBytes(String parameterName, byte[] value) {
		try {
			this.unwrap().setBytes(parameterName, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public String getString(int parameterIndex) {
		try {
			return this.unwrap().getString(parameterIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public String getString(String parameterName) {
		try {
			return this.unwrap().getString(parameterName);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setString(String parameterName, String value) {
		try {
			this.unwrap().setString(parameterName, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public String getNString(int parameterIndex) {
		try {
			return this.unwrap().getNString(parameterIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public String getNString(String parameterName) {
		try {
			return this.unwrap().getNString(parameterName);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setNString(String parameterName, String value) {
		try {
			this.unwrap().setNString(parameterName, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public URL getURL(int parameterIndex) {
		try {
			return this.unwrap().getURL(parameterIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public URL getURL(String parameterName) {
		try {
			return this.unwrap().getURL(parameterName);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setURL(String parameterName, URL value) {
		try {
			this.unwrap().setURL(parameterName, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setBinaryStream(
			String parameterName,
			InputStream value,
			int length) {
		try {
			this.unwrap().setBinaryStream(parameterName, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setBinaryStream(
			String parameterName,
			InputStream value,
			long length) {
		try {
			this.unwrap().setBinaryStream(parameterName, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setBinaryStream(String parameterName, InputStream value) {
		try {
			this.unwrap().setBinaryStream(parameterName, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setAsciiStream(
			String parameterName,
			InputStream value,
			int length) {
		try {
			this.unwrap().setAsciiStream(parameterName, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setAsciiStream(
			String parameterName,
			InputStream value,
			long length) {
		try {
			this.unwrap().setAsciiStream(parameterName, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setAsciiStream(String parameterName, InputStream value) {
		try {
			this.unwrap().setAsciiStream(parameterName, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Reader getCharacterStream(int parameterIndex) {
		try {
			return this.unwrap().getCharacterStream(parameterIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Reader getCharacterStream(String parameterName) {
		try {
			return this.unwrap().getCharacterStream(parameterName);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setCharacterStream(
			String parameterName,
			Reader value,
			int length) {
		try {
			this.unwrap().setCharacterStream(parameterName, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setCharacterStream(
			String parameterName,
			Reader value,
			long length) {
		try {
			this.unwrap().setCharacterStream(parameterName, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setCharacterStream(String parameterName, Reader value) {
		try {
			this.unwrap().setCharacterStream(parameterName, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Reader getNCharacterStream(int parameterIndex) {
		try {
			return this.unwrap().getNCharacterStream(parameterIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Reader getNCharacterStream(String parameterName) {
		try {
			return this.unwrap().getNCharacterStream(parameterName);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setNCharacterStream(
			String parameterName,
			Reader value,
			long length) {
		try {
			this.unwrap().setNCharacterStream(parameterName, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setNCharacterStream(String parameterName, Reader value) {
		try {
			this.unwrap().setNCharacterStream(parameterName, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Array getArray(int parameterIndex) {
		try {
			return this.wrap(
					this.unwrap().getArray(parameterIndex));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Array getArray(String parameterName) {
		try {
			return this.wrap(
					this.unwrap().getArray(parameterName));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Blob getBlob(int parameterIndex) {
		try {
			return this.wrap(
					this.unwrap().getBlob(parameterIndex));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Blob getBlob(String parameterName) {
		try {
			return this.wrap(
					this.unwrap().getBlob(parameterName));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setBlob(String parameterName, java.sql.Blob value) {
		try {
			this.unwrap().setBlob(parameterName, this.unwrap(value));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setBlob(
			String parameterName,
			InputStream value,
			long length) {
		try {
			this.unwrap().setBlob(parameterName, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setBlob(String parameterName, InputStream value) {
		try {
			this.unwrap().setBlob(parameterName, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Clob getClob(int parameterIndex) {
		try {
			return this.wrap(
					this.unwrap().getClob(parameterIndex));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Clob getClob(String parameterName) {
		try {
			return this.wrap(
					this.unwrap().getClob(parameterName));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setClob(String parameterName, java.sql.Clob value) {
		try {
			this.unwrap().setClob(parameterName, this.unwrap(value));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setClob(
			String parameterName,
			Reader value,
			long length) {
		try {
			this.unwrap().setClob(parameterName, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setClob(String parameterName, Reader value) {
		try {
			this.unwrap().setClob(parameterName, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public NClob getNClob(int parameterIndex) {
		try {
			return this.wrap(
					this.unwrap().getNClob(parameterIndex));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public NClob getNClob(String parameterName) {
		try {
			return this.wrap(
					this.unwrap().getNClob(parameterName));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setNClob(String parameterName, java.sql.NClob value) {
		try {
			this.unwrap().setNClob(parameterName, this.unwrap(value));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setNClob(
			String parameterName,
			Reader value,
			long length) {
		try {
			this.unwrap().setNClob(parameterName, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setNClob(String parameterName, Reader value) {
		try {
			this.unwrap().setNClob(parameterName, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public SQLXML getSQLXML(int parameterIndex) {
		try {
			return this.wrap(
					this.unwrap().getSQLXML(parameterIndex));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public SQLXML getSQLXML(String parameterName) {
		try {
			return this.wrap(
					this.unwrap().getSQLXML(parameterName));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setSQLXML(String parameterName, java.sql.SQLXML value) {
		try {
			this.unwrap().setSQLXML(parameterName, this.unwrap(value));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean wasNull() {
		try {
			return this.unwrap().wasNull();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setNull(String parameterName, int sqlType, String typeName) {
		try {
			this.unwrap().setNull(parameterName, sqlType, typeName);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setNull(String parameterName, int sqlType) {
		try {
			this.unwrap().setNull(parameterName, sqlType);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

}

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
import java.sql.SQLWarning;

public class ResultSet
extends DatabaseObject<java.sql.ResultSet>
implements java.sql.ResultSet {

	public ResultSet(Connection connection, java.sql.ResultSet wrapped) {
		super(connection, wrapped);
	}

	@Override
	public Statement getStatement() throws SQLException {
		try {
			return this.wrap(
					this.unwrap().getStatement());
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public SQLWarning getWarnings() {
		try {
			return this.unwrap().getWarnings();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void clearWarnings() {
		try {
			this.unwrap().clearWarnings();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public String getCursorName() {
		try {
			return this.unwrap().getCursorName();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		try {
			return this.wrap(
					this.unwrap().getMetaData());
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int getType() throws SQLException {
		try {
			return this.unwrap().getType();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int getConcurrency() throws SQLException {
		try {
			return this.unwrap().getConcurrency();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int getHoldability() throws SQLException {
		try {
			return this.unwrap().getHoldability();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int getFetchDirection() {
		try {
			return this.unwrap().getFetchDirection();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setFetchDirection(int value) {
		try {
			this.unwrap().setFetchDirection(value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int getFetchSize() {
		try {
			return this.unwrap().getFetchSize();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setFetchSize(int value) {
		try {
			this.unwrap().setFetchSize(value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int getRow() throws SQLException {
		try {
			return this.unwrap().getRow();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean isBeforeFirst() {
		try {
			return this.unwrap().isBeforeFirst();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean isAfterLast() {
		try {
			return this.unwrap().isAfterLast();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean isFirst() {
		try {
			return this.unwrap().isFirst();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean isLast() {
		try {
			return this.unwrap().isLast();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void beforeFirst() {
		try {
			this.unwrap().beforeFirst();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void afterLast() {
		try {
			this.unwrap().afterLast();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean first() {
		try {
			return this.unwrap().first();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean last() {
		try {
			return this.unwrap().last();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean absolute(int row) {
		try {
			return this.unwrap().absolute(row);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean relative(int rows) {
		try {
			return this.unwrap().relative(rows);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean next() {
		try {
			return this.unwrap().next();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean previous() {
		try {
			return this.unwrap().previous();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean rowInserted() {
		try {
			return this.unwrap().rowInserted();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean rowUpdated() {
		try {
			return this.unwrap().rowUpdated();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean rowDeleted() {
		try {
			return this.unwrap().rowDeleted();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void insertRow() {
		try {
			this.unwrap().insertRow();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateRow() {
		try {
			this.unwrap().updateRow();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void deleteRow() {
		try {
			this.unwrap().deleteRow();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void refreshRow() {
		try {
			this.unwrap().refreshRow();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void cancelRowUpdates() {
		try {
			this.unwrap().cancelRowUpdates();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void moveToInsertRow() {
		try {
			this.unwrap().moveToInsertRow();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void moveToCurrentRow() {
		try {
			this.unwrap().moveToCurrentRow();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean isClosed() {
		try {
			return this.unwrap().isClosed();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void close() {
		try {
			this.unwrap().close();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int findColumn(String columnLabel) {
		try {
			return this.unwrap().findColumn(columnLabel);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Object getObject(int columnIndex, Map<String, Class<?>> map) {
		try {
			return this.unwrap().getObject(columnIndex, map);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Object getObject(int columnIndex) {
		try {
			return this.unwrap().getObject(columnIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Object getObject(String columnLabel, Map<String, Class<?>> map) {
		try {
			return this.unwrap().getObject(columnLabel, map);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Object getObject(String columnLabel) {
		try {
			return this.unwrap().getObject(columnLabel);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateObject(int columnIndex, Object value, int size) {
		try {
			this.unwrap().updateObject(columnIndex, value, size);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateObject(int columnIndex, Object value) {
		try {
			this.unwrap().updateObject(columnIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateObject(String columnLabel, Object value, int size) {
		try {
			this.unwrap().updateObject(columnLabel, value, size);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateObject(String columnLabel, Object value) {
		try {
			this.unwrap().updateObject(columnLabel, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public RowId getRowId(int columnIndex) {
		try {
			return this.wrap(
					this.unwrap().getRowId(columnIndex));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public RowId getRowId(String columnLabel) {
		try {
			return this.wrap(
					this.unwrap().getRowId(columnLabel));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateRowId(int columnIndex, java.sql.RowId value) {
		try {
			this.unwrap().updateRowId(columnIndex, this.unwrap(value));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateRowId(String columnLabel, java.sql.RowId value) {
		try {
			this.unwrap().updateRowId(columnLabel, this.unwrap(value));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Ref getRef(int columnIndex) {
		try {
			return this.wrap(
					this.unwrap().getRef(columnIndex));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Ref getRef(String columnLabel) {
		try {
			return this.wrap(
					this.unwrap().getRef(columnLabel));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateRef(int columnIndex, java.sql.Ref value) {
		try {
			this.unwrap().updateRef(columnIndex, this.unwrap(value));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateRef(String columnLabel, java.sql.Ref value) {
		try {
			this.unwrap().updateRef(columnLabel, this.unwrap(value));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean getBoolean(int columnIndex) {
		try {
			return this.unwrap().getBoolean(columnIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean getBoolean(String columnLabel) {
		try {
			return this.unwrap().getBoolean(columnLabel);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateBoolean(int columnIndex, boolean value) {
		try {
			this.unwrap().updateBoolean(columnIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateBoolean(String columnLabel, boolean value) {
		try {
			this.unwrap().updateBoolean(columnLabel, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public byte getByte(int columnIndex) {
		try {
			return this.unwrap().getByte(columnIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public byte getByte(String columnLabel) {
		try {
			return this.unwrap().getByte(columnLabel);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateByte(int columnIndex, byte value) {
		try {
			this.unwrap().updateByte(columnIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateByte(String columnLabel, byte value) {
		try {
			this.unwrap().updateByte(columnLabel, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public short getShort(int columnIndex) {
		try {
			return this.unwrap().getShort(columnIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public short getShort(String columnLabel) {
		try {
			return this.unwrap().getShort(columnLabel);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateShort(int columnIndex, short value) {
		try {
			this.unwrap().updateShort(columnIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateShort(String columnLabel, short value) {
		try {
			this.unwrap().updateShort(columnLabel, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int getInt(int columnIndex) {
		try {
			return this.unwrap().getInt(columnIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int getInt(String columnLabel) {
		try {
			return this.unwrap().getInt(columnLabel);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateInt(int columnIndex, int value) {
		try {
			this.unwrap().updateInt(columnIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateInt(String columnLabel, int value) {
		try {
			this.unwrap().updateInt(columnLabel, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public long getLong(int columnIndex) {
		try {
			return this.unwrap().getLong(columnIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public long getLong(String columnLabel) {
		try {
			return this.unwrap().getLong(columnLabel);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateLong(int columnIndex, long value) {
		try {
			this.unwrap().updateLong(columnIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateLong(String columnLabel, long value) {
		try {
			this.unwrap().updateLong(columnLabel, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public float getFloat(int columnIndex) {
		try {
			return this.unwrap().getFloat(columnIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public float getFloat(String columnLabel) {
		try {
			return this.unwrap().getFloat(columnLabel);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateFloat(int columnIndex, float value) {
		try {
			this.unwrap().updateFloat(columnIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateFloat(String columnLabel, float value) {
		try {
			this.unwrap().updateFloat(columnLabel, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public double getDouble(int columnIndex) {
		try {
			return this.unwrap().getDouble(columnIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public double getDouble(String columnLabel) {
		try {
			return this.unwrap().getDouble(columnLabel);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateDouble(int columnIndex, double value) {
		try {
			this.unwrap().updateDouble(columnIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateDouble(String columnLabel, double value) {
		try {
			this.unwrap().updateDouble(columnLabel, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	@Deprecated
	public BigDecimal getBigDecimal(int columnIndex, int scale) {
		try {
			return this.unwrap().getBigDecimal(columnIndex, scale);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public BigDecimal getBigDecimal(int columnIndex) {
		try {
			return this.unwrap().getBigDecimal(columnIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	@Deprecated
	public BigDecimal getBigDecimal(String columnLabel, int scale) {
		try {
			return this.unwrap().getBigDecimal(columnLabel, scale);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public BigDecimal getBigDecimal(String columnLabel) {
		try {
			return this.unwrap().getBigDecimal(columnLabel);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateBigDecimal(int columnIndex, BigDecimal value) {
		try {
			this.unwrap().updateBigDecimal(columnIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateBigDecimal(String columnLabel, BigDecimal value) {
		try {
			this.unwrap().updateBigDecimal(columnLabel, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Timestamp getTimestamp(int columnIndex, Calendar calendar) {
		try {
			return this.unwrap().getTimestamp(columnIndex, calendar);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Timestamp getTimestamp(int columnIndex) {
		try {
			return this.unwrap().getTimestamp(columnIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Timestamp getTimestamp(String columnLabel, Calendar calendar) {
		try {
			return this.unwrap().getTimestamp(columnLabel, calendar);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Timestamp getTimestamp(String columnLabel) {
		try {
			return this.unwrap().getTimestamp(columnLabel);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateTimestamp(int columnIndex, Timestamp value) {
		try {
			this.unwrap().updateTimestamp(columnIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateTimestamp(String columnLabel, Timestamp value) {
		try {
			this.unwrap().updateTimestamp(columnLabel, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Date getDate(int columnIndex, Calendar calendar) {
		try {
			return this.unwrap().getDate(columnIndex, calendar);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Date getDate(int columnIndex) {
		try {
			return this.unwrap().getDate(columnIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Date getDate(String columnLabel, Calendar calendar) {
		try {
			return this.unwrap().getDate(columnLabel, calendar);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Date getDate(String columnLabel) {
		try {
			return this.unwrap().getDate(columnLabel);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateDate(int columnIndex, Date value) {
		try {
			this.unwrap().updateDate(columnIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateDate(String columnLabel, Date value) {
		try {
			this.unwrap().updateDate(columnLabel, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Time getTime(int columnIndex, Calendar calendar) {
		try {
			return this.unwrap().getTime(columnIndex, calendar);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Time getTime(int columnIndex) {
		try {
			return this.unwrap().getTime(columnIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Time getTime(String columnLabel, Calendar calendar) {
		try {
			return this.unwrap().getTime(columnLabel, calendar);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Time getTime(String columnLabel) {
		try {
			return this.unwrap().getTime(columnLabel);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateTime(int columnIndex, Time value) {
		try {
			this.unwrap().updateTime(columnIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateTime(String columnLabel, Time value) {
		try {
			this.unwrap().updateTime(columnLabel, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public byte[] getBytes(int columnIndex) {
		try {
			return this.unwrap().getBytes(columnIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public byte[] getBytes(String columnLabel) {
		try {
			return this.unwrap().getBytes(columnLabel);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateBytes(int columnIndex, byte[] value) {
		try {
			this.unwrap().updateBytes(columnIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateBytes(String columnLabel, byte[] value) {
		try {
			this.unwrap().updateBytes(columnLabel, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public String getString(int columnIndex) {
		try {
			return this.unwrap().getString(columnIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public String getString(String columnLabel) {
		try {
			return this.unwrap().getString(columnLabel);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateString(int columnIndex, String value) {
		try {
			this.unwrap().updateString(columnIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateString(String columnLabel, String value) {
		try {
			this.unwrap().updateString(columnLabel, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public String getNString(int columnIndex) {
		try {
			return this.unwrap().getNString(columnIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public String getNString(String columnLabel) {
		try {
			return this.unwrap().getNString(columnLabel);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateNString(int columnIndex, String value) {
		try {
			this.unwrap().updateNString(columnIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateNString(String columnLabel, String value) {
		try {
			this.unwrap().updateNString(columnLabel, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public URL getURL(int columnIndex) {
		try {
			return this.unwrap().getURL(columnIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public URL getURL(String columnLabel) {
		try {
			return this.unwrap().getURL(columnLabel);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public InputStream getBinaryStream(int columnIndex) {
		try {
			return this.unwrap().getBinaryStream(columnIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public InputStream getBinaryStream(String columnLabel) {
		try {
			return this.unwrap().getBinaryStream(columnLabel);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateBinaryStream(
			int columnIndex,
			InputStream value,
			int length) {
		try {
			this.unwrap().updateBinaryStream(columnIndex, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateBinaryStream(
			int columnIndex,
			InputStream value,
			long length) {
		try {
			this.unwrap().updateBinaryStream(columnIndex, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateBinaryStream(
			int columnIndex,
			InputStream value) {
		try {
			this.unwrap().updateBinaryStream(columnIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateBinaryStream(
			String columnLabel,
			InputStream value,
			int length) {
		try {
			this.unwrap().updateBinaryStream(columnLabel, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateBinaryStream(
			String columnLabel,
			InputStream value,
			long length) {
		try {
			this.unwrap().updateBinaryStream(columnLabel, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateBinaryStream(
			String columnLabel,
			InputStream value) {
		try {
			this.unwrap().updateBinaryStream(columnLabel, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public InputStream getAsciiStream(int columnIndex) {
		try {
			return this.unwrap().getAsciiStream(columnIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public InputStream getAsciiStream(String columnLabel) {
		try {
			return this.unwrap().getAsciiStream(columnLabel);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateAsciiStream(
			int columnIndex,
			InputStream value,
			int length) {
		try {
			this.unwrap().updateAsciiStream(columnIndex, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateAsciiStream(
			int columnIndex,
			InputStream value,
			long length) {
		try {
			this.unwrap().updateAsciiStream(columnIndex, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateAsciiStream(
			int columnIndex,
			InputStream value) {
		try {
			this.unwrap().updateAsciiStream(columnIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateAsciiStream(
			String columnLabel,
			InputStream value,
			int length) {
		try {
			this.unwrap().updateAsciiStream(columnLabel, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateAsciiStream(
			String columnLabel,
			InputStream value,
			long length) {
		try {
			this.unwrap().updateAsciiStream(columnLabel, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateAsciiStream(
			String columnLabel,
			InputStream value) {
		try {
			this.unwrap().updateAsciiStream(columnLabel, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	@Deprecated
	public InputStream getUnicodeStream(int columnIndex) {
		try {
			return this.unwrap().getUnicodeStream(columnIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	@Deprecated
	public InputStream getUnicodeStream(String columnLabel) {
		try {
			return this.unwrap().getUnicodeStream(columnLabel);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Reader getCharacterStream(int columnIndex) {
		try {
			return this.unwrap().getCharacterStream(columnIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Reader getCharacterStream(String columnLabel) {
		try {
			return this.unwrap().getCharacterStream(columnLabel);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateCharacterStream(
			int columnIndex,
			Reader value,
			int length) {
		try {
			this.unwrap().updateCharacterStream(columnIndex, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateCharacterStream(
			int columnIndex,
			Reader value,
			long length) {
		try {
			this.unwrap().updateCharacterStream(columnIndex, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateCharacterStream(
			int columnIndex,
			Reader value) {
		try {
			this.unwrap().updateCharacterStream(columnIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateCharacterStream(
			String columnLabel,
			Reader value,
			int length) {
		try {
			this.unwrap().updateCharacterStream(columnLabel, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateCharacterStream(
			String columnLabel,
			Reader value,
			long length) {
		try {
			this.unwrap().updateCharacterStream(columnLabel, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateCharacterStream(
			String columnLabel,
			Reader value) {
		try {
			this.unwrap().updateCharacterStream(columnLabel, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Reader getNCharacterStream(int columnIndex) {
		try {
			return this.unwrap().getNCharacterStream(columnIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Reader getNCharacterStream(String columnLabel) {
		try {
			return this.unwrap().getNCharacterStream(columnLabel);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateNCharacterStream(
			int columnIndex,
			Reader value,
			long length) {
		try {
			this.unwrap().updateNCharacterStream(columnIndex, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateNCharacterStream(
			int columnIndex,
			Reader value) {
		try {
			this.unwrap().updateNCharacterStream(columnIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateNCharacterStream(
			String columnLabel,
			Reader value,
			long length) {
		try {
			this.unwrap().updateNCharacterStream(columnLabel, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateNCharacterStream(
			String columnLabel,
			Reader value) {
		try {
			this.unwrap().updateNCharacterStream(columnLabel, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Array getArray(int columnIndex) {
		try {
			return this.wrap(
					this.unwrap().getArray(columnIndex));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Array getArray(String columnLabel) {
		try {
			return this.wrap(
					this.unwrap().getArray(columnLabel));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateArray(int columnIndex, java.sql.Array value) {
		try {
			this.unwrap().updateArray(columnIndex, this.unwrap(value));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateArray(String columnLabel, java.sql.Array value) {
		try {
			this.unwrap().updateArray(columnLabel, this.unwrap(value));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Blob getBlob(int columnIndex) {
		try {
			return this.wrap(
					this.unwrap().getBlob(columnIndex));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Blob getBlob(String columnLabel) {
		try {
			return this.wrap(
					this.unwrap().getBlob(columnLabel));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateBlob(int columnIndex, java.sql.Blob value) {
		try {
			this.unwrap().updateBlob(columnIndex, this.unwrap(value));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateBlob(
			int columnIndex,
			InputStream value,
			long length) {
		try {
			this.unwrap().updateBlob(columnIndex, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateBlob(
			int columnIndex,
			InputStream value) {
		try {
			this.unwrap().updateBlob(columnIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateBlob(String columnLabel, java.sql.Blob value) {
		try {
			this.unwrap().updateBlob(columnLabel, this.unwrap(value));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateBlob(
			String columnLabel,
			InputStream value,
			long length) {
		try {
			this.unwrap().updateBlob(columnLabel, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateBlob(
			String columnLabel,
			InputStream value) {
		try {
			this.unwrap().updateBlob(columnLabel, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Clob getClob(int columnIndex) {
		try {
			return this.wrap(
					this.unwrap().getClob(columnIndex));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Clob getClob(String columnLabel) {
		try {
			return this.wrap(
					this.unwrap().getClob(columnLabel));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateClob(int columnIndex, java.sql.Clob value) {
		try {
			this.unwrap().updateClob(columnIndex, this.unwrap(value));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateClob(
			int columnIndex,
			Reader value,
			long length) {
		try {
			this.unwrap().updateClob(columnIndex, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateClob(
			int columnIndex,
			Reader value) {
		try {
			this.unwrap().updateClob(columnIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateClob(String columnLabel, java.sql.Clob value) {
		try {
			this.unwrap().updateClob(columnLabel, this.unwrap(value));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateClob(
			String columnLabel,
			Reader value,
			long length) {
		try {
			this.unwrap().updateClob(columnLabel, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateClob(
			String columnLabel,
			Reader value) {
		try {
			this.unwrap().updateClob(columnLabel, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public NClob getNClob(int columnIndex) {
		try {
			return this.wrap(
					this.unwrap().getNClob(columnIndex));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public NClob getNClob(String columnLabel) {
		try {
			return this.wrap(
					this.unwrap().getNClob(columnLabel));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateNClob(int columnIndex, java.sql.NClob value) {
		try {
			this.unwrap().updateNClob(columnIndex, this.unwrap(value));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateNClob(
			int columnIndex,
			Reader value,
			long length) {
		try {
			this.unwrap().updateNClob(columnIndex, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateNClob(
			int columnIndex,
			Reader value) {
		try {
			this.unwrap().updateNClob(columnIndex, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateNClob(String columnLabel, java.sql.NClob value) {
		try {
			this.unwrap().updateNClob(columnLabel, this.unwrap(value));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateNClob(
			String columnLabel,
			Reader value,
			long length) {
		try {
			this.unwrap().updateNClob(columnLabel, value, length);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateNClob(
			String columnLabel,
			Reader value) {
		try {
			this.unwrap().updateNClob(columnLabel, value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public SQLXML getSQLXML(int columnIndex) {
		try {
			return this.wrap(
					this.unwrap().getSQLXML(columnIndex));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public SQLXML getSQLXML(String columnLabel) {
		try {
			return this.wrap(
					this.unwrap().getSQLXML(columnLabel));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateSQLXML(int columnIndex, java.sql.SQLXML value) {
		try {
			this.unwrap().updateSQLXML(columnIndex, this.unwrap(value));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateSQLXML(String columnLabel, java.sql.SQLXML value) {
		try {
			this.unwrap().updateSQLXML(columnLabel, this.unwrap(value));
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
	public void updateNull(int columnIndex) {
		try {
			this.unwrap().updateNull(columnIndex);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void updateNull(String columnLabel) {
		try {
			this.unwrap().updateNull(columnLabel);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

}

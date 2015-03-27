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

public interface ResultSetColumn {

	public int getColumn();

	public boolean isAutoIncrement();

	public boolean isCaseSensitive();

	public boolean isSearchable();

	public boolean isCurrency();

	public int isNullable();

	public boolean isSigned();

	public int getColumnDisplaySize();

	public String getColumnLabel();

	public String getColumnName();

	public String getSchemaName();

	public int getPrecision();

	public int getScale();

	public String getTableName();

	public String getCatalogName();

	public int getColumnType();

	public String getColumnTypeName();

	public boolean isReadOnly();

	public boolean isWritable();

	public boolean isDefinitelyWritable();

	public String getColumnClassName();

}

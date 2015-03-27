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

import java.util.Map;
import java.sql.SQLException;

public class Ref
extends DatabaseObject<java.sql.Ref>
implements java.sql.Ref {

	public Ref(Connection connection, java.sql.Ref wrapped) {
		super(connection, wrapped);
	}

	@Override
	public String getBaseTypeName() {
		try {
			return this.unwrap().getBaseTypeName();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Object getObject(Map<String, Class<?>> map) {
		try {
			return this.unwrap().getObject(map);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Object getObject() {
		try {
			return this.unwrap().getObject();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setObject(Object value) {
		try {
			this.unwrap().setObject(value);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

}

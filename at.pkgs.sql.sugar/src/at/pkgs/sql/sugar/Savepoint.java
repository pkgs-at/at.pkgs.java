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

import java.sql.SQLException;

public class Savepoint
extends DatabaseObject<java.sql.Savepoint>
implements java.sql.Savepoint {

	public Savepoint(Connection connection, java.sql.Savepoint wrapped) {
		super(connection, wrapped);
	}

	@Override
	public int getSavepointId() {
		try {
			return this.unwrap().getSavepointId();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public String getSavepointName() {
		try {
			return this.unwrap().getSavepointName();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

}

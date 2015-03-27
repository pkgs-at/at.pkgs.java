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

import java.util.Enumeration;
import java.util.Properties;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;

public class Driver implements java.sql.Driver {

	@Override
	public int getMajorVersion() {
		return 1;
	}

	@Override
	public int getMinorVersion() {
		return 0;
	}

	@Override
	public boolean jdbcCompliant() {
		return false;
	}

	protected java.sql.Driver getDriver(String url) {
		String underlying;
		Enumeration<java.sql.Driver> drivers;

		if (!url.startsWith("jdbc:sugar:")) return null;
		underlying = "jdbc:" + url.substring(11);
		drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			java.sql.Driver driver;

			driver = drivers.nextElement();
			if (driver == this) continue;
			try {
				if (driver.acceptsURL(underlying)) return driver;
			}
			catch (SQLException cause) {
				throw new DatabaseException(cause);
			}
		}
		return null;
	}

	@Override
	public boolean acceptsURL(String url) {
		return this.getDriver(url) != null;
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(
			String url,
			Properties properties) {
		java.sql.Driver driver;

		driver = this.getDriver(url);
		if (driver == null)
			throw new DatabaseException("driver not found: " + url);
		try {
			return driver.getPropertyInfo(url, properties);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Connection connect(
			String url,
			Properties properties) {
		java.sql.Driver driver;

		driver = this.getDriver(url);
		if (driver == null)
			throw new DatabaseException("driver not found: " + url);
		try {
			return Connection.wrap(driver.connect(url, properties));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	static {
		try {
			DriverManager.registerDriver(new Driver());
		}
		catch (SQLException cause) {
			throw new ExceptionInInitializerError(cause);
		}
	}

}

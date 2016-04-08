/*
 * Copyright (c) 2009-2016, Architector Inc., Japan
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

package at.pkgs.sql.trifle;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLNonTransientException;
import at.pkgs.sql.trifle.dialect.Dialect;

public abstract class AbstractDatabase {

	public static interface Transactional {

		public enum Isolation {

			READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),

			READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),

			REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),

			SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

			private final int value;

			private Isolation(int value) {
				this.value = value;
			}

			public int value() {
				return this.value;
			}

		}

		public Isolation isolation();

		public int retry();

		public long interval();

	}

	public static abstract class Transaction implements Transactional {

		public Isolation isolation() {
			return Isolation.READ_COMMITTED;
		}

		public int retry() {
			return 0;
		}

		public long interval() {
			return Double.valueOf(Math.random() * 1000D).longValue();
		}

	}

	public static interface Action extends Transactional {

		public abstract void execute(
				Connection connection)
						throws SQLException;

	}

	public static interface Function<ResultType> extends Transactional {

		public abstract ResultType execute(
				Connection connection)
						throws SQLException;

	}

	protected abstract Dialect getDialect();

	protected Connection getConnection() throws SQLException {
		throw new UnsupportedOperationException("getConnection() is not implemented");
	}

	protected Query query() {
		return new Query(this.getDialect());
	}

	protected <ResultType> ResultType transaction(
			Function<ResultType> transaction)
					throws SQLException {
		int retry;

		retry = 0;
		while (true) {
			Connection connection;
			boolean ignore;
			Integer transactionIsolation;
			Boolean autoCommit;

			connection = this.getConnection();
			ignore = true;
			transactionIsolation = null;
			autoCommit = null;
			try {
				ResultType result;
				Transactional.Isolation isolation;

				isolation = transaction.isolation();
				if (isolation != null) {
					transactionIsolation = connection.getTransactionIsolation();
					connection.setTransactionIsolation(isolation.value());
				}
				autoCommit = connection.getAutoCommit();
				connection.setAutoCommit(false);
				result = transaction.execute(connection);
				connection.commit();
				if (autoCommit != null)
					connection.setAutoCommit(autoCommit);
				if (transactionIsolation != null)
					connection.setTransactionIsolation(transactionIsolation);
				ignore = false;
				return result;
			}
			catch (SQLException cause) {
				try {
					connection.rollback();
					if (autoCommit != null)
						connection.setAutoCommit(autoCommit);
					if (transactionIsolation != null)
						connection.setTransactionIsolation(transactionIsolation);
				}
				catch (SQLException ignored) {
					// do nothing
				}
				if (cause instanceof SQLNonTransientException) throw cause;
				if (retry >= transaction.retry()) throw cause;
			}
			finally {
				try {
					connection.close();
				}
				catch (SQLException cause) {
					if (!ignore) throw cause;
				}
			}
			retry ++;
			try {
				Thread.sleep(transaction.interval());
			}
			catch (InterruptedException ignored) {
				// do nothing
			}
		}
	}

	protected void transaction(
			final Action transaction)
					throws SQLException {
		this.transaction(new Function<Void>() {

			@Override
			public Isolation isolation() {
				return transaction.isolation();
			}

			@Override
			public int retry() {
				return transaction.retry();
			}

			@Override
			public long interval() {
				return transaction.interval();
			}

			@Override
			public Void execute(
					Connection connection)
							throws SQLException {
				transaction.execute(connection);
				return null;
			}

		});
	}

}

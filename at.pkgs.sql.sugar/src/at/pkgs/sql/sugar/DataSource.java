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

import java.io.PrintWriter;
import java.sql.SQLException;

public class DataSource implements javax.sql.DataSource {

	public class Moderator {

		public static final int ABORTED_DEPTH = (-2);

		public static final int INITIAL_DEPTH = (-1);

		public static final int ORIGINAL_DEPTH = 0;

		private final Connection connection;

		private final String username;

		private final long threadId;

		private int depth;

		private Moderator(Connection connection, String username) {
			this.connection = connection;
			this.connection.transactional();
			try {
				if (!this.connection.unwrap().getAutoCommit())
					throw new DatabaseException(
							"initial auto-commit mode is false");
				this.connection.unwrap().setAutoCommit(false);
			}
			catch (SQLException cause) {
				throw new DatabaseException(cause);
			}
			this.username = username;
			this.threadId = Thread.currentThread().getId();
			this.depth = Moderator.INITIAL_DEPTH;
		}

		private Moderator(String username, String password) {
			this(
					DataSource.this.getConnection(username, password),
					username);
		}

		private Moderator() {
			this(
					DataSource.this.getConnection(),
					null);
		}

		public int getDepth() {
			return this.depth;
		}

		public void ensureOriginalThread() {
			if (this.threadId != Thread.currentThread().getId())
				throw new DatabaseException(
						"invalid thread context: not original thread");
		}

		Connection getConnection(String username) {
			this.ensureOriginalThread();
			if (username != null && username.equals(this.username))
				throw new DatabaseException("unmatched username");
			if (this.depth < Moderator.ORIGINAL_DEPTH)
				throw new DatabaseException("dead transaction");
			return this.connection;
		}

		Connection getConnection() {
			return this.getConnection(null);
		}

		private Moderator begin() {
			this.ensureOriginalThread();
			if (this.depth < Moderator.INITIAL_DEPTH)
				throw new DatabaseException("aborted transaction");
			this.depth ++;
			return this;
		}

		public void commit() {
			this.ensureOriginalThread();
			if (this.depth < Moderator.ORIGINAL_DEPTH)
				throw new DatabaseException("dead transaction");
			if (this.depth > Moderator.ORIGINAL_DEPTH) {
				this.depth --;
				return;
			}
			try {
				this.connection.unwrap().commit();
				this.connection.unwrap().setAutoCommit(true);
				this.connection.unwrap().close();
			}
			catch (SQLException cause) {
				throw new DatabaseException(cause);
			}
			DataSource.this.complete();
			this.depth = Moderator.INITIAL_DEPTH;
		}

		public void rollback() {
			if (this.depth < Moderator.ORIGINAL_DEPTH) return;
			try {
				this.ensureOriginalThread();
				try {
					this.connection.unwrap().rollback();
					this.connection.unwrap().setAutoCommit(true);
					this.connection.unwrap().close();
				}
				catch (SQLException cause) {
					throw new DatabaseException(cause);
				}
			}
			finally {
				DataSource.this.complete();
				this.depth = Moderator.ABORTED_DEPTH;
			}
		}

	}

	private final javax.sql.DataSource wrapped;

	private final ThreadLocal<Moderator> moderator;

	public DataSource(javax.sql.DataSource wrapped) {
		this.wrapped = wrapped;
		this.moderator = new ThreadLocal<Moderator>();
	}

	@Override
	public boolean isWrapperFor(Class<?> type) {
		if (type.isAssignableFrom(this.getClass()))
			return true;
		if (type.isAssignableFrom(this.wrapped.getClass()))
			return true;
		try {
			return this.wrapped.isWrapperFor(type);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public <Type> Type unwrap(Class<Type> type) {
		if (type.isAssignableFrom(this.getClass()))
			return type.cast(this);
		if (type.isAssignableFrom(this.wrapped.getClass()))
			return type.cast(this.wrapped);
		try {
			return this.wrapped.unwrap(type);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	public javax.sql.DataSource unwrap() {
		return this.wrapped;
	}

	@Override
	public PrintWriter getLogWriter() {
		try {
			return this.wrapped.getLogWriter();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setLogWriter(PrintWriter out) {
		try {
			this.wrapped.setLogWriter(out);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int getLoginTimeout() {
		try {
			return this.wrapped.getLoginTimeout();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public void setLoginTimeout(int seconds) {
		try {
			this.wrapped.setLoginTimeout(seconds);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Connection getConnection(String username, String password) {
		if (this.moderator.get() != null)
			return this.moderator.get().getConnection(username);
		try {
			return Connection.wrap(
					this.wrapped.getConnection(username, password));
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Connection getConnection() {
		if (this.moderator.get() != null)
			return this.moderator.get().getConnection();
		try {
			return Connection.wrap(
					this.wrapped.getConnection());
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	public Transaction transact(String username, String password) {
		if (this.moderator.get() == null)
			this.moderator.set(new Moderator(username, password));
		return new Transaction(this.moderator.get().begin());
	}

	public Transaction transact() {
		if (this.moderator.get() == null)
			this.moderator.set(new Moderator());
		return new Transaction(this.moderator.get().begin());
	}

	private void complete() {
		this.moderator.remove();
	}

	public static DataSource wrap(javax.sql.DataSource wrapped) {
		if (wrapped == null) return null;
		if (wrapped instanceof DataSource) return (DataSource)wrapped;
		return new DataSource(wrapped);
	}

}

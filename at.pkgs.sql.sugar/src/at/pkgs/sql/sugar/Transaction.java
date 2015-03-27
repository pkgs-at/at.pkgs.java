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

import java.io.Closeable;

public class Transaction implements Closeable {

	private final DataSource.Moderator moderator;

	private boolean committed;

	Transaction(DataSource.Moderator moderator) {
		this.moderator = moderator;
		this.committed = false;
	}

	@Override
	public String toString() {
		StringBuilder builder;

		builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(": depth:");
		builder.append(this.moderator.getDepth());
		builder.append(": thread:");
		builder.append(Thread.currentThread().getId());
		return builder.toString();
	}

	public Connection getConnection() {
		return this.moderator.getConnection();
	}

	public void commit() {
		this.moderator.commit();
		this.committed = true;
	}

	public void finish() {
		if (!this.committed) {
			this.moderator.rollback();
			this.committed = true;
		}
	}

	@Override
	public void close() {
		this.finish();
	}

}

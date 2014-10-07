/*
 * Copyright (c) 2009-2014, Architector Inc., Japan
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

package at.pkgs.sql.query;

import java.util.Iterator;
import java.sql.ResultSet;
import java.sql.SQLException;

class ResultRowIterable implements Iterable<Object> {

	private final ResultSet result;

	private final int length;

	ResultRowIterable(ResultSet result, int length) {
		this.result = result;
		this.length = length;
	}

	@Override
	public Iterator<Object> iterator() {
		return new Iterator<Object>() {

			private int index;

			{
				index = 0;
			}

			@Override
			public boolean hasNext() {
				return this.index < ResultRowIterable.this.length;
			}

			@Override
			public Object next() {
				try {
					return ResultRowIterable.this.result.getObject(
							++ this.index);
				}
				catch (SQLException throwable) {
					throw new Database.Exception(throwable);
				}
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}

}

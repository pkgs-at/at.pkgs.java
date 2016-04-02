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

package at.pkgs.sql.trifle.dialect;

import at.pkgs.sql.trifle.Query;

public class ApacheDerby extends Dialect {

	@Override
	public void select(
			Query query,
			Table table,
			boolean distinct,
			Query.Criteria criteria,
			Query.OrderBy sort,
			long offset,
			long length) {
		super.select(query, table, distinct, criteria, sort, -1L, -1L);
		if (offset < 0L) {
			if (length < 0L) return;
			offset = 0L;
		}
		query.append(" OFFSET ").value(offset).append(" ROWS");
		if (length >= 0L) query.append(" FETCH NEXT ").value(length).append(" ROWS ONLY");
	}

}

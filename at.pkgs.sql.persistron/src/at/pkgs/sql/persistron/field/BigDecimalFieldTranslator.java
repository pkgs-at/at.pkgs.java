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

package at.pkgs.sql.persistron.field;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BigDecimalFieldTranslator extends FieldTranslator {

	@Override
	public void set(
			PreparedStatement statement,
			int index,
			Object value) throws Exception {
		statement.setBigDecimal(index + 1, (BigDecimal)value);
	}

	@Override
	public void populate(
			Object target,
			ResultSet result,
			int index) throws Exception {
		this.field().set(target, result.getBigDecimal(index + 1));
	}

}
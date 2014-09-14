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

import java.lang.reflect.Field;
import at.pkgs.sql.persistron.Column;
import at.pkgs.sql.persistron.DatabaseException;

public class FieldTranslatorFactory {

	protected Class<? extends FieldTranslator> getClassForArrayOf(
			Class<?> type)
					throws Exception {
		if (type == Byte.TYPE)
			return BytePrimitiveArrayFieldTranslator.class;
		throw new DatabaseException(
				"currently unsupported array of %s",
				type.getName());
	}

	protected Class<? extends FieldTranslator> getClassForPrimitiveOf(
			Class<?> type)
					throws Exception {
		if (type == Boolean.TYPE)
			return BooleanPrimitiveFieldTranslator.class;
		if (type == Integer.TYPE)
			return IntegerPrimitiveFieldTranslator.class;
		if (type == Long.TYPE)
			return LongPrimitiveFieldTranslator.class;
		if (type == Float.TYPE)
			return FloatPrimitiveFieldTranslator.class;
		if (type == Double.TYPE)
			return DoublePrimitiveFieldTranslator.class;
		throw new DatabaseException(
				"currently unsupported primitive %s",
				type.getName());
	}

	protected Class<? extends FieldTranslator> getClassForEnumOf(
			Class<?> type)
					throws Exception {
		return EnumFieldTranslator.class;
	}

	protected Class<? extends FieldTranslator> getClassForObjectOf(
			Class<?> type)
					throws Exception {
		if (type.isAssignableFrom(String.class))
			return StringFieldTranslator.class;
		if (type.isAssignableFrom(Boolean.class))
			return BooleanObjectFieldTranslator.class;
		if (type.isAssignableFrom(Integer.class))
			return IntegerObjectFieldTranslator.class;
		if (type.isAssignableFrom(Long.class))
			return LongObjectFieldTranslator.class;
		if (type.isAssignableFrom(Float.class))
			return FloatObjectFieldTranslator.class;
		if (type.isAssignableFrom(Double.class))
			return DoubleObjectFieldTranslator.class;
		if (type.isAssignableFrom(java.sql.Timestamp.class))
			return TimestampFieldTranslator.class;
		if (type.isAssignableFrom(java.sql.Date.class))
			return DateFieldTranslator.class;
		if (type.isAssignableFrom(java.sql.Time.class))
			return TimeFieldTranslator.class;
		if (type.isAssignableFrom(java.math.BigDecimal.class))
			return BigDecimalFieldTranslator.class;
		throw new DatabaseException(
				"unsupported type %s",
				type.getName());
	}

	protected Class<? extends FieldTranslator> getClassFor(
			Class<?> type)
					throws Exception {
		if (type.isArray())
			return this.getClassForArrayOf(type.getComponentType());
		else if (type.isPrimitive())
			return this.getClassForPrimitiveOf(type);
		else if (type.isEnum())
			return this.getClassForEnumOf(type);
		else
			return this.getClassForObjectOf(type);
	}

	public FieldTranslator create(
			Class<?> type,
			Field field,
			Enum<?> identifier,
			Column column) {
		FieldTranslator translator;

		try {
			translator = this.getClassFor(field.getType()).newInstance();
		}
		catch (DatabaseException throwable) {
			throw throwable;
		}
		catch (Exception throwable) {
			throw new DatabaseException(
					throwable,
					"failed on FieldTranslator select");
		}
		translator.initialize(field, identifier, column);
		return translator;
	}

}

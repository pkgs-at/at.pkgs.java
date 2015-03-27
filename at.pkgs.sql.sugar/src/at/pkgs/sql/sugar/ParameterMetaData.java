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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.sql.SQLException;

public class ParameterMetaData
extends DatabaseObject<java.sql.ParameterMetaData>
implements java.sql.ParameterMetaData, Iterable<ParameterDetail> {

	private class Detail implements ParameterDetail {

		private final int parameter;

		private Detail(int parameter) {
			this.parameter = parameter;
		}

		@Override
		public int getParameter() {
			return this.parameter;
		}

		@Override
		public int isNullable() {
			return ParameterMetaData.this.isNullable(
					this.parameter);
		}

		@Override
		public boolean isSigned() {
			return ParameterMetaData.this.isSigned(
					this.parameter);
		}

		@Override
		public int getPrecision() {
			return ParameterMetaData.this.getPrecision(
					this.parameter);
		}

		@Override
		public int getScale() {
			return ParameterMetaData.this.getScale(
					this.parameter);
		}

		@Override
		public int getParameterType() {
			return ParameterMetaData.this.getParameterType(
					this.parameter);
		}

		@Override
		public String getParameterTypeName() {
			return ParameterMetaData.this.getParameterTypeName(
					this.parameter);
		}

		@Override
		public String getParameterClassName() {
			return ParameterMetaData.this.getParameterClassName(
					this.parameter);
		}

		@Override
		public int getParameterMode() {
			return ParameterMetaData.this.getParameterMode(
					this.parameter);
		}

	}

	private class DetailIterator implements Iterator<ParameterDetail> {

		private final int length;

		private int index;

		private DetailIterator() {
			this.length = ParameterMetaData.this.getParameterCount();
			this.index = 0;
		}

		@Override
		public boolean hasNext() {
			return  this.index < this.length;
		}

		@Override
		public ParameterDetail next() {
			if (!this.hasNext()) throw new NoSuchElementException();
			return new Detail(++ this.index);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException("remove()");
		}

	}

	public ParameterMetaData(
			Connection connection,
			java.sql.ParameterMetaData wrapped) {
		super(connection, wrapped);
	}

	@Override
	public int getParameterCount() {
		try {
			return this.unwrap().getParameterCount();
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int isNullable(int parameter) {
		try {
			return this.unwrap().isNullable(parameter);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public boolean isSigned(int parameter) {
		try {
			return this.unwrap().isSigned(parameter);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int getPrecision(int parameter) {
		try {
			return this.unwrap().getPrecision(parameter);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int getScale(int parameter) {
		try {
			return this.unwrap().getScale(parameter);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int getParameterType(int parameter) {
		try {
			return this.unwrap().getParameterType(parameter);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public String getParameterTypeName(int parameter) {
		try {
			return this.unwrap().getParameterTypeName(parameter);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public String getParameterClassName(int parameter) {
		try {
			return this.unwrap().getParameterClassName(parameter);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public int getParameterMode(int parameter) {
		try {
			return this.unwrap().getParameterMode(parameter);
		}
		catch (SQLException cause) {
			throw new DatabaseException(cause);
		}
	}

	@Override
	public Iterator<ParameterDetail> iterator() {
		return new DetailIterator();
	}

}

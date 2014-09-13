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

package at.pkgs.web.http;

import java.util.Iterator;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collections;

public class EntryIterable<Type> implements Iterable<Entry<String, Type>> {

	public static interface Keys {

		public Enumeration<String> get();

	}

	public static interface Values<Type> {

		public Type get(String key);

	}

	private final Keys keys;

	private final Values<Type> values;

	public EntryIterable(Keys keys, Values<Type> values) {
		this.keys = keys;
		this.values = values;
	}

	public EntryIterable(final Map<String, Type> map) {
		this(
				new EntryIterable.Keys() {

					@Override
					public Enumeration<String> get() {
						return Collections.enumeration(map.keySet());
					}

				},
				new EntryIterable.Values<Type>() {

					@Override
					public Type get(String key) {
						return map.get(key);
					}

				});
	}

	@Override
	public Iterator<Entry<String, Type>> iterator() {
		final Enumeration<String> keys;

		keys = this.keys.get();
		return new Iterator<Entry<String, Type>>() {

			@Override
			public boolean hasNext() {
				return keys.hasMoreElements();
			}

			@Override
			public Entry<String, Type> next() {
				String key;

				key = keys.nextElement();
				return new SimpleImmutableEntry<String, Type>(
						key,
						EntryIterable.this.values.get(key));
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}

}

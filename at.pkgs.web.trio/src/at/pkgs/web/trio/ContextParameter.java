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

package at.pkgs.web.trio;

import java.util.regex.Pattern;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ContextParameter {

	public static class ForDebug extends ContextParameter {

		public ForDebug() {
			this.setTemplateCache(false);
		}

	}

	public static final String NAME =
			"java:comp/env/" + ContextParameter.class.getName();

	public static final Pattern STRINGS_SEPARATOR =
			Pattern.compile(",\\s*");

	public static final String[] EMPTY_STRINGS =
			new String[0];

	private static volatile ContextParameter instance = null;

	private boolean templateCache;

	private String[] templateIncludes;

	public ContextParameter() {
		this.templateCache = true;
		this.templateIncludes = null;
	}

	public boolean getTemplateCache() {
		return this.templateCache;
	}

	public void setTemplateCache(boolean value) {
		this.templateCache = value;
	}

	public String[] getTemplateIncludes() {
		return this.templateIncludes == null ?
				ContextParameter.EMPTY_STRINGS :
				this.templateIncludes;
	}

	public void setTemplateIncludes(String[] value) {
		this.templateIncludes = value;
	}

	public void setTemplateIncludes(String value) {
		this.templateIncludes = (value == null || value.isEmpty()) ?
				null :
				ContextParameter.STRINGS_SEPARATOR.split(value);
	}

	public static ContextParameter get() {
		if (ContextParameter.instance == null) {
			synchronized(ContextParameter.class) {
				if (ContextParameter.instance == null) {
					ContextParameter instance;

					instance = null;
					try {
						instance = (ContextParameter)new InitialContext()
								.lookup(ContextParameter.NAME);
					}
					catch (NamingException ignored) {
						// do nothing
					}
					ContextParameter.instance = instance == null ?
							new ContextParameter() :
							instance;
				}
			}
		}
		return ContextParameter.instance;
	}

}

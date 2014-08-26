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

package at.pkgs.template;

import java.util.Map;
import java.io.IOException;
import javax.script.ScriptException;
import at.pkgs.template.preprocessor.TemplateParser;

public class Template {

	private final TemplateEngine engine;

	private final Object script;

	public Template(TemplateEngine engine, String path) throws IOException {
		TemplateParser parser;

		this.engine = engine;
		parser = new TemplateParser(this.engine.getTemplateResolver());
		parser.parse(path);
		try {
			this.script =
					this.engine.getInvoker().invokeFunction(
							"template",
							parser.content());
		}
		catch (ScriptException | NoSuchMethodException throwable) {
			throw new TemplateException(throwable);
		}
	}

	public String render(Map<String, Object> attributes) {
		try {
			return (String)this.engine.getInvoker().invokeMethod(
					this.script,
					"render",
					attributes);
		}
		catch (ScriptException | NoSuchMethodException throwable) {
			throw new TemplateException(throwable);
		}
	}

}

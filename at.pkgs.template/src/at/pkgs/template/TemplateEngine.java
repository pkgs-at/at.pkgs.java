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

import java.io.InputStream;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import javax.script.Invocable;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class TemplateEngine {

	private final TemplateResolver resolver;

	private final ScriptEngine script;

	public TemplateEngine(TemplateResolver resolver) throws IOException {
		this.resolver = resolver;
		this.script = new ScriptEngineManager().getEngineByName("nashorn");
		if (this.script == null)
			throw new TemplateException("script engine narshorn not found");
		try (InputStream input =
				this.getClass().getResourceAsStream(
						"script/at.pkgs-1.0.0.js")) {
			this.include(input);
		}
		try (InputStream input =
				this.getClass().getResourceAsStream(
						"script/sugar-1.4.1.js")) {
			this.include(input);
		}
		try (InputStream input =
				this.getClass().getResourceAsStream(
						"script/template.js")) {
			this.include(input);
		}
	}

	public void include(Reader reader) throws IOException {
		try (Reader source = reader) {
			this.script.eval(source);
		}
		catch (ScriptException throwable) {
			throw new TemplateException(throwable);
		}
	}

	public void include(InputStream input, Charset charset)
			throws IOException {
		this.include(new InputStreamReader(input, charset));
	}

	public void include(InputStream input)
			throws IOException {
		this.include(input, StandardCharsets.UTF_8);
	}

	public TemplateResolver getTemplateResolver() {
		return this.resolver;
	}

	public ScriptEngine getScriptEngine() {
		return this.script;
	}

	public Invocable getInvoker() {
		return (Invocable)this.script;
	}

	public Template template(String path) throws IOException {
		return new Template(this, path);
	}

	public static void main(String[] arguments) throws IOException {
		TemplateEngine engine;
		Template template;
		Runnable test;
		final String expected = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"><title>at.pkgs.template</title></head><body><h1>at.pkgs.template</h1></body></html>";

		engine =
				new TemplateEngine(
						new ClasspathResolver(
								"/at/pkgs/template/test",
								TemplateEngine.class.getClassLoader()));
		template = engine.template("default.html");
		test = new Runnable() {

			@Override
			public void run() {
				TemplateContext context;
				String result;

				context = new TemplateContext();
				context.put("title", "at.pkgs.template");
				for (int count = 0; count < 100; count ++) {
					result = template.render(context);
					if (!expected.equals(result))
						System.out.println("unexpected result: " + result);
				}
			}

		};
		for (int index = 0; index < 1000; index ++) {
			new Thread(test).run();
		}

	}

}

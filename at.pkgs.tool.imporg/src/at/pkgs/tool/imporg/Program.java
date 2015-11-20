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

package at.pkgs.tool.imporg;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.File;
import java.io.PrintStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.IOException;

public class Program implements Runnable {

	public static enum Option {

		ENCODING,

		LF,

		CR,

		CRLF;

	}

	private final static String DEFAULT_ENCODING = "UTF-8";

	private final static String DEFAULT_NEWLINE = "\n";

	private final static Pattern IMPORT_PATTERN = Pattern.compile(
			"import(\\s+static)?\\s+([^;\\s]+)\\s*;");

	private String encoding;

	private String newline;

	private final Set<String> dictionary;

	private final List<File> includes;

	private final List<File> excludes;

	private final Set<String> imports;

	public Program() {
		this.encoding = Program.DEFAULT_ENCODING;
		this.newline = Program.DEFAULT_NEWLINE;
		this.dictionary = new LinkedHashSet<String>();
		this.includes = new ArrayList<File>();
		this.excludes = new ArrayList<File>();
		this.imports = new TreeSet<String>();
	}

	public Program encoding(String encoding) {
		this.encoding = encoding;
		return this;
	}

	public Program newline(String newline) {
		this.newline = newline;
		return this;
	}

	public Program dictionary(File file) {
		BufferedReader reader;

		reader = null;
		try {
			String line;

			reader = new BufferedReader(
					new InputStreamReader(
							new BufferedInputStream(
									new FileInputStream(file)),
							this.encoding));
			line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty()) continue;
				if (line.startsWith("#")) continue;
				this.dictionary.add(line);
			}
		}
		catch (IOException cause) {
			throw new RuntimeException(cause);
		}
		finally {
			try {
				if (reader != null) reader.close();
			}
			catch (IOException cause) {
				throw new RuntimeException(cause);
			}
		}
		return this;
	}

	public Program include(File file) {
		this.includes.add(file.getAbsoluteFile());
		return this;
	}

	public Program exclude(File file) {
		this.excludes.add(file.getAbsoluteFile());
		return this;
	}

	public void process(File file) {
		Set<String> imports;
		Set<String> sorteds;
		StringBuilder header;
		StringBuilder body;
		BufferedReader reader;
		BufferedWriter writer;

		imports = new HashSet<String>();
		sorteds = new LinkedHashSet<String>(this.dictionary);
		header = new StringBuilder();
		body = new StringBuilder();
		reader = null;
		try {
			String line;

			reader = new BufferedReader(
					new InputStreamReader(
							new BufferedInputStream(
									new FileInputStream(file)),
							this.encoding));
			while ((line = reader.readLine()) != null) {
				Matcher matcher;

				matcher = Program.IMPORT_PATTERN.matcher(line);
				if (matcher.matches()) {
					String target;

					if (matcher.groupCount() > 2)
						target = "static " + matcher.group(3);
					else
						target = matcher.group(2);
					this.imports.add(target);
					imports.add(target);
					sorteds.add(target);
					continue;
				}
				if (imports.size() <= 0)
					header.append(line).append(this.newline);
				else
					body.append(line).append(this.newline);
			}
		}
		catch (IOException cause) {
			throw new RuntimeException(cause);
		}
		finally {
			try {
				if (reader != null) reader.close();
			}
			catch (IOException cause) {
				throw new RuntimeException(cause);
			}
		}
		sorteds.retainAll(imports);
		for (String target : sorteds)
			header
					.append("import ")
					.append(target)
					.append(';')
					.append(this.newline);
		body.insert(0, header);
		writer = null;
		try {
			writer = new BufferedWriter(
					new OutputStreamWriter(
							new BufferedOutputStream(
									new FileOutputStream(file)),
							this.encoding));
			writer.write(body.toString());
		}
		catch (IOException cause) {
			throw new RuntimeException(cause);
		}
		finally {
			try {
				if (writer != null) writer.close();
			}
			catch (IOException cause) {
				throw new RuntimeException(cause);
			}
		}
	}

	public void walk(File path) {
		if (this.excludes.contains(path)) return;
		for (File file : path.listFiles()) {
			if (file.isDirectory()) {
				this.walk(file);
			}
			else {
				if (!file.getName().endsWith(".java")) continue;
				if (this.excludes.contains(path)) continue;
				this.process(file);
			}
		}
	}

	@Override
	public void run() {
		Set<String> unused;
		Set<String> unlisted;

		for (File path : this.includes) this.walk(path);
		unused = new LinkedHashSet<String>(this.dictionary);
		unused.removeAll(this.imports);
		System.out.println("# unused");
		for (String target : unused) System.out.println(target);
		unlisted = new TreeSet<String>(this.imports);
		unlisted.removeAll(this.dictionary);
		System.out.println("# unlisted");
		for (String target : unlisted) System.out.println(target);
	}

	public static void usage(PrintStream output) {
		output.print("arguments:");
		output.print(" [-lf | -cr | -crlf]");
		output.print(" [-encoding:ENCODING]");
		output.print(" DICTIONARY_FILE (INCLUDE_PATH | !EXCLUDE_PATH)...");
		output.println();
		output.println("    -lf | -cr | -crlf      : new line sequence");
		output.println("    -encoding:ENCODING     : source file encoding");
	}

	public static void main(String... arguments) {
		Program program;
		File dictionary;

		if (arguments.length < 1) {
			Program.usage(System.err);
			System.exit(1);
			throw new Error();
		}
		program = new Program();
		dictionary = null;
		for (String argument : arguments) {
			if (argument.startsWith("-")) {
				String[] parts;
				Option option;

				parts = argument.substring(1).split(":", 2);
				try {
					option = Option.valueOf(parts[0].toUpperCase());
				}
				catch (IllegalArgumentException cause) {
					System.err.print("unknown option ");
					System.err.print(argument);
					Program.usage(System.err);
					System.exit(1);
					throw new Error();
				}
				switch (option) {
				case ENCODING :
					if (parts.length < 2) {
						Program.usage(System.err);
						System.exit(1);
						throw new Error();
					}
					program.encoding(parts[1]);
					break;
				case LF :
					program.newline("\n");
					break;
				case CR :
					program.newline("\r");
					break;
				case CRLF :
					program.newline("\r\n");
					break;
				}
				continue;
			}
			if (dictionary == null) {
				dictionary = new File(argument);
				if (!dictionary.isFile()) {
					System.err.print('"');
					System.err.print(dictionary);
					System.err.print("\" is not a file.");
					System.err.println();
					Program.usage(System.err);
					System.exit(1);
					throw new Error();
				}
				program.dictionary(dictionary);
				continue;
			}
			if (argument.startsWith("!")) {
				File path;

				path = new File(argument.substring(1));
				if (!path.exists()) {
					System.err.print('"');
					System.err.print(path);
					System.err.print("\" dose not exist.");
					System.err.println();
					Program.usage(System.err);
					System.exit(1);
					throw new Error();
				}
				program.exclude(path);
			}
			else {
				File path;

				path = new File(argument);
				if (!path.isDirectory()) {
					System.err.print('"');
					System.err.print(path);
					System.err.print("\" is not a directory.");
					System.err.println();
					Program.usage(System.err);
					System.exit(1);
					throw new Error();
				}
				program.include(path);
			}
		}
		program.run();
	}

}

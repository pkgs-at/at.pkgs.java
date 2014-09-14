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

package at.pkgs.logging;

public final class Logger {

	private final Sink sink;

	private Integer depth;

	private int skip;

	public Logger(Sink sink) {
		this.sink = sink;
	}

	public Logger depth(Integer depth) {
		this.depth = depth;
		return this;
	}

	public Logger skip(int skip) {
		this.skip = skip;
		return this;
	}

	private String format(String message) {
		LoggerFactory factory;
		int depth;
		int skip;
		StringBuilder builder;

		factory = LoggerFactory.get();
		if (factory == null) return message;
		depth = this.depth != null ? this.depth : factory.depth;
		if (depth <= 0) return message;
		if (message != null) {
			builder = new StringBuilder(message);
		}
		else {
			builder = new StringBuilder("null");
		}
		builder.append('\n');
		skip = this.skip + 3;
		for(StackTraceElement frame : Thread.currentThread().getStackTrace()){
			if (-- skip >= 0) continue;
			if (-- depth < 0) {
				builder.append("# ...\n");
				break;
			}
			builder.append(
					String.format(
							"# at %s.%s(%s:%d)\n",
							frame.getClassName(),
							frame.getMethodName(),
							frame.getFileName(),
							frame.getLineNumber()));
		}
		return builder.toString();
	}

	public boolean trace() {
		return this.sink.enabled(Level.Trace);
	}

	public boolean trace(
			String message) {
		if (!this.sink.enabled(Level.Trace)) return false;
		this.sink.write(
				Level.Trace,
				this.format(message), null);
		return true;
	}

	public boolean trace(
			Throwable throwable,
			String message) {
		if (!this.sink.enabled(Level.Trace)) return false;
		this.sink.write(
				Level.Trace,
				this.format(message),
				throwable);
		return true;
	}

	public boolean trace(
			String format,
			Object... arguments) {
		if (!this.sink.enabled(Level.Trace)) return false;
		this.sink.write(
				Level.Trace,
				this.format(String.format(format, arguments)),
				null);
		return true;
	}

	public boolean trace(
			Throwable throwable,
			String format,
			Object... arguments) {
		if (!this.sink.enabled(Level.Trace)) return false;
		this.sink.write(
				Level.Trace,
				this.format(String.format(format, arguments)),
				throwable);
		return true;
	}

	public boolean debug() {
		return this.sink.enabled(Level.Debug);
	}

	public boolean debug(
			String message) {
		if (!this.sink.enabled(Level.Debug)) return false;
		this.sink.write(
				Level.Debug,
				this.format(message), null);
		return true;
	}

	public boolean debug(
			Throwable throwable,
			String message) {
		if (!this.sink.enabled(Level.Debug)) return false;
		this.sink.write(
				Level.Debug,
				this.format(message),
				throwable);
		return true;
	}

	public boolean debug(
			String format,
			Object... arguments) {
		if (!this.sink.enabled(Level.Debug)) return false;
		this.sink.write(
				Level.Debug,
				this.format(String.format(format, arguments)),
				null);
		return true;
	}

	public boolean debug(
			Throwable throwable,
			String format,
			Object... arguments) {
		if (!this.sink.enabled(Level.Debug)) return false;
		this.sink.write(
				Level.Debug,
				this.format(String.format(format, arguments)),
				throwable);
		return true;
	}

	public boolean information() {
		return this.sink.enabled(Level.Information);
	}

	public boolean information(
			String message) {
		if (!this.sink.enabled(Level.Information)) return false;
		this.sink.write(
				Level.Information,
				this.format(message),
				null);
		return true;
	}

	public boolean information(
			Throwable throwable,
			String message) {
		if (!this.sink.enabled(Level.Information)) return false;
		this.sink.write(
				Level.Information,
				this.format(message),
				throwable);
		return true;
	}

	public boolean information(
			String format,
			Object... arguments) {
		if (!this.sink.enabled(Level.Information)) return false;
		this.sink.write(
				Level.Information,
				this.format(String.format(format, arguments)),
				null);
		return true;
	}

	public boolean information(
			Throwable throwable,
			String format,
			Object... arguments) {
		if (!this.sink.enabled(Level.Information)) return false;
		this.sink.write(
				Level.Information,
				this.format(String.format(format, arguments)),
				throwable);
		return true;
	}

	public boolean warning() {
		return this.sink.enabled(Level.Warning);
	}

	public boolean warning(
			String message) {
		if (!this.sink.enabled(Level.Warning)) return false;
		this.sink.write(
				Level.Warning,
				this.format(message),
				null);
		return true;
	}

	public boolean warning(
			Throwable throwable,
			String message) {
		if (!this.sink.enabled(Level.Warning)) return false;
		this.sink.write(
				Level.Warning,
				this.format(message),
				throwable);
		return true;
	}

	public boolean warning(
			String format,
			Object... arguments) {
		if (!this.sink.enabled(Level.Warning)) return false;
		this.sink.write(
				Level.Warning,
				this.format(String.format(format, arguments)),
				null);
		return true;
	}

	public boolean warning(
			Throwable throwable,
			String format,
			Object... arguments) {
		if (!this.sink.enabled(Level.Warning)) return false;
		this.sink.write(
				Level.Warning,
				this.format(String.format(format, arguments)),
				throwable);
		return true;
	}

	public boolean error() {
		return this.sink.enabled(Level.Error);
	}

	public boolean error(
			String message) {
		if (!this.sink.enabled(Level.Error)) return false;
		this.sink.write(
				Level.Error,
				this.format(message),
				null);
		return true;
	}

	public boolean error(
			Throwable throwable,
			String message) {
		if (!this.sink.enabled(Level.Error)) return false;
		this.sink.write(
				Level.Error,
				this.format(message),
				throwable);
		return true;
	}

	public boolean error(
			String format,
			Object... arguments) {
		if (!this.sink.enabled(Level.Error)) return false;
		this.sink.write(
				Level.Error,
				this.format(String.format(format, arguments)),
				null);
		return true;
	}

	public boolean error(
			Throwable throwable,
			String format,
			Object... arguments) {
		if (!this.sink.enabled(Level.Error)) return false;
		this.sink.write(
				Level.Error,
				this.format(String.format(format, arguments)),
				throwable);
		return true;
	}

	public boolean fatal() {
		return this.sink.enabled(Level.Fatal);
	}

	public boolean fatal(
			String message) {
		if (!this.sink.enabled(Level.Fatal)) return false;
		this.sink.write(
				Level.Fatal,
				this.format(message),
				null);
		return true;
	}

	public boolean fatal(
			Throwable throwable,
			String message) {
		if (!this.sink.enabled(Level.Fatal)) return false;
		this.sink.write(
				Level.Fatal,
				this.format(message),
				throwable);
		return true;
	}

	public boolean fatal(
			String format,
			Object... arguments) {
		if (!this.sink.enabled(Level.Fatal)) return false;
		this.sink.write(
				Level.Fatal,
				this.format(String.format(format, arguments)),
				null);
		return true;
	}

	public boolean fatal(
			Throwable throwable,
			String format,
			Object... arguments) {
		if (!this.sink.enabled(Level.Fatal)) return false;
		this.sink.write(
				Level.Fatal,
				this.format(String.format(format, arguments)),
				throwable);
		return true;
	}

	public static Logger of(String name) {
		return LoggerFactory.get().logger(name);
	}

	public static Logger of(Class<?> type) {
		return of(type.getName());
	}

	public static Logger of(Object object) {
		return of(object.getClass().getName());
	}

}

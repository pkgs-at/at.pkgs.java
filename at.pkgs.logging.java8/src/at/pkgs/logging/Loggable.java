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

package at.pkgs.logging;

public interface Loggable {

	public Logger getLogger();

	public default boolean trace() {
		return this.getLogger().trace();
	}

	public default void trace(
			String message) {
		this.getLogger().trace(message);
	}

	public default void trace(
			String format,
			Object... arguments) {
		if (!this.trace()) return;
		this.getLogger().trace(format, arguments);
	}

	public default void trace(
			Throwable throwable,
			String message) {
		this.getLogger().trace(throwable, message);
	}

	public default void trace(
			Throwable throwable,
			String format,
			Object... arguments) {
		if (!this.trace()) return;
		this.getLogger().trace(throwable, format, arguments);
	}

	public default boolean debug() {
		return this.getLogger().debug();
	}

	public default void debug(
			String message) {
		this.getLogger().debug(message);
	}

	public default void debug(
			String format,
			Object... arguments) {
		if (!this.debug()) return;
		this.getLogger().debug(format, arguments);
	}

	public default void debug(
			Throwable throwable,
			String message) {
		this.getLogger().debug(throwable, message);
	}

	public default void debug(
			Throwable throwable,
			String format,
			Object... arguments) {
		if (!this.debug()) return;
		this.getLogger().debug(throwable, format, arguments);
	}

	public default boolean information() {
		return this.getLogger().information();
	}

	public default void information(
			String message) {
		this.getLogger().information(message);
	}

	public default void information(
			String format,
			Object... arguments) {
		if (!this.information()) return;
		this.getLogger().information(format, arguments);
	}

	public default void information(
			Throwable throwable,
			String message) {
		this.getLogger().information(throwable, message);
	}

	public default void information(
			Throwable throwable,
			String format,
			Object... arguments) {
		if (!this.information()) return;
		this.getLogger().information(throwable, format, arguments);
	}

	public default boolean warning() {
		return this.getLogger().warning();
	}

	public default void warning(
			String message) {
		this.getLogger().warning(message);
	}

	public default void warning(
			String format,
			Object... arguments) {
		if (!this.warning()) return;
		this.getLogger().warning(format, arguments);
	}

	public default void warning(
			Throwable throwable,
			String message) {
		this.getLogger().warning(throwable, message);
	}

	public default void warning(
			Throwable throwable,
			String format,
			Object... arguments) {
		if (!this.warning()) return;
		this.getLogger().warning(throwable, format, arguments);
	}

	public default boolean error() {
		return this.getLogger().error();
	}

	public default void error(
			String message) {
		this.getLogger().error(message);
	}

	public default void error(
			String format,
			Object... arguments) {
		if (!this.error()) return;
		this.getLogger().error(format, arguments);
	}

	public default void error(
			Throwable throwable,
			String message) {
		this.getLogger().error(throwable, message);
	}

	public default void error(
			Throwable throwable,
			String format,
			Object... arguments) {
		if (!this.error()) return;
		this.getLogger().error(throwable, format, arguments);
	}

	public default boolean fatal() {
		return this.getLogger().fatal();
	}

	public default void fatal(
			String message) {
		this.getLogger().fatal(message);
	}

	public default void fatal(
			String format,
			Object... arguments) {
		if (!this.fatal()) return;
		this.getLogger().fatal(format, arguments);
	}

	public default void fatal(
			Throwable throwable,
			String message) {
		this.getLogger().fatal(throwable, message);
	}

	public default void fatal(
			Throwable throwable,
			String format,
			Object... arguments) {
		if (!this.fatal()) return;
		this.getLogger().fatal(throwable, format, arguments);
	}

}

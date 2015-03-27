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

	public Logger logger();

	public default boolean trace() {
		return this.logger().trace();
	}

	public default void trace(
			String message) {
		this.logger().trace(message);
	}

	public default void trace(
			String format,
			Object... arguments) {
		if (!this.trace()) return;
		this.logger().trace(format, arguments);
	}

	public default void trace(
			Throwable throwable,
			String message) {
		this.logger().trace(throwable, message);
	}

	public default void trace(
			Throwable throwable,
			String format,
			Object... arguments) {
		if (!this.trace()) return;
		this.logger().trace(throwable, format, arguments);
	}


	public default boolean debug() {
		return this.logger().debug();
	}

	public default void debug(
			String message) {
		this.logger().debug(message);
	}

	public default void debug(
			String format,
			Object... arguments) {
		if (!this.debug()) return;
		this.logger().debug(format, arguments);
	}

	public default void debug(
			Throwable throwable,
			String message) {
		this.logger().debug(throwable, message);
	}

	public default void debug(
			Throwable throwable,
			String format,
			Object... arguments) {
		if (!this.debug()) return;
		this.logger().debug(throwable, format, arguments);
	}

	public default boolean information() {
		return this.logger().information();
	}

	public default void information(
			String message) {
		this.logger().information(message);
	}

	public default void information(
			String format,
			Object... arguments) {
		if (!this.information()) return;
		this.logger().information(format, arguments);
	}

	public default void information(
			Throwable throwable,
			String message) {
		this.logger().information(throwable, message);
	}

	public default void information(
			Throwable throwable,
			String format,
			Object... arguments) {
		if (!this.information()) return;
		this.logger().information(throwable, format, arguments);
	}

	public default boolean warning() {
		return this.logger().warning();
	}

	public default void warning(
			String message) {
		this.logger().warning(message);
	}

	public default void warning(
			String format,
			Object... arguments) {
		if (!this.warning()) return;
		this.logger().warning(format, arguments);
	}

	public default void warning(
			Throwable throwable,
			String message) {
		this.logger().warning(throwable, message);
	}

	public default void warning(
			Throwable throwable,
			String format,
			Object... arguments) {
		if (!this.warning()) return;
		this.logger().warning(throwable, format, arguments);
	}

	public default boolean error() {
		return this.logger().error();
	}

	public default void error(
			String message) {
		this.logger().error(message);
	}

	public default void error(
			String format,
			Object... arguments) {
		if (!this.error()) return;
		this.logger().error(format, arguments);
	}

	public default void error(
			Throwable throwable,
			String message) {
		this.logger().error(throwable, message);
	}

	public default void error(
			Throwable throwable,
			String format,
			Object... arguments) {
		if (!this.error()) return;
		this.logger().error(throwable, format, arguments);
	}

	public default boolean fatal() {
		return this.logger().fatal();
	}

	public default void fatal(
			String message) {
		this.logger().fatal(message);
	}

	public default void fatal(
			String format,
			Object... arguments) {
		if (!this.fatal()) return;
		this.logger().fatal(format, arguments);
	}

	public default void fatal(
			Throwable throwable,
			String message) {
		this.logger().fatal(throwable, message);
	}

	public default void fatal(
			Throwable throwable,
			String format,
			Object... arguments) {
		if (!this.fatal()) return;
		this.logger().fatal(throwable, format, arguments);
	}

}

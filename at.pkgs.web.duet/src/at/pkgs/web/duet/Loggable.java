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

package at.pkgs.web.duet;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;

public interface Loggable {

	public Logger logger();

	public default boolean trace(Marker marker) {
		return this.logger().isTraceEnabled(marker);
	}

	public default void trace(
			Marker marker,
			Object message) {
		this.logger().trace(
				marker,
				message);
	}

	public default void trace(
			Marker marker,
			Throwable throwable,
			Object message) {
		this.logger().trace(
				marker,
				message,
				throwable);
	}

	public default void trace(
			Marker marker,
			String message) {
		this.logger().trace(
				marker,
				message);
	}

	public default void trace(
			Marker marker,
			String format,
			Object... parameters) {
		if (!this.trace(marker)) return;
		this.logger().trace(
				marker,
				String.format(format, parameters));
	}

	public default void trace(
			Marker marker,
			Throwable throwable,
			String message) {
		this.logger().trace(
				marker,
				message,
				throwable);
	}

	public default void trace(
			Marker marker,
			Throwable throwable,
			String format,
			Object... parameters) {
		if (!this.trace(marker)) return;
		this.logger().trace(
				marker,
				String.format(format, parameters),
				throwable);
	}

	public default boolean trace() {
		return this.logger().isTraceEnabled();
	}

	public default void trace(
			Object message) {
		this.logger().trace(
				message);
	}

	public default void trace(
			Throwable throwable,
			Object message) {
		this.logger().trace(
				message,
				throwable);
	}

	public default void trace(
			String message) {
		this.logger().trace(
				message);
	}

	public default void trace(
			String format,
			Object... parameters) {
		if (!this.trace()) return;
		this.logger().trace(
				String.format(format, parameters));
	}

	public default void trace(
			Throwable throwable,
			String message) {
		this.logger().trace(
				message,
				throwable);
	}

	public default void trace(
			Throwable throwable,
			String format,
			Object... parameters) {
		if (!this.trace()) return;
		this.logger().trace(
				String.format(format, parameters),
				throwable);
	}

	public default boolean debug(Marker marker) {
		return this.logger().isDebugEnabled(marker);
	}

	public default void debug(
			Marker marker,
			Object message) {
		this.logger().debug(
				marker,
				message);
	}

	public default void debug(
			Marker marker,
			Throwable throwable,
			Object message) {
		this.logger().debug(
				marker,
				message,
				throwable);
	}

	public default void debug(
			Marker marker,
			String message) {
		this.logger().debug(
				marker,
				message);
	}

	public default void debug(
			Marker marker,
			String format,
			Object... parameters) {
		if (!this.debug(marker)) return;
		this.logger().debug(
				marker,
				String.format(format, parameters));
	}

	public default void debug(
			Marker marker,
			Throwable throwable,
			String message) {
		this.logger().debug(
				marker,
				message,
				throwable);
	}

	public default void debug(
			Marker marker,
			Throwable throwable,
			String format,
			Object... parameters) {
		if (!this.debug(marker)) return;
		this.logger().debug(
				marker,
				String.format(format, parameters),
				throwable);
	}

	public default boolean debug() {
		return this.logger().isDebugEnabled();
	}

	public default void debug(
			Object message) {
		this.logger().debug(
				message);
	}

	public default void debug(
			Throwable throwable,
			Object message) {
		this.logger().debug(
				message,
				throwable);
	}

	public default void debug(
			String message) {
		this.logger().debug(
				message);
	}

	public default void debug(
			String format,
			Object... parameters) {
		if (!this.debug()) return;
		this.logger().debug(
				String.format(format, parameters));
	}

	public default void debug(
			Throwable throwable,
			String message) {
		this.logger().debug(
				message,
				throwable);
	}

	public default void debug(
			Throwable throwable,
			String format,
			Object... parameters) {
		if (!this.debug()) return;
		this.logger().debug(
				String.format(format, parameters),
				throwable);
	}

	public default boolean info(Marker marker) {
		return this.logger().isInfoEnabled(marker);
	}

	public default void info(
			Marker marker,
			Object message) {
		this.logger().info(
				marker,
				message);
	}

	public default void info(
			Marker marker,
			Throwable throwable,
			Object message) {
		this.logger().info(
				marker,
				message,
				throwable);
	}

	public default void info(
			Marker marker,
			String message) {
		this.logger().info(
				marker,
				message);
	}

	public default void info(
			Marker marker,
			String format,
			Object... parameters) {
		if (!this.info(marker)) return;
		this.logger().info(
				marker,
				String.format(format, parameters));
	}

	public default void info(
			Marker marker,
			Throwable throwable,
			String message) {
		this.logger().info(
				marker,
				message,
				throwable);
	}

	public default void info(
			Marker marker,
			Throwable throwable,
			String format,
			Object... parameters) {
		if (!this.info(marker)) return;
		this.logger().info(
				marker,
				String.format(format, parameters),
				throwable);
	}

	public default boolean info() {
		return this.logger().isInfoEnabled();
	}

	public default void info(
			Object message) {
		this.logger().info(
				message);
	}

	public default void info(
			Throwable throwable,
			Object message) {
		this.logger().info(
				message,
				throwable);
	}

	public default void info(
			String message) {
		this.logger().info(
				message);
	}

	public default void info(
			String format,
			Object... parameters) {
		if (!this.info()) return;
		this.logger().info(
				String.format(format, parameters));
	}

	public default void info(
			Throwable throwable,
			String message) {
		this.logger().info(
				message,
				throwable);
	}

	public default void info(
			Throwable throwable,
			String format,
			Object... parameters) {
		if (!this.info()) return;
		this.logger().info(
				String.format(format, parameters),
				throwable);
	}

	public default boolean warn(Marker marker) {
		return this.logger().isWarnEnabled(marker);
	}

	public default void warn(
			Marker marker,
			Object message) {
		this.logger().warn(
				marker,
				message);
	}

	public default void warn(
			Marker marker,
			Throwable throwable,
			Object message) {
		this.logger().warn(
				marker,
				message,
				throwable);
	}

	public default void warn(
			Marker marker,
			String message) {
		this.logger().warn(
				marker,
				message);
	}

	public default void warn(
			Marker marker,
			String format,
			Object... parameters) {
		if (!this.warn(marker)) return;
		this.logger().warn(
				marker,
				String.format(format, parameters));
	}

	public default void warn(
			Marker marker,
			Throwable throwable,
			String message) {
		this.logger().warn(
				marker,
				message,
				throwable);
	}

	public default void warn(
			Marker marker,
			Throwable throwable,
			String format,
			Object... parameters) {
		if (!this.warn(marker)) return;
		this.logger().warn(
				marker,
				String.format(format, parameters),
				throwable);
	}

	public default boolean warn() {
		return this.logger().isWarnEnabled();
	}

	public default void warn(
			Object message) {
		this.logger().warn(
				message);
	}

	public default void warn(
			Throwable throwable,
			Object message) {
		this.logger().warn(
				message,
				throwable);
	}

	public default void warn(
			String message) {
		this.logger().warn(
				message);
	}

	public default void warn(
			String format,
			Object... parameters) {
		if (!this.warn()) return;
		this.logger().warn(
				String.format(format, parameters));
	}

	public default void warn(
			Throwable throwable,
			String message) {
		this.logger().warn(
				message,
				throwable);
	}

	public default void warn(
			Throwable throwable,
			String format,
			Object... parameters) {
		if (!this.warn()) return;
		this.logger().warn(
				String.format(format, parameters),
				throwable);
	}

	public default boolean error(Marker marker) {
		return this.logger().isErrorEnabled(marker);
	}

	public default void error(
			Marker marker,
			Object message) {
		this.logger().error(
				marker,
				message);
	}

	public default void error(
			Marker marker,
			Throwable throwable,
			Object message) {
		this.logger().error(
				marker,
				message,
				throwable);
	}

	public default void error(
			Marker marker,
			String message) {
		this.logger().error(
				marker,
				message);
	}

	public default void error(
			Marker marker,
			String format,
			Object... parameters) {
		if (!this.error(marker)) return;
		this.logger().error(
				marker,
				String.format(format, parameters));
	}

	public default void error(
			Marker marker,
			Throwable throwable,
			String message) {
		this.logger().error(
				marker,
				message,
				throwable);
	}

	public default void error(
			Marker marker,
			Throwable throwable,
			String format,
			Object... parameters) {
		if (!this.error(marker)) return;
		this.logger().error(
				marker,
				String.format(format, parameters),
				throwable);
	}

	public default boolean error() {
		return this.logger().isErrorEnabled();
	}

	public default void error(
			Object message) {
		this.logger().error(
				message);
	}

	public default void error(
			Throwable throwable,
			Object message) {
		this.logger().error(
				message,
				throwable);
	}

	public default void error(
			String message) {
		this.logger().error(
				message);
	}

	public default void error(
			String format,
			Object... parameters) {
		if (!this.error()) return;
		this.logger().error(
				String.format(format, parameters));
	}

	public default void error(
			Throwable throwable,
			String message) {
		this.logger().error(
				message,
				throwable);
	}

	public default void error(
			Throwable throwable,
			String format,
			Object... parameters) {
		if (!this.error()) return;
		this.logger().error(
				String.format(format, parameters),
				throwable);
	}

	public default boolean fatal(Marker marker) {
		return this.logger().isFatalEnabled(marker);
	}

	public default void fatal(
			Marker marker,
			Object message) {
		this.logger().fatal(
				marker,
				message);
	}

	public default void fatal(
			Marker marker,
			Throwable throwable,
			Object message) {
		this.logger().fatal(
				marker,
				message,
				throwable);
	}

	public default void fatal(
			Marker marker,
			String message) {
		this.logger().fatal(
				marker,
				message);
	}

	public default void fatal(
			Marker marker,
			String format,
			Object... parameters) {
		if (!this.fatal(marker)) return;
		this.logger().fatal(
				marker,
				String.format(format, parameters));
	}

	public default void fatal(
			Marker marker,
			Throwable throwable,
			String message) {
		this.logger().fatal(
				marker,
				message,
				throwable);
	}

	public default void fatal(
			Marker marker,
			Throwable throwable,
			String format,
			Object... parameters) {
		if (!this.fatal(marker)) return;
		this.logger().fatal(
				marker,
				String.format(format, parameters),
				throwable);
	}

	public default boolean fatal() {
		return this.logger().isFatalEnabled();
	}

	public default void fatal(
			Object message) {
		this.logger().fatal(
				message);
	}

	public default void fatal(
			Throwable throwable,
			Object message) {
		this.logger().fatal(
				message,
				throwable);
	}

	public default void fatal(
			String message) {
		this.logger().fatal(
				message);
	}

	public default void fatal(
			String format,
			Object... parameters) {
		if (!this.fatal()) return;
		this.logger().fatal(
				String.format(format, parameters));
	}

	public default void fatal(
			Throwable throwable,
			String message) {
		this.logger().fatal(
				message,
				throwable);
	}

	public default void fatal(
			Throwable throwable,
			String format,
			Object... parameters) {
		if (!this.fatal()) return;
		this.logger().fatal(
				String.format(format, parameters),
				throwable);
	}

}

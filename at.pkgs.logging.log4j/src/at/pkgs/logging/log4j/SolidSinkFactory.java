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

package at.pkgs.logging.log4j;

import org.apache.logging.log4j.LogManager;
import at.pkgs.logging.SinkFactory;
import at.pkgs.logging.Sink;

public class SolidSinkFactory implements SinkFactory {

	@Override
	public Sink sink(String name) {
		return new SolidSink(LogManager.getLogger(name));
	}

}

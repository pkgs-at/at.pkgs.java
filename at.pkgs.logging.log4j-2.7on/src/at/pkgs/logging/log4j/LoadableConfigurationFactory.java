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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
//import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
//import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;

/*
@Plugin(
		name = "LoadableConfigurationFactory",
		category = "ConfigurationFactory")
@Order(10)
 */
public class LoadableConfigurationFactory extends ConfigurationFactory {

	private static final String[] SUFFIXES = new String[] {".xml", "*"};

	private static File file;

	@Override
	protected String[] getSupportedTypes() {
		return LoadableConfigurationFactory.SUFFIXES;
	}

	@Override
	public Configuration getConfiguration(
			LoggerContext context,
			ConfigurationSource source) {
		File file;

		synchronized(LoadableConfigurationFactory.class) {
			file = LoadableConfigurationFactory.file;
		}
		if (file != null) {
			try {
				source =
						new ConfigurationSource(
								new FileInputStream(file),
								file);
			}
			catch (IOException throwable) {
				throw new RuntimeException(throwable);
			}
		}
		return new XmlConfiguration(context, source);
	}

	public static void load(File file) {
		synchronized (LoadableConfigurationFactory.class) {
			LoadableConfigurationFactory.file = file;
		}
		((LoggerContext)LogManager.getContext()).reconfigure();
	}

}

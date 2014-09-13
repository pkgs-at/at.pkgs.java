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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.xml.XmlConfiguration;

public class LoggingManager extends AbstractManager {

	@org.apache.logging.log4j.core.config.plugins.Plugin(
			name = "LoggingManagerConfigurationFactory",
			category = "ConfigurationFactory")
	@org.apache.logging.log4j.core.config.Order(10)
	public static class ConfigurationFactory
	extends org.apache.logging.log4j.core.config.ConfigurationFactory {

		private static final String[] SUFFIXES = new String[] {".xml", "*"};

		private static File file;

		@Override
		protected String[] getSupportedTypes() {
			return ConfigurationFactory.SUFFIXES;
		}

		@Override
		public Configuration getConfiguration(ConfigurationSource source) {
			File file;

			synchronized(ConfigurationFactory.class) {
				file = ConfigurationFactory.file;
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
			return new XmlConfiguration(source);
		}

	}

	public LoggingManager(AbstractApplication application) {
		super(application);
		this.reload();
	}

	public void reload() {
		File file;

		file = this.getApplication()
				.getConfigurationManager()
				.getConfigurationFile("log4j2.xml");
		synchronized (ConfigurationFactory.class) {
			ConfigurationFactory.file = file;
		}
		((LoggerContext)LogManager.getContext()).reconfigure();
	}

}

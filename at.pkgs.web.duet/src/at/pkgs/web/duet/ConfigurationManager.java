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
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.ConfigurationException;

public class ConfigurationManager extends AbstractManager {

	private final File configurationDirectory;

	private final PropertiesConfiguration configuration;

	public ConfigurationManager(AbstractApplication application) {
		super(application);
		this.configurationDirectory =
				new File(
						application.getProperty(
								"configuration",
								this.getServletContext().getRealPath(
										"/WEB-INF")));
		this.configuration = new PropertiesConfiguration();
		try {
			this.configuration.load(
					this.getConfigurationFile(
							"application.properties"));
		}
		catch (ConfigurationException throwable) {
			this.error(
					throwable,
					"failed on load configuration from %s",
					this.getConfigurationFile("application.properties"));
			throw new RuntimeException(throwable);
		}
	}

	public File getConfigurationDirectory() {
		return this.configurationDirectory;
	}

	public File getConfigurationFile(String name) {
		return new File(this.getConfigurationDirectory(), name);
	}

	public Configuration get(String prefix) {
		return this.configuration.subset(prefix);
	}

}
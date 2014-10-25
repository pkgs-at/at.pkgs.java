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
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.ConfigurationException;

public class ConfigurationManager extends AbstractManager {

	private final File configurationDirectory;

	private final CompositeConfiguration configuration;

	public ConfigurationManager(AbstractApplication application) {
		super(application);
		this.configurationDirectory = this.getConfigurationParameter();
		this.configuration = new CompositeConfiguration();
		{
			File file;

			file = this.getConfigurationFile("application.local.properties");
			if (file.exists()) {
				PropertiesConfiguration properties;

				properties = new PropertiesConfiguration();
				properties.setEncoding("UTF-8");
				try {
					properties.load(file);
				}
				catch (ConfigurationException throwable) {
					this.error(
							throwable,
							"failed on load configuration from %s",
							file);
					throw new RuntimeException(throwable);
				}
				this.configuration.addConfiguration(properties);
			}
		}
		{
			File file;
			PropertiesConfiguration properties;

			file = this.getConfigurationFile("application.properties");
			properties = new PropertiesConfiguration();
			properties.setEncoding("UTF-8");
			try {
				properties.load(file);
			}
			catch (ConfigurationException throwable) {
				this.error(
						throwable,
						"failed on load configuration from %s",
						file);
				throw new RuntimeException(throwable);
			}
			this.configuration.addConfiguration(properties);
		}
		this.configuration.addConfiguration(new SystemConfiguration());
	}

	protected File getConfigurationParameter() {
		String value;

		value = this.getApplication().getServletContext().getInitParameter(
				"at.pkgs.web.duet.configuration");
		if (value != null)
			return new File(value);
		value = this.getApplication().getProperty(
				"configuration",
				(String)null);
		if (value != null)
			return new File(value);
		value = System.getProperty("at.pkgs.web.duet.configuration_root");
		if (value != null)
			return new File(
					value,
					this.getApplication().getClass().getPackage().getName());
		else
			return new File(
					this.getServletContext().getRealPath("/WEB-INF"));
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

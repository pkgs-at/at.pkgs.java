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

import java.util.Base64;
import java.util.EnumSet;
import java.util.Properties;
import java.io.File;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.configuration.Configuration;
import at.pkgs.web.http.HttpServlet;
import at.pkgs.web.http.HttpRequest;
import at.pkgs.web.http.HttpResponse;
import eu.medsea.mimeutil.MimeUtil2;

public class ServletManager extends AbstractManager {

	public static class BasicAuthorizationFilter implements Filter {

		private String realm;

		private File credentials;

		private void initialize(ServletManager manager) {
			Configuration configuration;

			configuration =
					manager.configuration.subset("BasicAuthorizationFilter");
			this.realm = configuration.getString("realm", "");
			this.credentials = manager.getApplication()
					.getConfigurationManager()
					.getConfigurationFile(
							"authorization.properties");
		}

		@Override
		public void init(
				FilterConfig config)
						throws ServletException {
			// do nothing
		}

		protected boolean authorize(
				String authorization)
						throws IOException {
			Properties credentials;
			String[] credential;

			if (authorization == null) return false;
			if (!authorization.startsWith("Basic ")) return false;
			credential =
					new String(
							Base64.getDecoder().decode(
									authorization.substring(6))).split(":", 2);
			if (credential.length != 2) return false;
			credentials = new Properties();
			try (InputStream input =
					new BufferedInputStream((
							new FileInputStream(
									this.credentials)))) {
				credentials.load(input);
			}
			if (credential[1].equals(credentials.getProperty(credential[0])))
				return true;
			else
				return false;
		}

		protected boolean authorize(
				HttpServletRequest request,
				HttpServletResponse response)
						throws IOException, ServletException {
			HttpSession session;

			session = request.getSession(false);
			if (session != null) {
				Boolean authorized;

				authorized =
						(Boolean)session.getAttribute(
								this.getClass().getName());
				if (authorized != null &&authorized == Boolean.TRUE)
					return true;
			}
			if (this.authorize(request.getHeader("Authorization"))) {
				session = request.getSession(true);
				session.setAttribute(this.getClass().getName(), Boolean.TRUE);
				return true;
			}
			else {
				if (session != null) session.invalidate();
				response.setHeader(
						"WWW-Authenticate",
						"BASIC realm=\"" + this.realm + "\"");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				return false;
			}
		}

		@Override
		public void doFilter(
				ServletRequest request,
				ServletResponse response,
				FilterChain chain)
						throws IOException, ServletException {
			boolean authorized;

			authorized =
					this.authorize(
							(HttpServletRequest)request,
							(HttpServletResponse)response);
			if (authorized) chain.doFilter(request, response);
		}

		@Override
		public void destroy() {
			// do nothing
		}

	}

	public static class MimeDetector {

		private static final String PROPERTIES =
				"/eu/medsea/mimeutil/mime-types.properties";

		private static final String DEFAULT_MIMETYPE =
				MimeUtil2.UNKNOWN_MIME_TYPE.toString();

		private Properties list;

		private MimeDetector() {
			this.list = new Properties();
			try (InputStream input =
					this.getClass().getResourceAsStream(
							MimeDetector.PROPERTIES)) {
				this.list.load(input);
			}
			catch (Throwable throwable) {
				throw new RuntimeException(throwable);
			}
		}

		private String detect(String extension) {
			String types;
			int end;

			if (!this.list.containsKey(extension))
				return MimeDetector.DEFAULT_MIMETYPE;
			types = this.list.getProperty(extension);
			end = types.indexOf(',');
			if (end >= 0) return types.substring(0, end);
			else return types;
		}

		private static final MimeDetector instance = new MimeDetector();

		public static String detectByExtension(String extension) {
			return MimeDetector.instance.detect(
					extension);
		}

		public static String detectByPath(String path) {
			int start;

			start = path.lastIndexOf('.') + 1;
			if (start <= 0) return MimeDetector.DEFAULT_MIMETYPE;
			else return MimeDetector.detectByExtension(path.substring(start));
		}

		public static String detectByUrl(URL location) {
			return MimeDetector.detectByPath(location.getPath());
		}

	}

	public static class DefaultServlet extends HttpServlet {

		private static final long serialVersionUID = 1L;

		@Override
		protected void service(
				HttpRequest request,
				HttpResponse response)
						throws ServletException, IOException {
			String path;
			URL resource;

			if (!request.methodIs("GET"))
				throw new UnsupportedOperationException();
			path = request.getPathInfo();
			resource = this.getServletContext().getResource(path);
			if (resource == null) {
				response.sendError(
						HttpResponse.SC_NOT_FOUND,
						String.format(
								"resource not found: %s",
								path));
				return;
			}
			response.sendResponse(
					MimeDetector.detectByUrl(resource),
					resource);
		}

	}

	private final Configuration configuration;

	public ServletManager(AbstractApplication application)
			throws ServletException {
		super(application);
		this.configuration = this.getApplication()
				.getConfigurationManager()
				.get("ServletManager");
		if (this.enabled(BasicAuthorizationFilter.class)) {
			BasicAuthorizationFilter filter;

			filter =
					this.getServletContext().createFilter(
							BasicAuthorizationFilter.class);
			filter.initialize(this);
			this.getServletContext()
					.addFilter(
							"BasicAuthorizationFilter",
							filter)
					.addMappingForUrlPatterns(
							EnumSet.of(DispatcherType.REQUEST),
							false,
							"/*");
		}
		if (this.enabled(DefaultServlet.class)) {
			this.getServletContext()
					.addServlet(
							"DefaultServlet",
							DefaultServlet.class)
					.addMapping(
							"/*");
		}
	}

	protected boolean enabled(Class<?> type) {
		return this.configuration.getBoolean(
				"use" + type.getSimpleName(),
				false);
	}

}

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

package at.pkgs.web.http;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpServlet extends javax.servlet.http.HttpServlet {

	protected void service(
			HttpRequest request,
			HttpResponse response)
					throws ServletException, IOException {
		super.service(request, response);
	}

	@Override
	protected void service(
			HttpServletRequest rawRequest,
			HttpServletResponse rawResponse)
					throws ServletException, IOException {
		HttpRequest request;
		HttpResponse response;

		request = new HttpRequest(rawRequest);
		response = new HttpResponse(request, rawResponse);
		this.service(request, response);
	}

	private static final long serialVersionUID = 1L;

}

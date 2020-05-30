package com.miiguar.hfms.view.registration;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.miiguar.hfms.config.ConfigureDb;
import com.miiguar.hfms.data.models.user.UserRequest;
import com.miiguar.hfms.data.models.user.UserResponse;
import com.miiguar.hfms.data.models.user.model.User;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.utils.Constants;
import com.miiguar.hfms.utils.Log;
import com.miiguar.hfms.view.base.BaseServlet;
import com.miiguar.hfms.view.result.ErrorResults;

/**
 * Servlet implementation class RegistrationServlet
 */
@WebServlet(urlPatterns = "/registration/register-user", asyncSupported = true)
public class RegistrationServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = RegistrationServlet.class.getSimpleName();
	private static final String EMAIL = "email";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		Log.d(TAG, "Request Received.", logger);

		try {
			includeRequest(request, response);
		} catch (final IOException e) {
			Log.e(TAG, "Error", logger, e);
		}
	}

	public void includeRequest(final HttpServletRequest request, final HttpServletResponse response)
			throws IOException {
		boolean isParamsValid = true;
		PrintWriter writer = null;
		if (request.getMethod().equalsIgnoreCase("POST")) {
			final String email = request.getParameter(EMAIL) == null ? "" : request.getParameter(EMAIL).trim();
			final String username = request.getParameter(USERNAME) == null ? "" : request.getParameter(USERNAME).trim();
			final String password = request.getParameter(PASSWORD) == null ? "" : request.getParameter(PASSWORD);

			final ErrorResults results = new ErrorResults();
			if (email.isEmpty()) {
				results.setEmailError("Email is required!");
				isParamsValid = false;
			}

			if (username.isEmpty()) {
				results.setUsernameError("Username is required!");
				isParamsValid = false;
			}

			if (password.isEmpty()) {
				results.setPasswordError("Password is required!");
				isParamsValid = false;
			}

			if (isParamsValid) {

				if (Constants.EMAIL_VERIFICATION_PATTERN.matcher(email).matches()) {

					final String usernameValidity = Constants.isUsernameValid(username);
					if (usernameValidity.isEmpty()) {
						Log.d(TAG, "path: " + new File(".").getAbsolutePath(), logger);

						final String validity = Constants.isPasswordValid(password);
						if (validity.isEmpty()) {
							final User user = new User();
							user.setAdmin(true);
							user.setEmail(request.getParameter(EMAIL));
							user.setUsername(request.getParameter(USERNAME));
							user.setPassword(request.getParameter(PASSWORD));
							final UserRequest params = new UserRequest(user);

							final Gson gson = new Gson();
							final String jsoStr = gson.toJson(params);

							ConfigureDb conf = new ConfigureDb();
							Properties properties = conf.readProperties();
							String baseUrl = properties.getProperty("hfms.baseUrl");

							final URL url = new URL(baseUrl + "registration");
							final URLConnection connection = url.openConnection();
							connection.setDoOutput(true);
							final OutputStreamWriter outputLine = new OutputStreamWriter(connection.getOutputStream());
							outputLine.write(jsoStr);
							outputLine.flush();

							// Get the response
							final BufferedReader streamReader = new BufferedReader(
									new InputStreamReader(connection.getInputStream()));
							String line;
							// streamReader = holding the data... can put it through a DOM loader?
							UserResponse item = null;
							while ((line = streamReader.readLine()) != null) {
								item = gson.fromJson(line, UserResponse.class);
							}

							if (item != null) {
								if (item.getReport().getStatus() != 200) {
									Report rep = item.getReport();
									String out = gson.toJson(rep);
									response.setStatus(item.getReport().getStatus());
									writer = response.getWriter();
									writer.write(out);
								} else {
									request.getSession().setAttribute(EMAIL, item.getUser().getEmail());
									request.getSession().setAttribute(USERNAME, item.getUser().getUsername());
									response.sendRedirect("/registration/confirm-user");
								}
							}
							outputLine.close();
							streamReader.close();
						} else {

							final ErrorResults pass = new ErrorResults();
							pass.setPasswordError("You password should have these properties:</br>" + validity);
							response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
							final String error = gson.toJson(pass);
							writer = response.getWriter();
							writer.write(error);
						}
					} else {

						final ErrorResults pass = new ErrorResults();
						pass.setUsernameError(usernameValidity);
						final String error = gson.toJson(pass);
						response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						writer = response.getWriter();
						writer.write(error);
					}
				} else {

					final ErrorResults pass = new ErrorResults();
					pass.setEmailError("Your email is invalid");
					final String error = gson.toJson(pass);
					response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					writer = response.getWriter();
					writer.write(error);
				}
			} else {
				final String error = gson.toJson(results);
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				writer = response.getWriter();
				writer.write(error);
			}
		}
	}
}

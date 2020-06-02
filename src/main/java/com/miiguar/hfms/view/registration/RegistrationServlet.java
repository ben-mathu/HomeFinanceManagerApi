package com.miiguar.hfms.view.registration;

import java.io.*;
import java.lang.reflect.Type;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.reflect.TypeToken;
import com.miiguar.hfms.data.models.user.UserRequest;
import com.miiguar.hfms.data.models.user.UserResponse;
import com.miiguar.hfms.data.models.user.model.User;
import com.miiguar.hfms.utils.Patterns;
import com.miiguar.hfms.utils.InitUrlConnection;
import com.miiguar.hfms.utils.Log;
import com.miiguar.hfms.view.base.BaseServlet;
import com.miiguar.hfms.view.result.ErrorResults;

import static com.miiguar.hfms.data.utils.DbEnvironment.COL_USER_ID;
import static com.miiguar.hfms.data.utils.URL.REGISTRATION;
import static com.miiguar.hfms.utils.Constants.*;

/**
 * Servlet implementation class RegistrationServlet
 */
@WebServlet(urlPatterns = "/registration/register-user", asyncSupported = true)
public class RegistrationServlet extends BaseServlet {
	private static final long serialVersionUID = 1L;
	private static final String TAG = RegistrationServlet.class.getSimpleName();

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
			throws ServletException, IOException {
		Log.d(TAG, "Request Received.");

		includeRequest(request, response);
	}

	public void includeRequest(final HttpServletRequest request, final HttpServletResponse response)
			throws IOException {
		boolean isParamsValid = true;
		PrintWriter writer = null;
		
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

			final String usernameValidity = Patterns.isUsernameValid(username);
			final String validity = Patterns.isPasswordValid(password);
			if (!Patterns.EMAIL_VERIFICATION_PATTERN.matcher(email).matches()) {

				final ErrorResults pass = new ErrorResults();
				pass.setEmailError("Your email is invalid");
				final String error = gson.toJson(pass);
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				writer = response.getWriter();
				writer.write(error);
			} else if (!usernameValidity.isEmpty()) {

				final ErrorResults pass = new ErrorResults();
				pass.setUsernameError(usernameValidity);
				final String error = gson.toJson(pass);
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				writer = response.getWriter();
				writer.write(error);
			} else if (!validity.isEmpty()) {

				final ErrorResults pass = new ErrorResults();
				pass.setPasswordError("You password should have these properties:</br>" + validity);
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				final String error = gson.toJson(pass);
				writer = response.getWriter();
				writer.write(error);
				
			} else {

				final User user = new User();
				user.setAdmin(true);
				user.setEmail(request.getParameter(EMAIL));
				user.setUsername(request.getParameter(USERNAME));
				user.setPassword(request.getParameter(PASSWORD));
				final UserRequest params = new UserRequest(user);

				InitUrlConnection<UserRequest, UserResponse> connection = new InitUrlConnection<>();
				BufferedReader streamReader = connection.getReader(params, REGISTRATION);

				String line = "";
				UserResponse item = null;
				while((line = streamReader.readLine()) != null) {
					item = gson.fromJson(line, UserResponse.class);
				}

				if (item != null) {
					if (item.getReport().getStatus() != 200) {
						String jsonStr = gson.toJson(item.getReport());
						response.setStatus(item.getReport().getStatus());
						writer = response.getWriter();
						writer.write(jsonStr);

					} else {

						request.getSession().setAttribute(USERNAME, item.getUser().getUsername());
						request.getSession().setAttribute(EMAIL, item.getUser().getEmail());
						request.getSession().setAttribute(PASSWORD, item.getUser().getPassword());
						request.getSession().setAttribute(TOKEN, item.getReport().getToken());
						request.getSession().setAttribute(COL_USER_ID, item.getUser().getUserId());
						writer = response.getWriter();
						String redirect = request.getContextPath() + "/registration/confirm-user";
						writer.write(redirect);
					}
				}

				connection.close();
			}
		} else {
			final String error = gson.toJson(results);
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			writer = response.getWriter();
			writer.write(error);
		}
	}
}

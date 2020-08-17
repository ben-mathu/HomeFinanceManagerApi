package com.miiguar.hfms.view.registration;

import java.io.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.miiguar.hfms.data.household.model.Household;
import com.miiguar.hfms.data.user.UserRequest;
import com.miiguar.hfms.data.user.UserResponse;
import com.miiguar.hfms.data.user.model.User;
import com.miiguar.hfms.data.status.Report;
import com.miiguar.hfms.utils.Patterns;
import com.miiguar.hfms.utils.InitUrlConnection;
import com.miiguar.hfms.utils.Log;
import com.miiguar.hfms.view.base.BaseServlet;
import com.miiguar.hfms.view.result.ErrorResults;

import static com.miiguar.hfms.data.utils.DbEnvironment.*;
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
		
		final String email = request.getParameter(EMAIL).trim();
		final String username = request.getParameter(USERNAME).trim();
		final String password = request.getParameter(PASSWORD);
		final String householdName = request.getParameter(HOUSEHOLD_NAME);
		final String houseDesc = request.getParameter(HOUSEHOLD_DESCRIPTION);
		final boolean joinHousehold = Boolean.parseBoolean(request.getParameter("joinHousehold"));
		String houseHoldId = "";
		if (joinHousehold) {
			houseHoldId = request.getParameter(HOUSEHOLD_ID);
		}

		final ErrorResults results = new ErrorResults();
		if (email.isEmpty() || request.getParameter(EMAIL) == null) {
			results.setEmailError("Email is required!");
			isParamsValid = false;
		}

		if (username.isEmpty() || request.getParameter(USERNAME) == null) {
			results.setUsernameError("Username is required!");
			isParamsValid = false;
		}

		if (password.isEmpty() || request.getParameter(PASSWORD) == null) {
			results.setPasswordError("Password is required!");
			isParamsValid = false;
		}

		if ((houseHoldId.isEmpty() || request.getParameter(HOUSEHOLD_ID) == null) && joinHousehold) {
			results.setHouseholdIdError("");
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

				User user = new User();
				user.setAdmin(true);
				user.setEmail(request.getParameter(EMAIL));
				user.setUsername(request.getParameter(USERNAME));
				user.setPassword(request.getParameter(PASSWORD));
				Household household = new Household();
				if (joinHousehold) {
					household.setId(houseHoldId);
				} else {
					household.setName(householdName);
					household.setDescription(houseDesc);
				}

				final UserRequest params = new UserRequest();
				params.setHousehold(household);
				params.setUser(user);

				InitUrlConnection<UserRequest> connection = new InitUrlConnection<>();

				BufferedReader streamReader;
				if (joinHousehold) {
					streamReader = connection.getReader(params, REGISTRATION + "?joinHousehold=true", "POST");
				} else {
					streamReader = connection.getReader(params, REGISTRATION + "?joinHousehold=false", "POST");
				}

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
						request.getSession().setAttribute(USER_ID, item.getUser().getUserId());
						request.getSession().setAttribute(EMAIL, item.getUser().getEmail());
						request.getSession().setAttribute(TOKEN, item.getReport().getToken());
						request.getSession().setAttribute(HOUSEHOLD_NAME, item.getHousehold().getName());
						request.getSession().setAttribute("isAlreadySent", false);

						// save the session
						Cookie cookie = new Cookie(TOKEN, item.getReport().getToken());
						cookie.setMaxAge(60*60*24);
						response.addCookie(cookie);

						Cookie userIdCookie = new Cookie(USER_ID, item.getUser().getUserId());
						userIdCookie.setMaxAge(60*60*24);
						response.addCookie(userIdCookie);

						Cookie householdNameCookie = new Cookie(HOUSEHOLD_NAME, item.getHousehold().getName());
						userIdCookie.setMaxAge(60*60*24);
						response.addCookie(householdNameCookie);

						request.getSession().setAttribute("isAlreadySent", false);

						Report report = item.getReport();
						user = item.getUser();
						household  = item.getHousehold();
						UserResponse obj = new UserResponse();
						obj.setReport(report);
						obj.setUser(user);
						obj.setHousehold(household);

						String responseStr = gson.toJson(obj);

						writer = response.getWriter();
						writer.write(responseStr);
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

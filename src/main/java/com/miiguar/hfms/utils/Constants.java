package com.miiguar.hfms.utils;

import java.util.regex.Pattern;

/**
 * @author bernard
 */
public class Constants {
    private Constants() {
        throw new IllegalStateException("Utility class");
    }
    
    public static final String ISSUER = "miiguar";

    // URL
    public static final String API = "/api";


    // Session keys
    public static final String EMAIL = "email";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String TOKEN = "token";

    // Patterns
    public static final Pattern EMAIL_VERIFICATION_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]{2,}@[A-Z0-9-]{2,}\\.([A-Z]{2,6}|[A-Z]{2,6}\\.[A-Z]{2,6})$", Pattern.CASE_INSENSITIVE);

    public static final Pattern USERNAME_VALIDATION = Pattern.compile("[a-zA-Z0-9_-]{6,12}");

    public static final Pattern VALID_PASSWORD_2LC = Pattern.compile("(?=(.*[a-z])){2,}");
    public static final Pattern VALID_PASSWORD_2UC = Pattern.compile("(?=(.*[A-Z]){2,})");
    public static final Pattern VALID_PASSWORD_2DG = Pattern.compile("(?=(.*[0-9]){2,})");
    public static final Pattern VALID_PASSWORD_2SC = Pattern.compile("(?=(.*[ !\"#$%&'£()*+,\\-./:;<=>?@^_`{|}~])+)");
    public static final Boolean VALID_PASSWORD = false;

    public static String isUsernameValid(final String username) {
        final String usernameResp = "Your username should have these properties:</br>"
                + "- between 6 and 12 letters,</br>" + "- and not contain these characters:</br>"
                + "<span style=\"color: #FEC800;font-size:18px;\">[]()=,\"/?@:;</span>";

        if (!USERNAME_VALIDATION.matcher(username).matches()) {
            return usernameResp;
        } else
            return "";
    }

    public static String isPasswordValid(final String password) {
        final StringBuilder sb = new StringBuilder();

        if (password.toLowerCase().contains("password")) {
            sb.append("Should not contain <span style=\"color: #FEC800;\">password</span></br>");
        }

        if (password.length() < 8) {
            sb.append("- At least 7 characters</br>");
        }

        if (!VALID_PASSWORD_2LC.matcher(password).find()) {
            sb.append("- At least 2 Lowercase letters</br>");
        }

        if (!VALID_PASSWORD_2UC.matcher(password).find()) {
            sb.append("- At least 2 Uppercase letters</br>");
        }

        if (!VALID_PASSWORD_2SC.matcher(password).find()) {
            sb.append("- At least 1 special characters</br>");
        }

        if (!VALID_PASSWORD_2DG.matcher(password).find()) {
            sb.append("- And at least 2 digits.");
        }

        return sb.toString();
    }
}

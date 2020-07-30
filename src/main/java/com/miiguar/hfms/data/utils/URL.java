package com.miiguar.hfms.data.utils;

/**
 * @author bernard
 */
public class URL {
    public static final String API = "/api";

    /**
     * API Endpoints
     */
    public static final String CONFIRM = "/registration/confirm";
    public static final String REGISTRATION = "/registration";
    public static final String GENERATE_CODE = "/registration/generate-confirmation-code";
    public static final String CHANGE_EMAIL = "/change-email-address";
    public static final String EMAIL_CONFIRMATION = "/registration/confirm-user/email-confirmation";
    public static final String LOGIN = "/login-user";
    public static final String GET_CONFIRMATION_CODE = "/registration/confirm-user/get-confirmation-code";

    // Groceries HTTP Methods
    public static final String MONEY_JARS = "/jars/*";
    public static final String ADD_MONEY_JAR = "/jars/add-money-jar";
    public static final String GET_MONEY_JAR = "/jars/get-money-jar";
    public static final String GET_ALL_MONEY_JARS = "/jars/get-all-jars";

    public static final String USER_DETAILS = "/users/*";
    public static final String GET_USER_DETAILS = "/users/user";

    // Income
    public static final String INCOME_ENDPOINT = "/income/*";
    public static final String ADD_USER_INCOME = "/income/add-income";

    // Daraja
    public static final String GENERATE_TOKEN = "oauth/v1/generate";
}

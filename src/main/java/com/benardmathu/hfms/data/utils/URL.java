package com.benardmathu.hfms.data.utils;

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
    public static final String UPDATE_MONEY_JAR = "/jars/update-money-jar";
    public static final String DELETE_MONEY_JAR = "/jars/delete-money-jar";
    
    public static final String GROCERY_URL = "/groceries/*";
    public static final String DELETE_GROCERY = "/groceries/delete-grocery";

    public static final String USER_DETAILS = "/users/*";
    public static final String GET_USER_DETAILS = "/users/user";

    // Income
    public static final String INCOME_ENDPOINT = "/income/*";
    public static final String ADD_USER_INCOME = "/income/add-income";
    public static final String UPDATE_USER_INCOME = "/income/update-income";

    // Transactions
    public static final String TRANSACTIONS = "/transactions/*";
    public static final String SEND_TRANSACTION = "/transactions/send-transaction";
    public static final String GET_TRANSACTIONS = "/transactions/get-transactions";

    // Daraja
    // Request url
    public static final String GENERATE_TOKEN = "oauth/v1/generate";
    public static final String LNMO = "mpesa/stkpush/v1/processrequest";

    // Callback URLs
    public static final String MPESA_URL = "/mpesa/confirm-url";
    public static final String LNMO_CALLBACK_URL = "/mpesa/lnmo-url";
    public static final String BASE_URL = "https://b4d3c67b734b.ngrok.io:8000";
    
    // Change Account details
    public static final String CHANGE_ACCOUNT_URL = "/accounts/*";
    public static final String CHANGE_ACCOUNT_DETAILS = "/accounts/change-password";
    public static final String CHANGE_EMAIL_DETAILS = "/accounts/change-email";
    public static final String CHANGE_USERNAME_DETAILS = "/accounts/change-username";
    public static final String CHANGE_NUMBER_DETAILS = "/accounts/change-number";
    
    // Report
    public static final String REPORTS = "/reports/*";
    public static final String GET_REPORTS = "/reports/get-reports";
}

package com.miiguar.hfms.api.utils;

/**
 * Constants
 */
public class Constants {

    /**
     * User table
     */
    public static final String USERS_TB_NAME = "users";
    public static final String USER_ID = "user_id";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String IS_ADMIN = "is_admin";
    public static final String PRIV_KEY_USERS = "pk_username_email";

    /**
     * Code table
     */
    public static String CODE_TB_NAME = "user_gen_code";
    public static final String CODE = "code";
    // user_id to identify the user

    /**
     * Endpoints
     */
    public static final String CONFIRM = "registration/confirm";
    public static final String REGISTRATION = "registration";
    public static final String GENERATE_CODE = "registration/generate-confirmation-code";
}
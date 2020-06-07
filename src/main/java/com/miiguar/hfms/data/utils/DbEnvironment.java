package com.miiguar.hfms.data.utils;

/**
 * Constants
 */
public class DbEnvironment {

    /**
     * User table
     */
    public static final String USERS_TB_NAME = "users";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_USERNAME = "username";
    public static final String COL_EMAIL = "email";
    public static final String COL_PASSWORD = "password";
    public static final String COL_IS_ADMIN = "is_admin";
    public static final String PRIV_KEY_USERS = "pk_user_id";

    /**
     * Code table
     */
    public static final String CODE_TB_NAME = "user_gen_code";
    public static final String COL_CODE = "code";
    public static final String COL_EMAIL_CONFIRMED = "is_email_confirmed";
    public static final String PRIV_KEY_CODE = "pk_code";
    public static final String FK_TB_CODE_USER_ID = "fk_user_id_code_ref_users";
    // user_id to identify the user
}
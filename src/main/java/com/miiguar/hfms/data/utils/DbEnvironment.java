package com.miiguar.hfms.data.utils;

/**
 * Constants
 */
public class DbEnvironment {
    /**
     * Household table
     */
    public static final String HOUSEHOLD_TB_NAME = "households";
    public static final String HOUSEHOLD_ID = "household_id";
    public static final String HOUSEHOLD_NAME = "household_name";
    public static final String HOUSEHOLD_DESCRIPTION = "description";

    /**
     * User table
     */
    public static final String USERS_TB_NAME = "users";
    public static final String USER_ID = "user_id";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String IS_ADMIN = "is_admin";
    public static final String IS_ONLINE = "is_online";

    /**
     * User-Household table relationship
     */
    public static final String USER_HOUSEHOLD_TB_NAME = "user_household";
    // user_id reference
    // household_id reference
    public static final String FK_USERS_HOUSEHOLD_HOUSEHOLD_ID = "fk_users_household_id_ref_households";
    public static final String FK_USERS_HOUSEHOLD_USER_ID = "fk_users_household_id_ref_users";

    /**
     * Code table
     */
    public static final String CODE_TB_NAME = "user_gen_code";
    public static final String CODE = "code";
    public static final String EMAIL_CONFIRMED = "is_email_confirmed";
    public static final String PRIV_KEY_CODE = "pk_code";
    public static final String FK_TB_CODE_USER_ID = "fk_user_id_code_ref_users";
    // user_id to identify the user

    /**
     * Group table
     */
    public static final String GROUP_TB_NAME = "user_groups";
    public static final String COL_GROUP_ID = "group_id";
    public static final String COL_GROUP_NAME = "group_name";
    public static final String PRIV_KEY_GROUP = "pk_groups_group_id";

    /**
     * Member table
     */
    public static final String MEMBERS_TB_NAME = "group_members";
    public static final String MEMBERS_ID = "_id";
    // group_id referenced from group table
    // user_id referenced from user table
    public static final String PRIV_KEY_MEMBERS = "pk_members_members_id";
    public static final String FK_TB_MEMBERS_GROUP_ID = "fk_members_group_id_ref_groups";

    /**
     * Grocery table
     */
    public static final String GROCERIES_TB_NAME = "groceries";
    public static final String GROCERY_ID = "grocery_id";
    public static final String GROCERY_NAME = "grocery_name";
    public static final String GROCERY_PRICE = "grocery_price";
    public static final String GROCERY_DESCRIPTION = "grocery_description";
    public static final String REQUIRED_QUANTITY = "required_quantity";
    public static final String REMAINING_QUANTITY = "remaining_quantity";
    public static final String PRIV_KEY_GROCERIES = "priv_key_groceries";
    // household_id references the household table (household_id)
    public static final String FK_GROCERY_REF_HOUSEHOLD_ID = "fk_tb_grocery_user_id_ref_users";

    /**
     * Income table
     */
    public static final String INCOME_TB_NAME = "income";
    public static final String INCOME_ID = "income_id";
    public static final String ACCOUNT_TYPE = "account_type";
    public static final String AMOUNT = "amount";
    public static final String CREATED_AT = "created_at";
    // user_id references the user table (user_id)
    public static final String FOREIGN_KEY_USER_USER_ID = "foreign_key_user_user_id";

    /**
     * Account Status
     */
    public static final String ACCOUNT_STATUS_TB_NAME = "account_status_update";
    public static final String ACCOUNT_STATUS = "account_status";
    // Reference user_id from users table
    public static final String INCOME_STATUS = "income_status";
    public static final String GROCERY_STATUS = "grocery_status";
    public static final String EXPENSES_STATUS = "expenses_status";
    public static final String HOUSEHOLD_STATUS = "household_status";
    public static final String COMPLETE_AT = "completed_at";
    public static final String FK_TB_USERID_REF_USERS = "fk_user_id_references_users_user_id";
}
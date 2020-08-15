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
    public static final String USER_HOUSEHOLD_TB_NAME = "user_households";
    // user_id reference
    // household_id reference
    public static final String FK_USERS_HOUSEHOLD_HOUSEHOLD_ID = "fk_users_households_household_id_ref_households";
    public static final String FK_USERS_HOUSEHOLD_USER_ID = "fk_users_households_user_id_ref_users";
    public static final String IS_OWNER = "is_owner";

    /**
     * Code table
     */
    public static final String CODE_TB_NAME = "user_gen_codes";
    public static final String CODE = "code";
    public static final String EMAIL_CONFIRMED = "is_email_confirmed";
    public static final String PRIV_KEY_CODE = "pk_code";
    // user_id to identify the user
    public static final String FK_CODE_USER_ID = "fk_code_user_id_ref_users";

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
    public static final String FK_MEMBERS_GROUP_ID = "fk_members_group_id_ref_groups";

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
    // jar_id references the jars table (household_id)
    public static final String FK_GROCERIES_REF_HOUSEHOLD_ID = "fk_groceries_user_id_ref_users";
    public static final String FK_GROCERIES_JAR_ID = "fk_groceries_jar_id_ref_jars";



    /**
     * Income table
     */
    public static final String INCOME_TB_NAME = "income";
    public static final String INCOME_ID = "asset_id";
    public static final String ACCOUNT_TYPE = "account_type";
    public static final String INCOME_DESC = "income_description";
    public static final String AMOUNT = "amount";
    public static final String CREATED_AT = "created_at";
    // user_id references the user table (user_id)
    public static final String FK_INCOME_USER_ID = "fk_income_user_id_ref_users";

    /**
     * Account Status
     */
    public static final String ACCOUNT_STATUS_TB_NAME = "account_status_updates";
    public static final String ACCOUNT_STATUS = "account_status";
    // Reference user_id from users table
    public static final String INCOME_STATUS = "income_status";
    public static final String JAR_STATUS = "jar_status";
    public static final String HOUSEHOLD_STATUS = "household_status";
    public static final String COMPLETE_AT = "completed_at";
    public static final String FK_ACCOUNT_STATUS_USER_ID = "fk_account_status_user_id_ref_users";

    /**
     * Envelope Table
     */
    public static final String MONEY_JAR_TB_NAME = "money_jars";
    public static final String MONEY_JAR_ID = "jar_id";
    public static final String MONEY_JAR_NAME = "jar_label";
    public static final String CATEGORY = "category";
    public static final String TOTAL_AMOUNT = "amount";
    // created at {type: string date}
    public static final String SCHEDULED_FOR = "scheduled_for";
    public static final String SCHEDULED_TYPE = "scheduled_type";
    // household_id ref household
    public static final String FK_JARS_GROCERY_ID = "fk_jar_grocery_id_groceries";
    public static final String FK_MONEY_JAR_HOUSEHOLD_ID = "fk_money_jar_household_id_ref_households";

    /**
     * Expenses table
     */
    public static final String EXPENSES_TB_NAME = "expenses";
    public static final String EXPENSE_ID = "expense_id";
    public static final String EXPENSE_NAME = "expense_name";
    public static final String EXPENSE_DESCRIPTION = "expense_description";
    // amount {type: double}
    // householdId  references households household_id
    // envelope id ref envelopes envelope id
    public static final String PAYEE_NAME = "payee_name";
    public static final String EXPENSE_TYPE = "type";
    public static final String BUSINESS_NUMBER = "business_number";
    public static final String ACCOUNT_NUMBER = "account_number";
    public static final String FK_HOUSEHOLD_ID_REF_HOUSEHOLDS = "fk_household_id_ref_households_household_id";
    public static final String FK_EXPENSES_JAR_ID = "fk_expenses_jar_id_ref_jars";

    /**
     * Transactions
     */
    public static final String TRANSACTION_TB_NAME = "transactions";
    public static final String MERCHANT_REQUEST_ID = "merchant_request_id";
    public static final String CHECKOUT_REQ_ID = "checkout_request_id";
    public static final String RESPONSE_CODE = "response_code";
    public static final String RESPONSE_DESCRIPTION = "response__description";
    public static final String CUSTOMER_MESSAGE = "customer_message";
    public static final String RESULT_CODE = "result_code";
    public static final String RESULT_DESC = "result_description";
    public static final String CALLBACK_METADATA = "callback_metadata";
    public static final String TRANSACTION_STATUS = "transaction_status";
}
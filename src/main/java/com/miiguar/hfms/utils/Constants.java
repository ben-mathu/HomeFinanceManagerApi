package com.miiguar.hfms.utils;

/**
 * @author bernard
 */
public class Constants {
    private Constants() {
        throw new IllegalStateException("This class is a utility class it should be used this way");
    }
    public static final String ISSUER = "miiguar";

    // Session keys
    public static final String TOKEN = "token";
    public static final String SUBJECT = "subject";

    /**
     * Menu items and corresponding URL
     */
    public static final String MESSAGES = "Messages";
    public static final String URL_MESSAGES = "/dashboard/messages";
    public static final String MEMBERS = "Members";
    public static final String URL_MEMBERS = "/dashboard/members";
    public static final String SETTINGS = "Settings";
    public static final String URL_SETTINGS = "/dashboard/settings";
    public static final String REFRESH = "refresh";


    public static final String PARTIAL = "Partial";
    public static final String COMPLETE = "Complete";
    public static class Status {
        public static final String STATUS = "status";
        public static final String DATE = "date";
    }

    public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm";

    /**
     * JSON Field Names
     */
    public static final String USER = "user";
    public static final String INCOME = "income";
    public static final String ACCOUNT_STATUS_UPDATE = "account_status_update";
    public static final String GROCERY = "grocery";
    public static final String LIABILITIES = "liabilities";
    public static final String JAR = "jar";
    public static final String EXPENSE = "expense";
    public static final String JAR_ELEMENTS = "jar_elements";
    public static final String RELATION = "relations";
    public static final String HOUSEHOLD_MEMBERS = "members";
    public static final String JAR_SCHEDULE_ID = "schedule_id";
    public static final String PAYBILL = "paybill";

    // Daraja
    public static final String ACCESS_TOKEN = "access_token";
    public static final String EXPIRES_IN = "expires_in";
    public static final String PAY_BILL = "CustomerPayBillOnline";
    public static final String DARAJA_DATE_FORMAT = "yyyyMMddHHmmss";

    public static class LnmoRequestFields {
        public static final String SHORT_CODE = "BusinessShortCode";
        public static final String PASSWORD = "Password";
        public static final String TIMESTAMP = "Timestamp";
        public static final String TRANSACTION_TYPE = "TransactionType";
        public static final String AMOUNT = "Amount";
        public static final String PARTY_A = "PartyA";
        public static final String PARTY_B = "PartyB";
        public static final String PHONE_NUMBER = "PhoneNumber";
        public static final String CALLBACK_URL = "CallBackURL";
        public static final String ACCOUNT_REF = "AccountReference";
        public static final String TRANSACTION_DESC = "TransactionDesc";
        public static final String REQUEST_ID = "requestId";
        public static final String ERROR_CODE = "errorCode";
        public static final String ERROR_MESSAGE = "errorMessage";
    }

    public static class LnmoResponseFields {
        public static final String MERCHANT_REQUEST_ID = "MerchantRequestID";
        public static final String CHECKOUT_REQ_ID = "CheckoutRequestID";
        public static final String RESPONSE_CODE = "ResponseCode";
        public static final String RESPONSE_DESCRIPTION = "ResponseDescription";
        public static final String CUSTOMER_MESSAGE = "CustomerMessage";
        public static final String RESULT_CODE = "ResultCode";
        public static final String RESULT_DESC = "ResultDesc";
        public static final String CALLBACK_METADATA = "CallbackMetadata";
        public static final String ITEM = "Item";
    }

    public static class ScheduleType {
        public static final String SCHEDULED = "Scheduled";
        public static final String DAILY = "Daily";
        public static final String WEEKLY = "Weekly";
        public static final String MONTHLY = "Monthly";
    }

    public static class JarType {
        public static final String LIST_EXPENSE_TYPE = "List";
        public static final String SINGLE_EXPENSE_TYPE = "Single Item";
    }
}

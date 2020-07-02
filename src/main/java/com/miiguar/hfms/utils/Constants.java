package com.miiguar.hfms.utils;

/**
 * @author bernard
 */
public class Constants {
    public static final String ISSUER = "miiguar";

    // Session keys
    public static final String TOKEN = "token";
    public static final String SUBJECT = "subject";

    /**
     * Menu items and corresponding url
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

    public static final String DATE_FORMAT = "dd-MM-yyyy HH:mm";

    /**
     * JSON Field Names
     */
    public static final String USER = "user";
    public static final String INCOME = "income";
    public static final String ACCOUNT_STATUS_UPDATE = "account_status_update";
    public static final String GROCERY = "grocery";
}

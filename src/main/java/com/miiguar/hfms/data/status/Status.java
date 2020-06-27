package com.miiguar.hfms.data.status;

import com.google.gson.annotations.SerializedName;

import static com.miiguar.hfms.utils.Constants.Status.DATE;
import static com.miiguar.hfms.utils.Constants.Status.STATUS;

/**
 * @author bernard
 */
public class Status {
    @SerializedName(STATUS)
    public String status = "";
    @SerializedName(DATE)
    public String date = "";
}

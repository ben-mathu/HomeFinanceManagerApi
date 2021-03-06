package com.benardmathu.hfms.data.daraja.models;

import com.google.gson.annotations.SerializedName;

import static com.benardmathu.hfms.utils.Constants.ACCESS_TOKEN;
import static com.benardmathu.hfms.utils.Constants.EXPIRES_IN;

/**
 * @author bernard
 */
public class AccessToken {
    @SerializedName(ACCESS_TOKEN)
    private String accessToken = "";
    @SerializedName(EXPIRES_IN)
    private String expires = "";

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}

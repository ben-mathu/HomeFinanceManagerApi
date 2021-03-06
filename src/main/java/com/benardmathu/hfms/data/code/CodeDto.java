package com.benardmathu.hfms.data.code;

import com.google.gson.annotations.SerializedName;
import com.benardmathu.hfms.data.code.model.Code;

/**
 * @author bernard
 */
public class CodeDto {
    @SerializedName("code")
    private Code code;

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }
}

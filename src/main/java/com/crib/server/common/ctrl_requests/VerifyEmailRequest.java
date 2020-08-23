package com.crib.server.common.ctrl_requests;

import com.crib.server.common.patterns.CtrlRequest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class VerifyEmailRequest extends CtrlRequest {

    @NotNull
    @Size(min = 1, max = 1024)
    private String code;

    @NotNull
    @Size(min = 1, max = 1024)
    private String userId;

    public VerifyEmailRequest() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

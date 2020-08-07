package com.crib.server.controllers.auth.responses;

import com.crib.server.common.patterns.CtrlResponse;

public class SignUpResponse extends CtrlResponse {

    private String userId;

    public SignUpResponse() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
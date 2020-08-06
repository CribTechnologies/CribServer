package com.crib.server.controllers.auth.responses;

import com.crib.server.common.patterns.ControllerResponse;

public class SignUpResponse extends ControllerResponse {

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

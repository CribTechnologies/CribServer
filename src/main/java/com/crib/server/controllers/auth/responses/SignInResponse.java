package com.crib.server.controllers.auth.responses;

import com.crib.server.common.patterns.ControllerResponse;

public class SignInResponse extends ControllerResponse {

    private String userId;
    private String authenticationToken;

    public SignInResponse() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public void setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }
}

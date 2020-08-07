package com.crib.server.common.ctrl_responses;

import com.crib.server.common.patterns.CtrlResponse;

public class SignInResponse extends CtrlResponse {

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

package com.crib.server.common.ctrl_responses;

import com.crib.server.common.patterns.CtrlResponse;

public class VerifyEmailResponse extends CtrlResponse {

    private boolean verified;

    public VerifyEmailResponse() {
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}

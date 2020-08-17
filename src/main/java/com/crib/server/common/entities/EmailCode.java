package com.crib.server.common.entities;

import com.crib.server.common.patterns.DTO;

public class EmailCode extends DTO {

    private String userId;
    private String verificationCode;

    public EmailCode() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}

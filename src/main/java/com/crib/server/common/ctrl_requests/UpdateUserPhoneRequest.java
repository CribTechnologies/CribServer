package com.crib.server.common.ctrl_requests;

import com.crib.server.common.patterns.CtrlRequest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateUserPhoneRequest extends CtrlRequest {

    @NotNull
    @Size(min = 1, max = 128)
    private String userId;

    @NotNull
    @Size(min = 6, max = 1024)
    private String password;

    @NotNull
    @Size(min = 1, max = 128)
    private String phoneNumber;

    public UpdateUserPhoneRequest() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

package com.crib.server.common.ctrl_requests;

import com.crib.server.common.patterns.CtrlRequest;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateUserEmailRequest extends CtrlRequest {

    @NotNull
    @Size(min = 1, max = 128)
    private String userId;

    @NotNull
    @Size(min = 6, max = 1024)
    private String password;

    @NotNull
    @Email
    @Size(min = 6, max = 1024)
    private String email;

    public UpdateUserEmailRequest() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

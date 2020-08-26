package com.crib.server.common.ctrl_requests;

import com.crib.server.common.patterns.CtrlRequest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateUserPasswordRequest extends CtrlRequest {

    @NotNull
    @Size(min = 1, max = 128)
    private String userId;

    @NotNull
    @Size(min = 6, max = 1024)
    private String oldPassword;

    @NotNull
    @Size(min = 6, max = 1024)
    private String newPassword;

    public UpdateUserPasswordRequest() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

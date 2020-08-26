package com.crib.server.common.ctrl_requests;

import com.crib.server.common.enums.UserHomeRole;
import com.crib.server.common.patterns.CtrlRequest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RoleChangeRequest extends CtrlRequest {

    @NotNull
    @Size(min = 1, max = 128)
    private String homeId;

    @NotNull
    @Size(min = 1, max = 128)
    private String userId;

    @NotNull
    private UserHomeRole role;

    public RoleChangeRequest() {
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserHomeRole getRole() {
        return role;
    }

    public void setRole(UserHomeRole role) {
        this.role = role;
    }
}

package com.crib.server.common.value_objects;

import com.crib.server.common.enums.UserHomeRole;

public class UserIdWithRole {

    private String userId;
    private UserHomeRole role;

    public UserIdWithRole() {
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

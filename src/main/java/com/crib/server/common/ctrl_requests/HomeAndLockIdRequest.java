package com.crib.server.common.ctrl_requests;

import com.crib.server.common.patterns.CtrlRequest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class HomeAndLockIdRequest extends CtrlRequest {

    @NotNull
    @Size(min = 1, max = 128)
    private String homeId;

    @NotNull
    @Size(min = 1, max = 128)
    private String lockId;

    public HomeAndLockIdRequest() {
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }
}

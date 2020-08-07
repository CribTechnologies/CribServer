package com.crib.server.controllers.lock.responses;

import com.crib.server.common.patterns.CtrlResponse;

public class RegisterLockResponse extends CtrlResponse {

    private String lockId;

    public RegisterLockResponse() {
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }
}

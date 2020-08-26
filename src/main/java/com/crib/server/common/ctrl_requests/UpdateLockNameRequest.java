package com.crib.server.common.ctrl_requests;

import com.crib.server.common.patterns.CtrlRequest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateLockNameRequest extends CtrlRequest {

    @NotNull
    @Size(min = 1, max = 128)
    private String lockId;

    @NotNull
    @Size(min = 1, max = 128)
    private String name;

    public UpdateLockNameRequest() {
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

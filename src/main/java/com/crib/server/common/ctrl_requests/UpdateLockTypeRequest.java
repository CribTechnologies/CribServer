package com.crib.server.common.ctrl_requests;

import com.crib.server.common.enums.LockType;
import com.crib.server.common.patterns.CtrlRequest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateLockTypeRequest extends CtrlRequest {

    @NotNull
    @Size(min = 1, max = 128)
    private String lockId;

    @NotNull
    private LockType lockType;

    public UpdateLockTypeRequest() {
    }

    public String getLockId() {
        return lockId;
    }

    public void setLockId(String lockId) {
        this.lockId = lockId;
    }

    public LockType getLockType() {
        return lockType;
    }

    public void setLockType(LockType lockType) {
        this.lockType = lockType;
    }
}

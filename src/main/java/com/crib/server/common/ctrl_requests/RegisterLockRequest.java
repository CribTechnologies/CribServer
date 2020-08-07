package com.crib.server.common.ctrl_requests;

import com.crib.server.common.enums.LockType;
import com.crib.server.common.patterns.CtrlRequest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegisterLockRequest extends CtrlRequest {

    @NotNull
    @Size(min = 1, max = 128)
    private String name;

    @NotNull
    private LockType type;

    @NotNull
    @Size(min = 1, max = 128)
    private String serialNumber;

    public RegisterLockRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LockType getType() {
        return type;
    }

    public void setType(LockType type) {
        this.type = type;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}

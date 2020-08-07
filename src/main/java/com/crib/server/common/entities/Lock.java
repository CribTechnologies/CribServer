package com.crib.server.common.entities;

import com.crib.server.common.enums.LockType;
import com.crib.server.common.patterns.DTO;

public class Lock extends DTO {

    private String name;
    private LockType type;
    private String serialNumber;

    public Lock() {
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

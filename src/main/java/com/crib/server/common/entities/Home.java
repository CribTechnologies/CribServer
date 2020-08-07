package com.crib.server.common.entities;

import com.crib.server.common.patterns.DTO;
import com.crib.server.common.value_objects.PhysicalAddress;

import java.util.List;

public class Home extends DTO {

    private String name;
    private PhysicalAddress address;
    private List<String> memberIds;
    private List<String> lockIds;

    public Home() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PhysicalAddress getAddress() {
        return address;
    }

    public void setAddress(PhysicalAddress address) {
        this.address = address;
    }

    public List<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<String> memberIds) {
        this.memberIds = memberIds;
    }

    public List<String> getLockIds() {
        return lockIds;
    }

    public void setLockIds(List<String> lockIds) {
        this.lockIds = lockIds;
    }
}

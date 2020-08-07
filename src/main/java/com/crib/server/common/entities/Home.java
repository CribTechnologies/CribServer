package com.crib.server.common.entities;

import com.crib.server.common.patterns.DTO;
import com.crib.server.common.value_objects.PhysicalAddress;
import com.crib.server.common.value_objects.UserIdWithRole;

import java.util.List;

public class Home extends DTO {

    private String name;
    private PhysicalAddress address;
    private List<String> lockIds;
    private List<UserIdWithRole> users;

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

    public List<String> getLockIds() {
        return lockIds;
    }

    public void setLockIds(List<String> lockIds) {
        this.lockIds = lockIds;
    }

    public List<UserIdWithRole> getUsers() {
        return users;
    }

    public void setUsers(List<UserIdWithRole> users) {
        this.users = users;
    }
}

package com.crib.server.common.ctrl_responses;

import com.crib.server.common.entities.Home;
import com.crib.server.common.entities.Lock;
import com.crib.server.common.entities.User;
import com.crib.server.common.patterns.CtrlResponse;

import java.util.List;

public class ViewHomeDetailsResponse extends CtrlResponse {

    private Home home;
    private List<User> users;
    private List<Lock> locks;

    public ViewHomeDetailsResponse() {
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Lock> getLocks() {
        return locks;
    }

    public void setLocks(List<Lock> locks) {
        this.locks = locks;
    }
}

package com.crib.server.common.ctrl_responses;

import com.crib.server.common.entities.Home;
import com.crib.server.common.patterns.CtrlResponse;

public class HomeResponse extends CtrlResponse {

    private Home home;

    public HomeResponse() {
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }
}

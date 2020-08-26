package com.crib.server.common.ctrl_requests;

import com.crib.server.common.patterns.CtrlRequest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class HomeIdRequest extends CtrlRequest {

    @NotNull
    @Size(min = 1, max = 128)
    private String homeId;

    public HomeIdRequest() {
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }
}

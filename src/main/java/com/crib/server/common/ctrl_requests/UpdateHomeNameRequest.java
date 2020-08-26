package com.crib.server.common.ctrl_requests;

import com.crib.server.common.patterns.CtrlRequest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateHomeNameRequest extends CtrlRequest {

    @NotNull
    @Size(min = 1, max = 128)
    private String homeId;

    @NotNull
    @Size(min = 1, max = 128)
    private String name;

    public UpdateHomeNameRequest() {
    }

    public String getHomeId() {
        return homeId;
    }

    public void setHomeId(String homeId) {
        this.homeId = homeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

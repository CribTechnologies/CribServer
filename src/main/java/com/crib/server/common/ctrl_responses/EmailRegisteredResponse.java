package com.crib.server.common.ctrl_responses;

import com.crib.server.common.patterns.CtrlResponse;

public class EmailRegisteredResponse extends CtrlResponse {

    public boolean registered;

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }
}

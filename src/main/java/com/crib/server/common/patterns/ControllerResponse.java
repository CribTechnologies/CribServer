package com.crib.server.common.patterns;

import com.crib.server.common.enums.ControllerResponseStatus;

import java.util.ArrayList;
import java.util.List;

public class ControllerResponse {

    private ControllerResponseStatus status;
    private List<String> messages;

    public ControllerResponse() {
        messages = new ArrayList<String>();
    }

    public ControllerResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ControllerResponseStatus status) {
        this.status = status;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}

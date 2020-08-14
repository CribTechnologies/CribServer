package com.crib.server.common.patterns;

import com.crib.server.common.enums.CtrlResponseStatus;

import java.util.ArrayList;
import java.util.List;

public class CtrlResponse {

    private CtrlResponseStatus status;
    private List<String> messages;

    public CtrlResponse() {
        messages = new ArrayList<String>();
    }

    public CtrlResponse(CtrlResponseStatus status, String message) {
        this.status = status;
        this.messages = new ArrayList<>();
        this.messages.add(message);
    }

    public void addMessage(String message) {
        if (message != null)
            messages.add(message);
    }

    public void addMessages(List<String> messages) {
        for (String message : messages) {
            if (message != null)
                addMessage(message);
        }
    }

    public CtrlResponseStatus getStatus() {
        return status;
    }

    public void setStatus(CtrlResponseStatus status) {
        this.status = status;
    }

    public List<String> getMessages() {
        return messages;
    }
}

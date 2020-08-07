package com.crib.server.common.patterns;

// Stands for Controller Response With Payload
public class CtrlResponseWP<T> extends CtrlResponse {

    private T payload;

    public CtrlResponseWP() {
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}

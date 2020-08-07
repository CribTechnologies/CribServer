package com.crib.server.common.patterns;

public class RepoResponseWP<T> extends RepoResponse {

    private T payload;

    public RepoResponseWP() {
    }

    public RepoResponseWP(boolean successful, String message, T payload) {
        super(successful, message);
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}

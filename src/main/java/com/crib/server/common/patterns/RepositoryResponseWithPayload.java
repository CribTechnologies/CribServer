package com.crib.server.common.patterns;

public class RepositoryResponseWithPayload<T> extends RepositoryResponse {

    private T payload;

    public RepositoryResponseWithPayload() {
    }

    public RepositoryResponseWithPayload(boolean successful, String message, T payload) {
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

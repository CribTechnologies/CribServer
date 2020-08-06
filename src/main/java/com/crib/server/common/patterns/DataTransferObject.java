package com.crib.server.common.patterns;

public class DataTransferObject {

    private String id;
    private int timestamp;

    public DataTransferObject() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}

package com.crib.server.common.patterns;

public class DataTransferObject {

    private String id;
    private long timestamp;

    public DataTransferObject() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

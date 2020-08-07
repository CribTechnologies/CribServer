package com.crib.server.common.patterns;

public class RepositoryResponse {

    private boolean successful;
    private String message;

    public RepositoryResponse() {
    }

    public RepositoryResponse(boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
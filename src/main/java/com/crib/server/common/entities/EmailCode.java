package com.crib.server.common.entities;

import com.crib.server.common.patterns.DTO;

public class EmailCode extends DTO {

    private String userId;
    private String codeHash;

    public EmailCode() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCodeHash() {
        return codeHash;
    }

    public void setCodeHash(String codeHash) {
        this.codeHash = codeHash;
    }
}

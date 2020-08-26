package com.crib.server.services.helpers;

import java.util.UUID;

public class IdHelper {

    private static IdHelper singleInstance;

    private IdHelper() {
    }

    public static IdHelper getInstance() {
        if (singleInstance == null)
            singleInstance = new IdHelper();
        return singleInstance;
    }

    public String generateId() {
        return UUID.randomUUID().toString();
    }
}

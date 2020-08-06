package com.crib.server.services;

public class ServiceFactory {

    private static ServiceFactory singleInstance = null;
    private AuthService authService;

    private ServiceFactory() {
        authService = new AuthService();
    }

    public static ServiceFactory getInstance() {
        if (singleInstance == null)
            singleInstance = new ServiceFactory();
        return singleInstance;
    }

    public AuthService getAuthService() {
        return authService;
    }
}

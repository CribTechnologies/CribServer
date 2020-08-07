package com.crib.server.services;

public class ServiceFactory {

    private static ServiceFactory singleInstance = null;
    private AuthService authService;
    private UserService userService;
    private LockService lockService;
    private HomeService homeService;

    private ServiceFactory() {
        authService = new AuthService();
        userService = new UserService();
        lockService = new LockService();
        homeService = new HomeService();
    }

    public static ServiceFactory getInstance() {
        if (singleInstance == null)
            singleInstance = new ServiceFactory();
        return singleInstance;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public UserService getUserService() {
        return userService;
    }

    public LockService getLockService() {
        return lockService;
    }

    public HomeService getHomeService() {
        return homeService;
    }
}

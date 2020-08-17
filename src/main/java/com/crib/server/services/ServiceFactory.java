package com.crib.server.services;

public class ServiceFactory {

    private static ServiceFactory singleInstance = null;
    private final AuthService authService;
    private final UserService userService;
    private final LockService lockService;
    private final HomeService homeService;
    private final EmailService emailService;

    private ServiceFactory() {
        authService = new AuthService();
        userService = new UserService();
        lockService = new LockService();
        homeService = new HomeService();
        emailService = new EmailService();
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

    public EmailService getEmailService() {
        return emailService;
    }
}

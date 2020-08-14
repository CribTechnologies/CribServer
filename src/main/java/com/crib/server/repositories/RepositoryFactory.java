package com.crib.server.repositories;

import com.crib.server.repositories.implementations.firestore.EmailVerificationCodeRepository;
import com.crib.server.repositories.implementations.firestore.HomeRepository;
import com.crib.server.repositories.implementations.firestore.LockRepository;
import com.crib.server.repositories.implementations.firestore.UserRepository;
import com.crib.server.repositories.interfaces.IEmailVerificationCodeRepository;
import com.crib.server.repositories.interfaces.IHomeRepository;
import com.crib.server.repositories.interfaces.ILockRepository;
import com.crib.server.repositories.interfaces.IUserRepository;

public class RepositoryFactory {

    private static RepositoryFactory singleInstance = null;
    private final IUserRepository userRepository;
    private final ILockRepository lockRepository;
    private final IHomeRepository homeRepository;
    private final IEmailVerificationCodeRepository emailVerificationCodeRepository;

    private RepositoryFactory() {
        userRepository = new UserRepository();
        lockRepository = new LockRepository();
        homeRepository = new HomeRepository();
        emailVerificationCodeRepository = new EmailVerificationCodeRepository();
    }

    public static RepositoryFactory getInstance() {
        if (singleInstance == null)
            singleInstance = new RepositoryFactory();
        return singleInstance;
    }

    public IUserRepository getUserRepository() {
        return userRepository;
    }

    public ILockRepository getLockRepository() {
        return lockRepository;
    }

    public IHomeRepository getHomeRepository() {
        return homeRepository;
    }

    public IEmailVerificationCodeRepository getEmailVerificationCodeRepository() {
        return emailVerificationCodeRepository;
    }
}

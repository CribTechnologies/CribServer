package com.crib.server.repositories;

import com.crib.server.repositories.implementations.firestore.HomeRepository;
import com.crib.server.repositories.implementations.firestore.LockRepository;
import com.crib.server.repositories.implementations.firestore.UserRepository;
import com.crib.server.repositories.interfaces.IHomeRepository;
import com.crib.server.repositories.interfaces.ILockRepository;
import com.crib.server.repositories.interfaces.IUserRepository;

public class RepositoryFactory {

    private static RepositoryFactory singleInstance = null;
    private IUserRepository userRepository;
    private ILockRepository lockRepository;
    private IHomeRepository homeRepository;

    private RepositoryFactory() {
        userRepository = new UserRepository();
        lockRepository = new LockRepository();
        homeRepository = new HomeRepository();
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
}

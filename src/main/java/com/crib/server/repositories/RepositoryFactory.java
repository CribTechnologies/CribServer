package com.crib.server.repositories;

import com.crib.server.repositories.implementations.firestore.UserRepository;
import com.crib.server.repositories.interfaces.IUserRepository;

public class RepositoryFactory {

    private static RepositoryFactory singleInstance = null;
    private IUserRepository userRepository;

    private RepositoryFactory() {
        userRepository = new UserRepository();
    }

    public static RepositoryFactory getInstance() {
        if (singleInstance == null)
            singleInstance = new RepositoryFactory();
        return singleInstance;
    }

    public IUserRepository getUserRepository() {
        return userRepository;
    }
}

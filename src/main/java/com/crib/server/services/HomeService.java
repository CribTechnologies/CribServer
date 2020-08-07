package com.crib.server.services;

import com.crib.server.repositories.RepositoryFactory;
import com.crib.server.repositories.interfaces.IHomeRepository;
import com.crib.server.repositories.interfaces.ILockRepository;

public class HomeService extends Service {

    private IHomeRepository homeRepository;
    private ILockRepository lockRepository;

    public HomeService() {
        RepositoryFactory repositoryFactory = RepositoryFactory.getInstance();
        homeRepository = repositoryFactory.getHomeRepository();
        lockRepository = repositoryFactory.getLockRepository();
    }
}

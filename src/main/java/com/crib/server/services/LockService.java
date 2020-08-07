package com.crib.server.services;

import com.crib.server.common.entities.Lock;
import com.crib.server.common.enums.ControllerResponseStatus;
import com.crib.server.common.enums.LockType;
import com.crib.server.common.patterns.CtrlResponse;
import com.crib.server.common.patterns.RepoResponse;
import com.crib.server.common.ctrl_requests.RegisterLockRequest;
import com.crib.server.common.ctrl_responses.RegisterLockResponse;
import com.crib.server.repositories.RepositoryFactory;
import com.crib.server.repositories.interfaces.ILockRepository;

import java.util.Date;
import java.util.UUID;

public class LockService extends Service {

    private ILockRepository lockRepository;

    public LockService() {
        RepositoryFactory repositoryFactory = RepositoryFactory.getInstance();
        lockRepository = repositoryFactory.getLockRepository();
    }

    public RegisterLockResponse registerLock(RegisterLockRequest request) {
        RegisterLockResponse response = new RegisterLockResponse();

        Lock lock = new Lock();
        lock.setId(UUID.randomUUID().toString());
        lock.setTimestamp(new Date().getTime());

        lock.setName(request.getName());
        lock.setSerialNumber(request.getSerialNumber());
        lock.setType(request.getType());

        RepoResponse repoResponse = lockRepository.create(lock);
        if (repoResponse.isSuccessful()) {
            response.setStatus(ControllerResponseStatus.SUCCESS);
            response.setLockId(lock.getId());
        }
        else {
            response.setStatus(ControllerResponseStatus.REPOSITORY_ERROR);
        }
        return response;
    }

    public CtrlResponse updateName(String lockId, String name) {
        return repoToCtrlResponse(lockRepository.updateName(lockId, name));
    }

    public CtrlResponse updateLockType(String lockId, LockType lockType) {
        return repoToCtrlResponse(lockRepository.updateType(lockId, lockType));
    }
}

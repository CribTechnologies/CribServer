package com.crib.server.services;

import com.crib.server.common.ctrl_requests.RegisterLockRequest;
import com.crib.server.common.ctrl_requests.UpdateLockNameRequest;
import com.crib.server.common.ctrl_requests.UpdateLockTypeRequest;
import com.crib.server.common.ctrl_responses.RegisterLockResponse;
import com.crib.server.common.entities.Lock;
import com.crib.server.common.enums.CtrlResponseStatus;
import com.crib.server.common.patterns.CtrlResponse;
import com.crib.server.common.patterns.RepoResponse;
import com.crib.server.repositories.RepositoryFactory;
import com.crib.server.repositories.interfaces.ILockRepository;
import com.crib.server.services.helpers.IdHelper;
import com.crib.server.services.helpers.ValidationHelper;

import java.util.Date;

public class LockService extends Service {

    private final ILockRepository lockRepository;

    private final IdHelper idHelper;

    public LockService() {
        RepositoryFactory repositoryFactory = RepositoryFactory.getInstance();
        lockRepository = repositoryFactory.getLockRepository();
        idHelper = IdHelper.getInstance();
    }

    public RegisterLockResponse registerLock(RegisterLockRequest request) {
        RegisterLockResponse response = new RegisterLockResponse();

        Lock lock = new Lock();
        lock.setId(idHelper.generateId());
        lock.setTimestamp(new Date().getTime());

        lock.setName(request.getName());
        lock.setSerialNumber(request.getSerialNumber());
        lock.setType(request.getType());

        RepoResponse repoResponse = lockRepository.create(lock);
        if (repoResponse.isSuccessful()) {
            response.setStatus(CtrlResponseStatus.SUCCESS);
            response.setLockId(lock.getId());
        }
        else {
            response.setStatus(CtrlResponseStatus.REPOSITORY_ERROR);
        }
        return response;
    }

    public CtrlResponse updateName(UpdateLockNameRequest request) {
        CtrlResponse response = new CtrlResponse();
        if (ValidationHelper.addValidationErrorsToResponse(request, response)) {
            return response;
        }

        RepoResponse repoResponse = lockRepository.updateName(request.getLockId(), request.getName());
        if (repoResponse.isSuccessful()) {
            response.setStatus(CtrlResponseStatus.SUCCESS);
        }
        else {
            response.setStatus(CtrlResponseStatus.REPOSITORY_ERROR);
            response.addMessage(repoResponse.getMessage());
        }
        return response;
    }

    public CtrlResponse updateLockType(UpdateLockTypeRequest request) {
        CtrlResponse response = new CtrlResponse();
        if (ValidationHelper.addValidationErrorsToResponse(request, response)) {
            return response;
        }

        RepoResponse repoResponse = lockRepository.updateType(request.getLockId(), request.getLockType());
        if (repoResponse.isSuccessful()) {
            response.setStatus(CtrlResponseStatus.SUCCESS);
        }
        else {
            response.setStatus(CtrlResponseStatus.REPOSITORY_ERROR);
            response.addMessage(repoResponse.getMessage());
        }
        return response;
    }
}

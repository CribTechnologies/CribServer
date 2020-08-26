package com.crib.server.services;

import com.crib.server.Argon2Setup;
import com.crib.server.common.ctrl_requests.UpdateUserDetailsRequest;
import com.crib.server.common.ctrl_requests.UpdateUserEmailRequest;
import com.crib.server.common.ctrl_requests.UpdateUserPasswordRequest;
import com.crib.server.common.ctrl_requests.UpdateUserPhoneRequest;
import com.crib.server.common.entities.User;
import com.crib.server.common.enums.CtrlResponseStatus;
import com.crib.server.common.patterns.CtrlResponse;
import com.crib.server.common.patterns.RepoResponse;
import com.crib.server.common.patterns.RepoResponseWP;
import com.crib.server.repositories.RepositoryFactory;
import com.crib.server.repositories.interfaces.IUserRepository;
import com.crib.server.services.helpers.ValidationHelper;

public class UserService extends Service {

    private final IUserRepository userRepository;

    private final Argon2Setup argon2;

    public UserService() {
        RepositoryFactory repositoryFactory = RepositoryFactory.getInstance();
        userRepository = repositoryFactory.getUserRepository();
        argon2 = Argon2Setup.getInstance();
    }

    public CtrlResponse updateDetails(UpdateUserDetailsRequest request) {
        CtrlResponse response = new CtrlResponse();
        if (ValidationHelper.addValidationErrorsToResponse(request, response)) {
            return response;
        }
        RepoResponse repoResponse = userRepository.updateDetails(request.getUserId(), request.getFirstName(),
                request.getLastName(), request.getDateOfBirth(), request.getGender());
        if (repoResponse.isSuccessful()) {
            response.setStatus(CtrlResponseStatus.SUCCESS);
        }
        else {
            response.setStatus(CtrlResponseStatus.REPOSITORY_ERROR);
            response.addMessage(repoResponse.getMessage());
        }
        return response;
    }

    public CtrlResponse updatePassword(UpdateUserPasswordRequest request) {
        CtrlResponse response = new CtrlResponse();
        if (ValidationHelper.addValidationErrorsToResponse(request, response)) {
            return response;
        }
        RepoResponseWP<User> repoResponseWP = userRepository.getById(request.getUserId());
        if (argon2.rawAndHashAreEqual(request.getOldPassword(), repoResponseWP.getPayload().getPasswordHash())) {
            String newPasswordHash = argon2.createHash(request.getNewPassword());
            RepoResponse repoResponse = userRepository.updatePasswordHash(request.getUserId(), newPasswordHash);
            if (repoResponse.isSuccessful()) {
                response.setStatus(CtrlResponseStatus.SUCCESS);
            }
            else {
                response.setStatus(CtrlResponseStatus.REPOSITORY_ERROR);
                response.addMessage(repoResponse.getMessage());
            }
        }
        else {
            response.setStatus(CtrlResponseStatus.AUTH_ERROR);
            response.addMessage("The old password is incorrect");
        }
        return response;
    }

    public CtrlResponse updateEmail(UpdateUserEmailRequest request) {
        CtrlResponse response = new CtrlResponse();
        if (ValidationHelper.addValidationErrorsToResponse(request, response)) {
            return response;
        }
        // TODO re-send verification link to user
        RepoResponseWP<User> repoResponseWP = userRepository.getById(request.getUserId());
        if (argon2.rawAndHashAreEqual(request.getPassword(), repoResponseWP.getPayload().getPasswordHash())) {
            RepoResponse repoResponse = userRepository.updateEmailAndVerified(request.getUserId(), request.getEmail(), false);
            if (repoResponse.isSuccessful()) {
                response.setStatus(CtrlResponseStatus.SUCCESS);
            }
            else {
                response.setStatus(CtrlResponseStatus.REPOSITORY_ERROR);
                response.addMessage(repoResponse.getMessage());
            }
        }
        else {
            response.setStatus(CtrlResponseStatus.AUTH_ERROR);
            response.addMessage("The password is incorrect");
        }
        return response;
    }

    public CtrlResponse updatePhoneNumber(UpdateUserPhoneRequest request) {
        CtrlResponse response = new CtrlResponse();
        if (ValidationHelper.addValidationErrorsToResponse(request, response)) {
            return response;
        }
        // TODO re-send verification link to user
        RepoResponseWP<User> repoResponseWP = userRepository.getById(request.getUserId());
        if (argon2.rawAndHashAreEqual(request.getPassword(), repoResponseWP.getPayload().getPasswordHash())) {
            RepoResponse repoResponse = userRepository.updatePhoneNumberAndVerified(request.getUserId(), request.getPhoneNumber(), false);
            if (repoResponse.isSuccessful()) {
                response.setStatus(CtrlResponseStatus.SUCCESS);
            }
            else {
                response.setStatus(CtrlResponseStatus.REPOSITORY_ERROR);
                response.addMessage(repoResponse.getMessage());
            }
        }
        else {
            response.setStatus(CtrlResponseStatus.AUTH_ERROR);
            response.addMessage("The password is incorrect");
        }
        return response;
    }
}

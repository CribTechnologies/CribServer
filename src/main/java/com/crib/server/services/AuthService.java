package com.crib.server.services;

import com.crib.server.Argon2Setup;
import com.crib.server.JSONWebTokenSetup;
import com.crib.server.common.entities.User;
import com.crib.server.common.enums.ControllerResponseStatus;
import com.crib.server.common.patterns.CtrlResponseWP;
import com.crib.server.common.patterns.RepoResponse;
import com.crib.server.common.patterns.RepoResponseWP;
import com.crib.server.common.ctrl_requests.SignInRequest;
import com.crib.server.common.ctrl_requests.SignUpRequest;
import com.crib.server.common.ctrl_responses.SignInResponse;
import com.crib.server.common.ctrl_responses.SignUpResponse;
import com.crib.server.repositories.RepositoryFactory;
import com.crib.server.repositories.interfaces.IUserRepository;

import java.util.Date;
import java.util.UUID;

public class AuthService extends Service {

    private IUserRepository userRepository;
    private Argon2Setup argon2;
    private JSONWebTokenSetup jwt;

    public AuthService() {
        RepositoryFactory repositoryFactory = RepositoryFactory.getInstance();
        userRepository = repositoryFactory.getUserRepository();
        argon2 = Argon2Setup.getInstance();
        jwt = JSONWebTokenSetup.getInstance();
    }

    public SignInResponse signIn(SignInRequest request) {
        SignInResponse response = new SignInResponse();
        RepoResponseWP<User> repositoryResponse = userRepository.getUserByEmail(request.getEmail());
        User user = repositoryResponse.getPayload();
        if (!repositoryResponse.isSuccessful()) {
            response.setStatus(ControllerResponseStatus.REPOSITORY_ERROR);
            response.addMessage("There was an internal error");
            response.addMessage("Repository Error: " + repositoryResponse.getMessage());
        }
        else if (user == null) {
            response.setStatus(ControllerResponseStatus.AUTH_ERROR);
            response.addMessage("This email is not registered");
        }
        else if (!argon2.rawAndHashAreEqual(request.getPassword(), repositoryResponse.getPayload().getPasswordHash())) {
            response.setStatus(ControllerResponseStatus.AUTH_ERROR);
            response.addMessage("The email and password do not match");
        }
        else {
            response.setStatus(ControllerResponseStatus.SUCCESS);
            response.setUserId(user.getId());
            response.setAuthenticationToken(jwt.generateSignInToken(user.getId()));
        }
        return response;
    }

    public SignUpResponse signUp(SignUpRequest request) {
        SignUpResponse response = new SignUpResponse();
        RepoResponseWP<User> repositoryResponse1 = userRepository.getUserByEmail(request.getEmail());
        if (repositoryResponse1.getPayload() != null) {
            response.setStatus(ControllerResponseStatus.VALIDATION_ERROR);
            response.addMessage("This email is already registered");
            return response;
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setGender(request.getGender());

        // TODO send verification links to user
        user.setEmailVerified(false);
        user.setPhoneNumberVerified(false);

        user.setId(UUID.randomUUID().toString());
        user.setTimestamp(new Date().getTime());
        user.setPasswordHash(argon2.createHash(request.getPassword()));

        RepoResponse repoResponse2 = userRepository.create(user);
        if (repoResponse2.isSuccessful()) {
            response.setStatus(ControllerResponseStatus.ERROR);
            response.addMessage(repoResponse2.getMessage());
        }
        else {
            response.setStatus(ControllerResponseStatus.SUCCESS);
            response.setUserId(user.getId());
        }
        return response;
    }

    public CtrlResponseWP<Boolean> emailIsRegistered(String email) {
        RepoResponseWP<User> repoResponse = userRepository.getUserByEmail(email);
        return repoToCtrlResponseWithCustomPayload(repoResponse, repoResponse.getPayload() == null);
    }
}

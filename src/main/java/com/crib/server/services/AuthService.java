package com.crib.server.services;

import com.crib.server.Argon2Setup;
import com.crib.server.JSONWebTokenSetup;
import com.crib.server.common.entities.User;
import com.crib.server.common.enums.ControllerResponseStatus;
import com.crib.server.common.patterns.RepositoryResponse;
import com.crib.server.common.patterns.RepositoryResponseWithPayload;
import com.crib.server.controllers.auth.requests.SignInRequest;
import com.crib.server.controllers.auth.requests.SignUpRequest;
import com.crib.server.controllers.auth.responses.SignInResponse;
import com.crib.server.controllers.auth.responses.SignUpResponse;
import com.crib.server.repositories.RepositoryFactory;
import com.crib.server.repositories.interfaces.IUserRepository;

import java.util.Date;
import java.util.UUID;

public class AuthService {

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
        RepositoryResponseWithPayload<User> repositoryResponse = userRepository.getUserByEmail(request.getEmail());
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
        RepositoryResponseWithPayload<User> repositoryResponse1 = userRepository.getUserByEmail(request.getEmail());
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

        user.setId(UUID.randomUUID().toString());
        user.setTimestamp(new Date().getTime());
        user.setPasswordHash(argon2.createHash(request.getPassword()));

        RepositoryResponse repositoryResponse2 = userRepository.create(user);
        if (repositoryResponse2.isSuccessful()) {
            response.setStatus(ControllerResponseStatus.ERROR);
            response.addMessage(repositoryResponse2.getMessage());
        }
        else {
            response.setStatus(ControllerResponseStatus.SUCCESS);
            response.setUserId(user.getId());
        }
        return response;
    }

    public boolean emailIsRegistered(String email) {
        return userRepository.getUserByEmail(email).getPayload() == null;
    }
}

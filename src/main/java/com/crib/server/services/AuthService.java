package com.crib.server.services;

import com.crib.server.Argon2Setup;
import com.crib.server.EnvVariables;
import com.crib.server.JSONWebTokenSetup;
import com.crib.server.common.ctrl_requests.EmailRegisteredRequest;
import com.crib.server.common.ctrl_requests.SignInRequest;
import com.crib.server.common.ctrl_requests.SignUpRequest;
import com.crib.server.common.ctrl_responses.EmailRegisteredResponse;
import com.crib.server.common.ctrl_responses.SignInResponse;
import com.crib.server.common.ctrl_responses.SignUpResponse;
import com.crib.server.common.entities.EmailCode;
import com.crib.server.common.entities.User;
import com.crib.server.common.enums.CtrlResponseStatus;
import com.crib.server.common.patterns.RepoResponse;
import com.crib.server.common.patterns.RepoResponseWP;
import com.crib.server.repositories.RepositoryFactory;
import com.crib.server.repositories.interfaces.IUserRepository;
import com.crib.server.services.helpers.EmailHelper;
import com.crib.server.services.helpers.StringGeneratorHelper;
import com.crib.server.services.helpers.ValidationHelper;

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
        if (ValidationHelper.addValidationErrorsToResponse(request, response)) {
            return response;
        }

        RepoResponseWP<User> repositoryResponse = userRepository.getUserByEmail(request.getEmail());
        User user = repositoryResponse.getPayload();
        if (!repositoryResponse.isSuccessful()) {
            response.setStatus(CtrlResponseStatus.REPOSITORY_ERROR);
            response.addMessage("There was an internal error");
            response.addMessage("Repository Error: " + repositoryResponse.getMessage());
        }
        else if (user == null) {
            response.setStatus(CtrlResponseStatus.AUTH_ERROR);
            response.addMessage("This email is not registered");
        }
        else if (!argon2.rawAndHashAreEqual(request.getPassword(), repositoryResponse.getPayload().getPasswordHash())) {
            response.setStatus(CtrlResponseStatus.AUTH_ERROR);
            response.addMessage("The email and password do not match");
        }
        else {
            response.setStatus(CtrlResponseStatus.SUCCESS);
            response.setUserId(user.getId());
            response.setAuthenticationToken(jwt.generateSignInToken(user.getId()));
        }
        return response;
    }

    public SignUpResponse signUp(SignUpRequest request) {
        SignUpResponse response = new SignUpResponse();
        if (ValidationHelper.addValidationErrorsToResponse(request, response)) {
            return response;
        }

        RepoResponseWP<User> repositoryResponse1 = userRepository.getUserByEmail(request.getEmail());
        if (repositoryResponse1.getPayload() != null) {
            response.setStatus(CtrlResponseStatus.VALIDATION_ERROR);
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

        // TODO send phone verification link
        user.setEmailVerified(false);
        user.setPhoneNumberVerified(false);

        user.setId(UUID.randomUUID().toString());
        user.setTimestamp(new Date().getTime());
        user.setPasswordHash(argon2.createHash(request.getPassword()));

        // Generate email code and send email
        EmailCode code = new EmailCode();
        code.setUserId(UUID.randomUUID().toString());
        code.setTimestamp(new Date().getTime());
        code.setUserId(user.getId());

        String generatedCode = StringGeneratorHelper.generateRandomString(128);
        code.setCodeHash(argon2.createHash(generatedCode));

        EmailHelper.sendPlainTextEmail(user.getFirstName() + " " + user.getLastName(), user.getEmail(),
                "Crib: Verify your email",
                "We are so excited to have you, " + user.getFirstName() + " " + user.getLastName() + "! Before you continue," +
                        "you must verify your email. Click this link to do so: " +
                        EnvVariables.BASE_URL + "verifyemail?code=" + generatedCode + "&userId=" + user.getId());

        RepoResponse repoResponse2 = userRepository.create(user);
        if (repoResponse2.isSuccessful()) {
            response.setStatus(CtrlResponseStatus.SUCCESS);
            response.setUserId(user.getId());
            response.setAuthenticationToken(jwt.generateSignInToken(user.getId()));
        }
        else {
            response.setStatus(CtrlResponseStatus.ERROR);
            response.addMessage(repoResponse2.getMessage());
        }
        return response;
    }

    public EmailRegisteredResponse emailIsRegistered(EmailRegisteredRequest request) {
        EmailRegisteredResponse response = new EmailRegisteredResponse();
        if (ValidationHelper.addValidationErrorsToResponse(request, response)) {
            return response;
        }

        RepoResponseWP<User> repoResponse = userRepository.getUserByEmail(request.getEmail());
        if (repoResponse.isSuccessful()) {
            response.setRegistered(repoResponse == null);
        }
        else {
            response.setStatus(CtrlResponseStatus.REPOSITORY_ERROR);
            response.addMessage(repoResponse.getMessage());
        }
        return response;
    }
}

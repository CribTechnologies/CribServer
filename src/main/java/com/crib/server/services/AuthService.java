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
import com.crib.server.repositories.interfaces.IEmailCodeRepository;
import com.crib.server.repositories.interfaces.IUserRepository;
import com.crib.server.services.helpers.EmailHelper;
import com.crib.server.services.helpers.StringGeneratorHelper;
import com.crib.server.services.helpers.ValidationHelper;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class AuthService extends Service {

    private IUserRepository userRepository;
    private IEmailCodeRepository emailCodeRepository;
    private EnvVariables envVariables;
    private Argon2Setup argon2;
    private JSONWebTokenSetup jwt;
    private EmailHelper emailHelper;

    public AuthService() {
        RepositoryFactory repositoryFactory = RepositoryFactory.getInstance();
        envVariables = EnvVariables.getInstance();
        userRepository = repositoryFactory.getUserRepository();
        emailCodeRepository = repositoryFactory.getEmailCodeRepository();
        argon2 = Argon2Setup.getInstance();
        jwt = JSONWebTokenSetup.getInstance();
        emailHelper = EmailHelper.getInstance();
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
        user.setEmail(request.getEmail().toLowerCase());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setGender(request.getGender());

        // TODO send phone verification link
        user.setEmailVerified(false);
        user.setPhoneNumberVerified(false);

        user.setId(UUID.randomUUID().toString());
        user.setTimestamp(new Date().getTime());
        user.setPasswordHash(argon2.createHash(request.getPassword()));

        // Generate email code
        EmailCode code = new EmailCode();
        code.setId(UUID.randomUUID().toString());
        code.setTimestamp(new Date().getTime());
        code.setUserId(user.getId());

        String generatedCode = StringGeneratorHelper.generateRandomString(128);
        code.setCodeHash(argon2.createHash(generatedCode));

        // Run 3 threads
        // Thread 1: create user
        // Thread 2: create email code
        // Thread 3: send email code
        AtomicReference<RepoResponse> createUserResp = new AtomicReference<>();
        Thread createUser = new Thread(() -> createUserResp.set(userRepository.create(user)));

        AtomicReference<RepoResponse> createEmailCodeResp = new AtomicReference<>();
        Thread createEmailCode = new Thread(() -> createEmailCodeResp.set(emailCodeRepository.create(code)));

        Thread sendEmailCode = new Thread(() -> {
            String emailBody = String.format(
                    "Welcome to Crib, %s!\n\nTo continue registration for your account, click this link to verify your email: %s",
                    user.getFirstName() + " " + user.getLastName(),
                    envVariables.BASE_URL + "verifyemail?code=" + generatedCode + "&userId=" + user.getId()
            );
            emailHelper.sendEmail(user.getEmail(), "Crib: Verify your email", emailBody);
        });

        // Run threads in parallel
        createUser.start();
        createEmailCode.start();
        sendEmailCode.start();
        try {
            createUser.join();
            createEmailCode.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Error handling
        if (!createUserResp.get().isSuccessful()) {
            response.setStatus(CtrlResponseStatus.REPOSITORY_ERROR);
            response.addMessage(createUserResp.get().getMessage());
        }
        if (!createEmailCodeResp.get().isSuccessful()) {
            response.setStatus(CtrlResponseStatus.REPOSITORY_ERROR);
            response.addMessage(createEmailCodeResp.get().getMessage());
        }
        if (response.getMessages().isEmpty()) {
            response.setStatus(CtrlResponseStatus.SUCCESS);
            response.setUserId(user.getId());
            response.setAuthenticationToken(jwt.generateSignInToken(user.getId()));
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

package com.crib.server.services;

import com.crib.server.Argon2Setup;
import com.crib.server.EnvVariables;
import com.crib.server.JSONWebTokenSetup;
import com.crib.server.common.ctrl_requests.EmailRegisteredRequest;
import com.crib.server.common.ctrl_requests.SignInRequest;
import com.crib.server.common.ctrl_requests.SignUpRequest;
import com.crib.server.common.ctrl_requests.VerifyEmailRequest;
import com.crib.server.common.ctrl_responses.EmailRegisteredResponse;
import com.crib.server.common.ctrl_responses.SignInResponse;
import com.crib.server.common.ctrl_responses.SignUpResponse;
import com.crib.server.common.ctrl_responses.VerifyEmailResponse;
import com.crib.server.common.entities.EmailCode;
import com.crib.server.common.entities.User;
import com.crib.server.common.enums.CtrlResponseStatus;
import com.crib.server.common.patterns.RepoResponse;
import com.crib.server.common.patterns.RepoResponseWP;
import com.crib.server.repositories.RepositoryFactory;
import com.crib.server.repositories.interfaces.IEmailCodeRepository;
import com.crib.server.repositories.interfaces.IUserRepository;
import com.crib.server.services.helpers.EmailHelper;
import com.crib.server.services.helpers.IdHelper;
import com.crib.server.services.helpers.StringGeneratorHelper;
import com.crib.server.services.helpers.ValidationHelper;

import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class AuthService extends Service {

    private final IUserRepository userRepository;
    private final IEmailCodeRepository emailCodeRepository;

    private final EnvVariables envVariables;
    private final Argon2Setup argon2;
    private final JSONWebTokenSetup jwt;

    private final EmailHelper emailHelper;
    private final IdHelper idHelper;

    public AuthService() {
        RepositoryFactory repositoryFactory = RepositoryFactory.getInstance();
        envVariables = EnvVariables.getInstance();
        userRepository = repositoryFactory.getUserRepository();
        emailCodeRepository = repositoryFactory.getEmailCodeRepository();
        argon2 = Argon2Setup.getInstance();
        jwt = JSONWebTokenSetup.getInstance();
        emailHelper = EmailHelper.getInstance();
        idHelper = IdHelper.getInstance();
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

        user.setId(idHelper.generateId());
        user.setTimestamp(new Date().getTime());
        user.setPasswordHash(argon2.createHash(request.getPassword()));

        // Generate email code
        EmailCode code = new EmailCode();
        code.setId(idHelper.generateId());
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
                    envVariables.BASE_URL + "api/auth/verifyemail?code=" + generatedCode + "&userId=" + user.getId()
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
            response.setRegistered(repoResponse.getPayload() != null);
        }
        else {
            response.setStatus(CtrlResponseStatus.REPOSITORY_ERROR);
            response.addMessage(repoResponse.getMessage());
        }
        return response;
    }

    public VerifyEmailResponse verifyEmail(VerifyEmailRequest request) {
        VerifyEmailResponse response = new VerifyEmailResponse();
        if (ValidationHelper.addValidationErrorsToResponse(request, response)) {
            return response;
        }

        RepoResponseWP<EmailCode> repoResponse = emailCodeRepository.getEmailCodeByCode(request.getCode());
        if (repoResponse.getPayload() == null) {
            response.setVerified(false);
            response.addMessage("This code does not exist!");
        }
        else if (repoResponse.getPayload().getUserId() != request.getUserId()) {
            response.setVerified(false);
            response.addMessage("The code and userId do not match!");
        }
        else {
            RepoResponse repoResponse2 = userRepository.setEmailVerifiedToTrue(request.getUserId());
            if (repoResponse.isSuccessful()) {
                response.setVerified(true);
            }
            else {
                response.setStatus(CtrlResponseStatus.REPOSITORY_ERROR);
                response.addMessage(repoResponse2.getMessage());
            }
        }
        return response;
    }
}

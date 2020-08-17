package com.crib.server.controllers;

import com.crib.server.common.enums.CtrlResponseStatus;
import com.crib.server.common.patterns.CtrlResponseWP;
import com.crib.server.common.ctrl_requests.SignInRequest;
import com.crib.server.common.ctrl_requests.SignUpRequest;
import com.crib.server.common.ctrl_responses.SignInResponse;
import com.crib.server.common.ctrl_responses.SignUpResponse;
import com.crib.server.services.AuthService;
import com.crib.server.services.ServiceFactory;
import com.crib.server.services.helpers.ValidationHelper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/signin")
    public SignInResponse signIn(@RequestBody SignInRequest request) {
        return ServiceFactory.getInstance()
                .getAuthService()
                .signIn(request);
    }

    @PostMapping("/signup")
    public SignUpResponse signUp(@RequestBody SignUpRequest request) {
        return ServiceFactory.getInstance()
                .getAuthService()
                .signUp(request);
    }

    @PostMapping("/emailisregistered")
    public CtrlResponseWP<Boolean> emailIsRegistered(@RequestBody @NotNull @Email String email) {
        return ServiceFactory.getInstance()
                .getAuthService()
                .emailIsRegistered(email);
    }
}

package com.crib.server.controllers;

import com.crib.server.common.patterns.CtrlResponseWP;
import com.crib.server.common.ctrl_requests.SignInRequest;
import com.crib.server.common.ctrl_requests.SignUpRequest;
import com.crib.server.common.ctrl_responses.SignInResponse;
import com.crib.server.common.ctrl_responses.SignUpResponse;
import com.crib.server.services.AuthService;
import com.crib.server.services.ServiceFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;

    public AuthController() {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        authService = serviceFactory.getAuthService();
    }

    @GetMapping("/signin")
    public SignInResponse signIn(@RequestBody SignInRequest request) {
        return authService.signIn(request);
    }

    @PostMapping("/signup")
    public SignUpResponse signUp(@RequestBody SignUpRequest request) {
        return authService.signUp(request);
    }

    @GetMapping("/emailisregistered")
    public CtrlResponseWP<Boolean> emailIsRegistered(@RequestBody @NotNull @Email String email) {
        return authService.emailIsRegistered(email);
    }
}

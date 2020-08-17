package com.crib.server.controllers;

import com.crib.server.common.ctrl_requests.EmailRegisteredRequest;
import com.crib.server.common.ctrl_requests.SignInRequest;
import com.crib.server.common.ctrl_requests.SignUpRequest;
import com.crib.server.common.ctrl_responses.EmailRegisteredResponse;
import com.crib.server.common.ctrl_responses.SignInResponse;
import com.crib.server.common.ctrl_responses.SignUpResponse;
import com.crib.server.services.ServiceFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public EmailRegisteredResponse emailIsRegistered(EmailRegisteredRequest request) {
        return ServiceFactory.getInstance()
                .getAuthService()
                .emailIsRegistered(request);
    }
}

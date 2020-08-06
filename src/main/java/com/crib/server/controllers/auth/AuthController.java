package com.crib.server.controllers.auth;

import com.crib.server.controllers.auth.requests.SignInRequest;
import com.crib.server.controllers.auth.requests.SignUpRequest;
import com.crib.server.controllers.auth.responses.SignInResponse;
import com.crib.server.controllers.auth.responses.SignUpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/signin")
    public SignInResponse signIn(@RequestBody SignInRequest request) {
        return new SignInResponse();
    }

    @PostMapping("/signup")
    public SignUpResponse signUp(@RequestBody SignUpRequest request) {
        return new SignUpResponse();
    }
}

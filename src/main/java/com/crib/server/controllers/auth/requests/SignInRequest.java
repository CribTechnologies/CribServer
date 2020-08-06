package com.crib.server.controllers.auth.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SignInRequest {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 1, max = 1024)
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

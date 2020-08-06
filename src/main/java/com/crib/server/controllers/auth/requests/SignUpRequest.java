package com.crib.server.controllers.auth.requests;

import com.crib.server.common.enums.Gender;
import com.crib.server.common.patterns.ControllerRequest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Past;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

public class SignUpRequest extends ControllerRequest {

    @NotNull
    @Size(min = 1, max = 128)
    private String firstName;

    @NotNull
    @Size(min = 1, max = 128)
    private String lastName;

    @NotNull
    @Past
    private Date dateOfBirth;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 1, max = 1024)
    private String password;

    @NotNull
    @NotBlank
    private String phoneNumber;

    @NotNull
    private Gender gender;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Gender getGender() {
        return gender;
    }
}

package com.crib.server.common.ctrl_requests;

import com.crib.server.common.enums.Gender;
import com.crib.server.common.patterns.CtrlRequest;

import javax.validation.constraints.*;
import java.util.Date;

public class SignUpRequest extends CtrlRequest {

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
    @NotBlank
    private String email;

    @NotNull
    @Size(min = 6, max = 1024)
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

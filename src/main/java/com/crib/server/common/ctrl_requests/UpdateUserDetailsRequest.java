package com.crib.server.common.ctrl_requests;

import com.crib.server.common.enums.Gender;
import com.crib.server.common.patterns.CtrlRequest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

public class UpdateUserDetailsRequest extends CtrlRequest {

    @NotNull
    @Size(min = 1, max = 128)
    private String userId;

    @NotNull
    @Size(min = 6, max = 1024)
    private String password;

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
    private Gender gender;

    public UpdateUserDetailsRequest() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}

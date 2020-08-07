package com.crib.server.controllers;

import com.crib.server.common.enums.Gender;
import com.crib.server.common.patterns.CtrlResponse;
import com.crib.server.services.ServiceFactory;
import com.crib.server.services.UserService;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    public UserController() {
        ServiceFactory factory = ServiceFactory.getInstance();
        userService = factory.getUserService();
    }

    @PatchMapping("/name")
    public CtrlResponse updateName(@RequestBody @NotNull @Size(min = 1, max = 128) String userId,
                                   @RequestBody @NotNull @Size(min = 1, max = 128) String firstName,
                                   @RequestBody @NotNull @Size(min = 1, max = 128) String lastName) {
        return userService.updateFirstAndLastName(userId, firstName, lastName);
    }

    @PatchMapping("/dob")
    public CtrlResponse updateDateOfBirth(@RequestBody @NotNull @Size(min = 1, max = 128) String userId,
                                          @RequestBody @NotNull @Past Date dateOfBirth) {
        return userService.updateDateOfBirth(userId, dateOfBirth);
    }

    @PatchMapping("/gender")
    public CtrlResponse updateGender(@RequestBody @NotNull @Size(min = 1, max = 128) String userId,
                                     @RequestBody @NotNull Gender gender) {
        return userService.updateGender(userId, gender);
    }

    @PatchMapping("/password")
    public CtrlResponse updatePassword(@RequestBody @NotNull @Size(min = 1, max = 128) String userId,
                                       @RequestBody @NotNull @Size(min = 6, max = 1024) String oldPassword,
                                       @RequestBody @NotNull @Size(min = 6, max = 1024) String newPassword) {
        return userService.updatePassword(userId, oldPassword, newPassword);
    }

    @PatchMapping("/email")
    public CtrlResponse updateEmail(@RequestBody @NotNull @Size(min = 1, max = 128) String userId,
                                    @RequestBody @NotNull @Size(min = 6, max = 1024) String password,
                                    @RequestBody @NotNull @Email @Size(min = 6, max = 1024) String email) {
        return userService.updateEmail(userId, password, email);
    }

    @PatchMapping("/phonenumber")
    public CtrlResponse updatePhoneNumber(@RequestBody @NotNull @Size(min = 1, max = 128) String userId,
                                          @RequestBody @NotNull @Size(min = 6, max = 1024) String password,
                                          @RequestBody @NotNull @Size(min = 1, max = 128) String phoneNumber) {
        return userService.updateEmail(userId, password, phoneNumber);
    }
}

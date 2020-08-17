package com.crib.server.controllers;

import com.crib.server.common.enums.Gender;
import com.crib.server.common.patterns.CtrlResponse;
import com.crib.server.services.ServiceFactory;
import com.crib.server.services.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @PostMapping("/updateUser/name")
    public CtrlResponse updateName(@RequestBody @NotNull @Size(min = 1, max = 128) String userId,
                                   @RequestBody @NotNull @Size(min = 1, max = 128) String firstName,
                                   @RequestBody @NotNull @Size(min = 1, max = 128) String lastName) {
        return ServiceFactory.getInstance()
                .getUserService()
                .updateFirstAndLastName(userId, firstName, lastName);
    }

    @PostMapping("/updateUser/dob")
    public CtrlResponse updateDateOfBirth(@RequestBody @NotNull @Size(min = 1, max = 128) String userId,
                                          @RequestBody @NotNull @Past Date dateOfBirth) {
        return ServiceFactory.getInstance()
                .getUserService()
                .updateDateOfBirth(userId, dateOfBirth);
    }

    @PostMapping("/updateUser/gender")
    public CtrlResponse updateGender(@RequestBody @NotNull @Size(min = 1, max = 128) String userId,
                                     @RequestBody @NotNull Gender gender) {
        return ServiceFactory.getInstance()
                .getUserService()
                .updateGender(userId, gender);
    }

    @PostMapping("/updateUser/password")
    public CtrlResponse updatePassword(@RequestBody @NotNull @Size(min = 1, max = 128) String userId,
                                       @RequestBody @NotNull @Size(min = 6, max = 1024) String oldPassword,
                                       @RequestBody @NotNull @Size(min = 6, max = 1024) String newPassword) {
        return ServiceFactory.getInstance()
                .getUserService()
                .updatePassword(userId, oldPassword, newPassword);
    }

    @PostMapping("/updateUser/email")
    public CtrlResponse updateEmail(@RequestBody @NotNull @Size(min = 1, max = 128) String userId,
                                    @RequestBody @NotNull @Size(min = 6, max = 1024) String password,
                                    @RequestBody @NotNull @Email @Size(min = 6, max = 1024) String email) {
        return ServiceFactory.getInstance()
                .getUserService()
                .updateEmail(userId, password, email);
    }

    @PostMapping("/updateUser/phonenumber")
    public CtrlResponse updatePhoneNumber(@RequestBody @NotNull @Size(min = 1, max = 128) String userId,
                                          @RequestBody @NotNull @Size(min = 6, max = 1024) String password,
                                          @RequestBody @NotNull @Size(min = 1, max = 128) String phoneNumber) {
        return ServiceFactory.getInstance()
                .getUserService()
                .updateEmail(userId, password, phoneNumber);
    }
}

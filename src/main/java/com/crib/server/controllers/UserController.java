package com.crib.server.controllers;

import com.crib.server.common.ctrl_requests.UpdateUserDetailsRequest;
import com.crib.server.common.ctrl_requests.UpdateUserEmailRequest;
import com.crib.server.common.ctrl_requests.UpdateUserPasswordRequest;
import com.crib.server.common.ctrl_requests.UpdateUserPhoneRequest;
import com.crib.server.common.patterns.CtrlResponse;
import com.crib.server.services.ServiceFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @PostMapping("/updateUser/details")
    public CtrlResponse updateDetails(@RequestBody UpdateUserDetailsRequest request) {
        return ServiceFactory.getInstance()
                .getUserService()
                .updateDetails(request);
    }

    @PostMapping("/updateUser/password")
    public CtrlResponse updatePassword(@RequestBody UpdateUserPasswordRequest request) {
        return ServiceFactory.getInstance()
                .getUserService()
                .updatePassword(request);
    }

    @PostMapping("/updateUser/email")
    public CtrlResponse updateEmail(@RequestBody UpdateUserEmailRequest request) {
        return ServiceFactory.getInstance()
                .getUserService()
                .updateEmail(request);
    }

    @PostMapping("/updateUser/phonenumber")
    public CtrlResponse updatePhoneNumber(@RequestBody UpdateUserPhoneRequest request) {
        return ServiceFactory.getInstance()
                .getUserService()
                .updatePhoneNumber(request);
    }
}

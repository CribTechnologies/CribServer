package com.crib.server.controllers;

import com.crib.server.common.ctrl_requests.RegisterLockRequest;
import com.crib.server.common.ctrl_requests.UpdateLockNameRequest;
import com.crib.server.common.ctrl_requests.UpdateLockTypeRequest;
import com.crib.server.common.ctrl_responses.RegisterLockResponse;
import com.crib.server.common.patterns.CtrlResponse;
import com.crib.server.services.ServiceFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lock")
public class LockController {

    @PostMapping("/register")
    public RegisterLockResponse registerLock(@RequestBody RegisterLockRequest request) {
        return ServiceFactory.getInstance()
                .getLockService()
                .registerLock(request);
    }

    @PostMapping("/updateLock/name")
    public CtrlResponse updateLockName(@RequestBody UpdateLockNameRequest request) {
        return ServiceFactory.getInstance()
                .getLockService()
                .updateName(request);
    }

    @PostMapping("/updateLock/type")
    public CtrlResponse updateLockType(@RequestBody UpdateLockTypeRequest request) {
        return ServiceFactory.getInstance()
                .getLockService()
                .updateLockType(request);
    }
}

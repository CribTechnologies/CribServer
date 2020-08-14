package com.crib.server.controllers;

import com.crib.server.common.ctrl_requests.RegisterLockRequest;
import com.crib.server.common.ctrl_responses.RegisterLockResponse;
import com.crib.server.common.enums.LockType;
import com.crib.server.common.patterns.CtrlResponse;
import com.crib.server.services.LockService;
import com.crib.server.services.ServiceFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RestController
@RequestMapping("/api/lock")
public class LockController {

    private LockService lockService;

    public LockController() {
        ServiceFactory serviceFactory = ServiceFactory.getInstance();
        lockService = serviceFactory.getLockService();
    }

    @PostMapping("/register")
    public RegisterLockResponse registerLock(@RequestBody RegisterLockRequest request) {
        return lockService.registerLock(request);
    }

    @PostMapping("/updateLock/name")
    public CtrlResponse updateLockName(@RequestBody @NotNull @Size(min = 1, max = 128) String lockId,
                                       @RequestBody @NotNull @Size(min = 1, max = 128) String name) {
        return lockService.updateName(lockId, name);
    }

    @PostMapping("/updateLock/type")
    public CtrlResponse updateLockType(@RequestBody @NotNull @Size(min = 1, max = 128) String lockId,
                                       @RequestBody @NotNull LockType lockType) {
        return lockService.updateLockType(lockId, lockType);
    }
}

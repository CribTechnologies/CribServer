package com.crib.server.controllers.lock;

import com.crib.server.controllers.lock.requests.RegisterLockRequest;
import com.crib.server.controllers.lock.responses.RegisterLockResponse;
import com.crib.server.services.LockService;
import com.crib.server.services.ServiceFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

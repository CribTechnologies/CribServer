package com.crib.server.controllers;

import com.crib.server.common.ctrl_requests.*;
import com.crib.server.common.ctrl_responses.HomeResponse;
import com.crib.server.common.ctrl_responses.ViewHomeDetailsResponse;
import com.crib.server.common.patterns.CtrlResponse;
import com.crib.server.services.ServiceFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    @PostMapping("/create")
    public HomeResponse createHome(@RequestBody CreateHomeRequest request) {
        return ServiceFactory.getInstance()
                .getHomeService()
                .createHome(request);
    }

    @PostMapping("/getById")
    public HomeResponse getHomeById(@RequestBody HomeIdRequest request) {
        return ServiceFactory.getInstance()
                .getHomeService()
                .getHomeById(request);
    }

    @PostMapping("/getDetails")
    public ViewHomeDetailsResponse viewHomeDetails(@RequestBody HomeIdRequest request) {
        return ServiceFactory.getInstance()
                .getHomeService()
                .getHomeDetails(request);
    }

    @PostMapping("/delete")
    public CtrlResponse deleteHomeById(@RequestBody HomeIdRequest request) {
        return ServiceFactory.getInstance()
                .getHomeService()
                .deleteHomeById(request);
    }

    @PostMapping("/addLock")
    public CtrlResponse addLockToHome(@RequestBody HomeAndLockIdRequest request) {
        return ServiceFactory.getInstance()
                .getHomeService()
                .addLockToHome(request);
    }

    @PostMapping("/removeLock")
    public CtrlResponse removeLockFromHome(@RequestBody HomeAndLockIdRequest request) {
        return ServiceFactory.getInstance()
                .getHomeService()
                .removeLockFromHome(request);
    }

    @PostMapping("/addMemberRole")
    public CtrlResponse addUserWithRole(@RequestBody RoleChangeRequest request) {
        return ServiceFactory.getInstance()
                .getHomeService()
                .addUserWithRole(request);
    }

    @PostMapping("/removeMemberRole")
    public CtrlResponse removeUserWithRole(@RequestBody RoleChangeRequest request) {
        return ServiceFactory.getInstance()
                .getHomeService()
                .removeUserWithRole(request);
    }

    @PostMapping("/changeMemberRole")
    public CtrlResponse changeUserWithRole(@RequestBody RoleChangeRequest request) {
        return ServiceFactory.getInstance()
                .getHomeService()
                .changeUserWithRole(request);
    }

    @PostMapping("/updateSettings/name")
    public CtrlResponse updateName(@RequestBody UpdateHomeNameRequest request) {
        return ServiceFactory.getInstance()
                .getHomeService()
                .updateName(request);
    }

    @PostMapping("/updateSettings/address")
    public CtrlResponse updateAddress(@RequestBody UpdateHomeAddressRequest request) {
        return ServiceFactory.getInstance()
                .getHomeService()
                .updateAddress(request);
    }
}

package com.crib.server.controllers;

import com.crib.server.common.ctrl_requests.CreateHomeRequest;
import com.crib.server.common.ctrl_requests.UpdateHomeAddressRequest;
import com.crib.server.common.ctrl_responses.ViewHomeDetailsResponse;
import com.crib.server.common.entities.Home;
import com.crib.server.common.enums.UserHomeRole;
import com.crib.server.common.patterns.CtrlResponse;
import com.crib.server.common.patterns.CtrlResponseWP;
import com.crib.server.services.HomeService;
import com.crib.server.services.ServiceFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    private HomeService homeService;

    public HomeController() {
        ServiceFactory factory = ServiceFactory.getInstance();
        homeService = factory.getHomeService();
    }

    @PostMapping("/create")
    public CtrlResponseWP<Home> createHome(@RequestBody CreateHomeRequest request) {
        return homeService.createHome(request);
    }

    @PostMapping("/getById")
    public CtrlResponseWP<Home> getHomeById(@RequestBody @NotNull @Size(min = 1, max = 128) String homeId,
                                            @RequestBody @NotNull @Size(min = 1, max = 128) String userAccessorId) {
        return homeService.getHomeById(homeId, userAccessorId);
    }

    @PostMapping("/getDetails")
    public ViewHomeDetailsResponse viewHomeDetails(@RequestBody @NotNull @Size(min = 1, max = 128) String homeId,
                                                   @RequestBody @NotNull @Size(min = 1, max = 128) String userAccessorId) {
        return homeService.getHomeDetails(homeId, userAccessorId);
    }

    @PostMapping("/delete")
    public CtrlResponse deleteHomeById(@RequestBody @NotNull @Size(min = 1, max = 128) String homeId,
                                       @RequestBody @NotNull @Size(min = 1, max = 128) String userAccessorId) {
        return homeService.deleteHomeById(homeId, userAccessorId);
    }

    @PostMapping("/addLock")
    public CtrlResponse addLockToHome(@RequestBody @NotNull @Size(min = 1, max = 128) String homeId,
                                      @RequestBody @NotNull @Size(min = 1, max = 128) String userAccessorId,
                                      @RequestBody @NotNull @Size(min = 1, max = 128) String lockId) {
        return homeService.addLockToHome(homeId, userAccessorId, lockId);
    }

    @PostMapping("/removeLock")
    public CtrlResponse removeLockFromHome(@RequestBody @NotNull @Size(min = 1, max = 128) String homeId,
                                           @RequestBody @NotNull @Size(min = 1, max = 128) String userAccessorId,
                                           @RequestBody @NotNull @Size(min = 1, max = 128) String lockId) {
        return homeService.removeLockFromHome(homeId, userAccessorId, lockId);
    }

    @PostMapping("/addMemberRole")
    public CtrlResponse addUserWithRole(@RequestBody @NotNull @Size(min = 1, max = 128) String homeId,
                                      @RequestBody @NotNull @Size(min = 1, max = 128) String userAccessorId,
                                      @RequestBody @NotNull @Size(min = 1, max = 128) String userId,
                                      @RequestBody @NotNull UserHomeRole role) {
        return homeService.addUserWithRole(homeId, userAccessorId, userId, role);
    }

    @PostMapping("/removeMemberRole")
    public CtrlResponse removeUserWithRole(@RequestBody @NotNull @Size(min = 1, max = 128) String homeId,
                                           @RequestBody @NotNull @Size(min = 1, max = 128) String userAccessorId,
                                           @RequestBody @NotNull @Size(min = 1, max = 128) String userId,
                                           @RequestBody @NotNull UserHomeRole role) {
        return homeService.removeUserWithRole(homeId, userAccessorId, userId, role);
    }

    @PostMapping("/changeMemberRole")
    public CtrlResponse changeUserWithRole(@RequestBody @NotNull @Size(min = 1, max = 128) String homeId,
                                           @RequestBody @NotNull @Size(min = 1, max = 128) String userAccessorId,
                                           @RequestBody @NotNull @Size(min = 1, max = 128) String userId,
                                           @RequestBody @NotNull UserHomeRole role) {
        return homeService.changeUserWithRole(homeId, userAccessorId, userId, role);
    }

    @PostMapping("/updateSettings/name")
    public CtrlResponse updateName(@RequestBody @NotNull @Size(min = 1, max = 128) String homeId,
                                   @RequestBody @NotNull @Size(min = 1, max = 128) String userAccessorId,
                                   @RequestBody @NotNull @Size(min = 1, max = 128) String name) {
        return homeService.updateName(homeId, userAccessorId, name);
    }

    @PostMapping("/updateSettings/address")
    public CtrlResponse updateAddress(@RequestBody UpdateHomeAddressRequest request) {
        return homeService.updateAddress(request);
    }
}

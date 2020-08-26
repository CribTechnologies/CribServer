package com.crib.server.services;

import com.crib.server.common.ctrl_requests.*;
import com.crib.server.common.ctrl_responses.HomeResponse;
import com.crib.server.common.ctrl_responses.ViewHomeDetailsResponse;
import com.crib.server.common.entities.Home;
import com.crib.server.common.entities.Lock;
import com.crib.server.common.entities.User;
import com.crib.server.common.enums.CtrlResponseStatus;
import com.crib.server.common.enums.UserHomeRole;
import com.crib.server.common.patterns.CtrlResponse;
import com.crib.server.common.patterns.RepoResponse;
import com.crib.server.common.patterns.RepoResponseWP;
import com.crib.server.common.value_objects.PhysicalAddress;
import com.crib.server.common.value_objects.UserIdWithRole;
import com.crib.server.repositories.RepositoryFactory;
import com.crib.server.repositories.interfaces.IHomeRepository;
import com.crib.server.repositories.interfaces.ILockRepository;
import com.crib.server.repositories.interfaces.IUserRepository;
import com.crib.server.services.helpers.IdHelper;
import com.crib.server.services.helpers.ValidationHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class HomeService extends Service {

    // TODO decide on role permissions for each method
    // TODO authentication - who can access what method AND whether they are member of house

    private final IHomeRepository homeRepository;
    private final ILockRepository lockRepository;
    private final IUserRepository userRepository;

    private final IdHelper idHelper;

    public HomeService() {
        RepositoryFactory repositoryFactory = RepositoryFactory.getInstance();
        homeRepository = repositoryFactory.getHomeRepository();
        lockRepository = repositoryFactory.getLockRepository();
        userRepository = repositoryFactory.getUserRepository();
        idHelper = IdHelper.getInstance();
    }

    // Reused private methods
    private UserHomeRole getRoleOfUserInHome(String homeId, String userId) {
        RepoResponseWP<Home> repoResponseWP = homeRepository.getById(homeId);
        for (UserIdWithRole uiwr : repoResponseWP.getPayload().getUsers()) {
            if (uiwr.getUserId().equals(userId)) {
                return uiwr.getRole();
            }
        }
        return null;
    }

    // Public methods
    public HomeResponse createHome(CreateHomeRequest request) {
        HomeResponse response = new HomeResponse();

        PhysicalAddress address = new PhysicalAddress();
        address.setPrimaryStreet(request.getAddressPrimaryStreet());
        address.setSecondaryStreet(request.getAddressSecondaryStreet());
        address.setCity(request.getAddressCity());
        address.setState(request.getAddressState());
        address.setCountry(request.getAddressCountry());
        address.setZipCode(request.getAddressZipCode());

        String fullAddress = String.format("%s, %s, %s, %s %s, %s",
                request.getAddressPrimaryStreet(),
                request.getAddressSecondaryStreet(),
                request.getAddressCity(),
                request.getAddressState(),
                request.getAddressCountry(),
                request.getAddressZipCode());
        address.setFullAddress(fullAddress);

        Home home = new Home();
        home.setId(idHelper.generateId());
        home.setTimestamp(new Date().getTime());
        home.setName(request.getHomeName());
        home.setLockIds(new ArrayList<>());
        home.setAddress(address);

        List<UserIdWithRole> uiwrs = new ArrayList<>();
        UserIdWithRole uiwr = new UserIdWithRole();
        uiwr.setUserId(request.getUserCreatorId());
        uiwr.setRole(UserHomeRole.ADMIN);
        uiwrs.add(uiwr);
        home.setUsers(uiwrs);

        RepoResponse repoResponse = homeRepository.create(home);
        if (repoResponse.isSuccessful()) {
            response.setStatus(CtrlResponseStatus.SUCCESS);
            response.setHome(home);
        }
        else {
            response.setStatus(CtrlResponseStatus.REPOSITORY_ERROR);
            response.addMessage(repoResponse.getMessage());
        }
        return response;
    }

    public HomeResponse getHomeById(HomeIdRequest request) {
        HomeResponse response = new HomeResponse();
        if (ValidationHelper.addValidationErrorsToResponse(request, response)) {
            return response;
        }

        RepoResponseWP<Home> repoResponse = homeRepository.getById(request.getHomeId());
        if (repoResponse.isSuccessful()) {
            response.setStatus(CtrlResponseStatus.SUCCESS);
            response.setHome(repoResponse.getPayload());
        }
        else {
            response.setStatus(CtrlResponseStatus.REPOSITORY_ERROR);
            response.addMessage(repoResponse.getMessage());
        }
        return response;
    }

    public ViewHomeDetailsResponse getHomeDetails(HomeIdRequest request) {
        ViewHomeDetailsResponse response = new ViewHomeDetailsResponse();
        if (ValidationHelper.addValidationErrorsToResponse(request, response)) {
            return response;
        }

        try {
            RepoResponseWP<Home> repoResponseWP = homeRepository.getById(request.getHomeId());
            Home home = repoResponseWP.getPayload();

            AtomicReference<RepoResponseWP<List<User>>> getUsersResponseRef = new AtomicReference<>();
            Thread getUsersThread = new Thread(() -> {
                List<String> userIds = home
                        .getUsers()
                        .stream()
                        .map(UserIdWithRole::getUserId)
                        .collect(Collectors.toList());

                getUsersResponseRef.set(userRepository.getByIds(userIds));
            });

            AtomicReference<RepoResponseWP<List<Lock>>> getLocksResponseRef = new AtomicReference<>();
            Thread getLocksThread = new Thread(() -> {
                getLocksResponseRef.set(lockRepository.getByIds(home.getLockIds()));
            });

            getUsersThread.start();
            getLocksThread.start();
            getUsersThread.join();
            getLocksThread.join();

            response.setStatus(CtrlResponseStatus.SUCCESS);
            response.setHome(home);
            response.setUsers(getUsersResponseRef.get().getPayload());
            response.setLocks(getLocksResponseRef.get().getPayload());
        }
        catch (Exception e) {
            response.setStatus(CtrlResponseStatus.REPOSITORY_ERROR);
            response.addMessage(e.getMessage());
        }
        return response;
    }

    public CtrlResponse deleteHomeById(HomeIdRequest request) {
        CtrlResponse response = new CtrlResponse();
        if (ValidationHelper.addValidationErrorsToResponse(request, response)) {
            return response;
        }
        RepoResponse repoResponse = homeRepository.delete(request.getHomeId());
        if (repoResponse.isSuccessful()) {
            response.setStatus(CtrlResponseStatus.SUCCESS);
        }
        else {
            response.setStatus(CtrlResponseStatus.REPOSITORY_ERROR);
            response.addMessage(repoResponse.getMessage());
        }
        return response;
    }

    public CtrlResponse addLockToHome(HomeAndLockIdRequest request) {
        CtrlResponse response = new CtrlResponse();
        if (ValidationHelper.addValidationErrorsToResponse(request, response)) {
            return response;
        }
        RepoResponse repoResponse = homeRepository.addLockToHome(request.getHomeId(), request.getLockId());
        if (repoResponse.isSuccessful()) {
            response.setStatus(CtrlResponseStatus.SUCCESS);
        }
        else {
            response.setStatus(CtrlResponseStatus.REPOSITORY_ERROR);
            response.addMessage(repoResponse.getMessage());
        }
        return response;
    }

    public CtrlResponse removeLockFromHome(HomeAndLockIdRequest request) {
        CtrlResponse response = new CtrlResponse();
        if (ValidationHelper.addValidationErrorsToResponse(request, response)) {
            return response;
        }
        RepoResponse repoResponse = homeRepository.removeLockFromHome(request.getHomeId(), request.getLockId());
        if (repoResponse.isSuccessful()) {
            response.setStatus(CtrlResponseStatus.SUCCESS);
        }
        else {
            response.setStatus(CtrlResponseStatus.REPOSITORY_ERROR);
            response.addMessage(repoResponse.getMessage());
        }
        return response;
    }

    public CtrlResponse addUserWithRole(RoleChangeRequest request) {
        CtrlResponse response = new CtrlResponse();
        if (ValidationHelper.addValidationErrorsToResponse(request, response)) {
            return response;
        }
        UserIdWithRole uiwr = new UserIdWithRole();
        uiwr.setUserId(request.getUserId());
        uiwr.setRole(request.getRole());

        RepoResponse repoResponse = homeRepository.addUserWithRole(request.getHomeId(), uiwr);
        if (repoResponse.isSuccessful()) {
            response.setStatus(CtrlResponseStatus.SUCCESS);
        }
        else {
            response.setStatus(CtrlResponseStatus.REPOSITORY_ERROR);
            response.addMessage(repoResponse.getMessage());
        }
        return response;
    }

    public CtrlResponse removeUserWithRole(RoleChangeRequest request) {
        CtrlResponse response = new CtrlResponse();
        if (ValidationHelper.addValidationErrorsToResponse(request, response)) {
            return response;
        }
        UserIdWithRole uiwr = new UserIdWithRole();
        uiwr.setUserId(request.getUserId());
        uiwr.setRole(request.getRole());

        RepoResponse repoResponse = homeRepository.removeUserWithRole(request.getHomeId(), uiwr);
        if (repoResponse.isSuccessful()) {
            response.setStatus(CtrlResponseStatus.SUCCESS);
        }
        else {
            response.setStatus(CtrlResponseStatus.REPOSITORY_ERROR);
            response.addMessage(repoResponse.getMessage());
        }
        return response;
    }

    public CtrlResponse changeUserWithRole(RoleChangeRequest request) {
        CtrlResponse response = new CtrlResponse();
        if (ValidationHelper.addValidationErrorsToResponse(request, response)) {
            return response;
        }
        UserIdWithRole uiwr = new UserIdWithRole();
        uiwr.setUserId(request.getUserId());
        uiwr.setRole(request.getRole());

        RepoResponse repoResponse = homeRepository.changeUserWithRole(request.getHomeId(), uiwr);
        if (repoResponse.isSuccessful()) {
            response.setStatus(CtrlResponseStatus.SUCCESS);
        }
        else {
            response.setStatus(CtrlResponseStatus.REPOSITORY_ERROR);
            response.addMessage(repoResponse.getMessage());
        }
        return response;
    }

    public CtrlResponse updateName(UpdateHomeNameRequest request) {
        CtrlResponse response = new CtrlResponse();
        if (ValidationHelper.addValidationErrorsToResponse(request, response)) {
            return response;
        }

        RepoResponse repoResponse = homeRepository.updateName(request.getHomeId(), request.getName());
        if (repoResponse.isSuccessful()) {
            response.setStatus(CtrlResponseStatus.SUCCESS);
        }
        else {
            response.setStatus(CtrlResponseStatus.REPOSITORY_ERROR);
            response.addMessage(repoResponse.getMessage());
        }
        return response;
    }

    // request includes userAccessorId
    public CtrlResponse updateAddress(UpdateHomeAddressRequest request) {
        PhysicalAddress address = new PhysicalAddress();
        address.setPrimaryStreet(request.getAddressPrimaryStreet());
        address.setSecondaryStreet(request.getAddressSecondaryStreet());
        address.setCity(request.getAddressCity());
        address.setState(request.getAddressState());
        address.setCountry(request.getAddressCountry());
        address.setZipCode(request.getAddressZipCode());

        String fullAddress = String.format("%s, %s, %s, %s %s, %s",
                request.getAddressPrimaryStreet(),
                request.getAddressSecondaryStreet(),
                request.getAddressCity(),
                request.getAddressState(),
                request.getAddressCountry(),
                request.getAddressZipCode());
        address.setFullAddress(fullAddress);

        return repoToCtrlResponse(homeRepository.updateAddress(request.getHomeId(), address));
    }
}

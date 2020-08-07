package com.crib.server.services;

import com.crib.server.common.ctrl_requests.CreateHomeRequest;
import com.crib.server.common.ctrl_responses.ViewHomeDetailsResponse;
import com.crib.server.common.entities.Home;
import com.crib.server.common.entities.Lock;
import com.crib.server.common.entities.User;
import com.crib.server.common.enums.ControllerResponseStatus;
import com.crib.server.common.enums.UserHomeRole;
import com.crib.server.common.patterns.CtrlResponse;
import com.crib.server.common.patterns.CtrlResponseWP;
import com.crib.server.common.patterns.RepoResponse;
import com.crib.server.common.patterns.RepoResponseWP;
import com.crib.server.common.value_objects.PhysicalAddress;
import com.crib.server.common.value_objects.UserIdWithRole;
import com.crib.server.repositories.RepositoryFactory;
import com.crib.server.repositories.interfaces.IHomeRepository;
import com.crib.server.repositories.interfaces.ILockRepository;
import com.crib.server.repositories.interfaces.IUserRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class HomeService extends Service {

    // TODO decide on role permissions for each method
    // TODO authentication - who can access what method AND whether they are member of house

    private final IHomeRepository homeRepository;
    private final ILockRepository lockRepository;
    private final IUserRepository userRepository;

    public HomeService() {
        RepositoryFactory repositoryFactory = RepositoryFactory.getInstance();
        homeRepository = repositoryFactory.getHomeRepository();
        lockRepository = repositoryFactory.getLockRepository();
        userRepository = repositoryFactory.getUserRepository();
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
    public CtrlResponseWP<Home> createHome(CreateHomeRequest request) {
        CtrlResponseWP<Home> ctrlResponseWP = new CtrlResponseWP<>();

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
        home.setId(UUID.randomUUID().toString());
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
            ctrlResponseWP.setStatus(ControllerResponseStatus.SUCCESS);
            ctrlResponseWP.setPayload(home);
        }
        else {
            ctrlResponseWP.setStatus(ControllerResponseStatus.REPOSITORY_ERROR);
            ctrlResponseWP.addMessage(repoResponse.getMessage());
        }
        return ctrlResponseWP;
    }

    public CtrlResponseWP<Home> getHomeById(String homeId) {
        return repoToCtrlResponseWithPayload(homeRepository.getById(homeId));
    }

    public ViewHomeDetailsResponse getHomeDetails(String homeId) {
        ViewHomeDetailsResponse response = new ViewHomeDetailsResponse();
        try {
            RepoResponseWP<Home> repoResponseWP = homeRepository.getById(homeId);
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

            response.setStatus(ControllerResponseStatus.SUCCESS);
            response.setHome(home);
            response.setUsers(getUsersResponseRef.get().getPayload());
            response.setLocks(getLocksResponseRef.get().getPayload());
        }
        catch (Exception e) {
            response.setStatus(ControllerResponseStatus.REPOSITORY_ERROR);
            response.addMessage(e.getMessage());
        }
        return response;
    }

    public CtrlResponse deleteHomeById(String homeId, String userAccessorId) {
        return repoToCtrlResponse(homeRepository.delete(homeId));
    }

    public CtrlResponse addLockToHome(String homeId, String userAccessorId, String lockId) {
        return repoToCtrlResponse(homeRepository.addLockToHome(homeId, lockId));
    }

    public CtrlResponse removeLockFromHome(String homeId, String userAccessorId, String lockId) {
        return repoToCtrlResponse(homeRepository.removeLockFromHome(homeId, lockId));
    }

    public CtrlResponse addUserWithRole(String homeId, String userAccessorId, String userId, UserHomeRole role) {
        UserIdWithRole uiwr = new UserIdWithRole();
        uiwr.setUserId(userId);
        uiwr.setRole(role);
        return repoToCtrlResponse(homeRepository.addUserWithRole(homeId, uiwr));
    }

    public CtrlResponse removeUserWithRole(String homeId, String userAccessorId, String userId, UserHomeRole role) {
        UserIdWithRole uiwr = new UserIdWithRole();
        uiwr.setUserId(userId);
        uiwr.setRole(role);
        return repoToCtrlResponse(homeRepository.removeUserWithRole(homeId, uiwr));
    }

    public CtrlResponse changeUserWithRole(String homeId, String userAccessorId, String userId, UserHomeRole role) {
        UserIdWithRole uiwr = new UserIdWithRole();
        uiwr.setUserId(userId);
        uiwr.setRole(role);
        return repoToCtrlResponse(homeRepository.changeUserWithRole(homeId, uiwr));
    }

    public CtrlResponse updateName(String homeId, String userAccessorId, String name) {
        return repoToCtrlResponse(homeRepository.updateName(homeId, name));
    }

    public CtrlResponse updateAddress(String homeId, String userAccessorId, PhysicalAddress address) {
        return repoToCtrlResponse(homeRepository.updateAddress(homeId, address));
    }
}

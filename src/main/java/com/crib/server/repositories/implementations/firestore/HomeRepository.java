package com.crib.server.repositories.implementations.firestore;

import com.crib.server.common.entities.Home;
import com.crib.server.common.patterns.RepoResponse;
import com.crib.server.common.value_objects.PhysicalAddress;
import com.crib.server.common.value_objects.UserIdWithRole;
import com.crib.server.repositories.FirestoreRepository;
import com.crib.server.repositories.interfaces.IHomeRepository;
import com.google.cloud.firestore.FieldValue;

import java.util.List;

public class HomeRepository extends FirestoreRepository<Home> implements IHomeRepository {

    public HomeRepository() {
        super("homes");
    }

    @Override
    protected Class<Home> getDTOClass() {
        return Home.class;
    }

    // Reusable methods
    public RepoResponse changeItemMembershipFromHome(String homeId, boolean add, String field, String itemId) {
        RepoResponse response = new RepoResponse();
        try {
            getCollectionRef()
                    .document(homeId)
                    .update(field, add ? FieldValue.arrayUnion(itemId) : FieldValue.arrayRemove(itemId))
                    .get();

            response.setSuccessful(true);
        }
        catch (Exception e) {
            response.setSuccessful(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    // Inherited methods
    @Override
    public RepoResponse addLockToHome(String homeId, String lockId) {
        return changeItemMembershipFromHome(homeId, true, "lockIds", lockId);
    }

    @Override
    public RepoResponse removeLockFromHome(String homeId, String lockId) {
        return changeItemMembershipFromHome(homeId, false, "lockIds", lockId);
    }

    @Override
    public RepoResponse addUserWithRole(String homeId, UserIdWithRole userIdWithRole) {
        RepoResponse response = new RepoResponse();
        try {
            getCollectionRef()
                    .document(homeId)
                    .update("users", FieldValue.arrayUnion(userIdWithRole))
                    .get();

            response.setSuccessful(true);
        }
        catch (Exception e) {
            response.setSuccessful(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public RepoResponse removeUserWithRole(String homeId, UserIdWithRole userIdWithRole) {
        RepoResponse response = new RepoResponse();
        try {
            getCollectionRef()
                    .document(homeId)
                    .update("users", FieldValue.arrayRemove(userIdWithRole))
                    .get();

            response.setSuccessful(true);
        }
        catch (Exception e) {
            response.setSuccessful(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public RepoResponse changeUserWithRole(String homeId, UserIdWithRole userIdWithRole) {
        RepoResponse response = new RepoResponse();
        try {
            Home home = getCollectionRef()
                    .document(homeId)
                    .get()
                    .get()
                    .toObject(Home.class);

            assert home != null;
            List<UserIdWithRole> userIdWithRoleList = home.getUsers();
            for (UserIdWithRole uiwr : userIdWithRoleList) {
                if (uiwr.getUserId().equals(userIdWithRole.getUserId())) {
                    uiwr.setRole(userIdWithRole.getRole());
                    break;
                }
            }

            getCollectionRef()
                    .document(homeId)
                    .update("users", userIdWithRoleList)
                    .get();

            response.setSuccessful(true);
        }
        catch (Exception e) {
            response.setSuccessful(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public RepoResponse updateName(String homeId, String name) {
        return updateField(homeId, "name", name);
    }

    @Override
    public RepoResponse updateAddress(String homeId, PhysicalAddress address) {
        return updateField(homeId, "address", address);
    }
}

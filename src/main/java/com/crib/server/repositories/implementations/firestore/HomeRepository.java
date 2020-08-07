package com.crib.server.repositories.implementations.firestore;

import com.crib.server.common.entities.Home;
import com.crib.server.common.patterns.RepoResponse;
import com.crib.server.common.value_objects.PhysicalAddress;
import com.crib.server.repositories.FirestoreRepository;
import com.crib.server.repositories.interfaces.IHomeRepository;
import com.google.cloud.firestore.FieldValue;

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
    public RepoResponse addMemberToHome(String homeId, String userId) {
        return changeItemMembershipFromHome(homeId, true, "memberIds", userId);
    }

    @Override
    public RepoResponse removeMemberFromHome(String homeId, String userId) {
        return changeItemMembershipFromHome(homeId, false, "memberIds", userId);
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

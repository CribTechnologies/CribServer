package com.crib.server.repositories.implementations.firestore;

import com.crib.server.common.entities.Lock;
import com.crib.server.common.enums.LockType;
import com.crib.server.common.patterns.RepoResponse;
import com.crib.server.repositories.FirestoreRepository;
import com.crib.server.repositories.interfaces.ILockRepository;

public class LockRepository extends FirestoreRepository<Lock> implements ILockRepository {

    public LockRepository() {
        super("locks");
    }

    @Override
    protected Class<Lock> getDTOClass() {
        return Lock.class;
    }

    @Override
    public RepoResponse updateName(String id, String name) {
        return updateField(id, "name", name);
    }

    @Override
    public RepoResponse updateType(String id, LockType type) {
        return updateField(id, "type", type);
    }
}

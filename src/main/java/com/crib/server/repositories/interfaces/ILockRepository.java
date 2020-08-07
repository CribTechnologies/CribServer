package com.crib.server.repositories.interfaces;

import com.crib.server.common.entities.Lock;
import com.crib.server.common.enums.LockType;
import com.crib.server.common.patterns.RepoResponse;
import com.crib.server.repositories.IRepository;

public interface ILockRepository extends IRepository<Lock> {

    RepoResponse updateName(String id, String name);
    RepoResponse updateType(String id, LockType type);
}

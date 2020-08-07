package com.crib.server.repositories.interfaces;

import com.crib.server.common.entities.Home;
import com.crib.server.common.patterns.RepoResponse;
import com.crib.server.common.value_objects.PhysicalAddress;
import com.crib.server.repositories.IRepository;

public interface IHomeRepository extends IRepository<Home> {

    RepoResponse addLockToHome(String homeId, String lockId);
    RepoResponse removeLockFromHome(String homeId, String lockId);
    RepoResponse addMemberToHome(String homeId, String userId);
    RepoResponse removeMemberFromHome(String homeId, String userId);

    RepoResponse updateName(String homeId, String name);
    RepoResponse updateAddress(String homeId, PhysicalAddress address);
}

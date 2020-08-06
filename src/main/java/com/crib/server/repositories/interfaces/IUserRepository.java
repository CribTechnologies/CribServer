package com.crib.server.repositories.interfaces;

import com.crib.server.common.entities.User;
import com.crib.server.common.enums.Gender;
import com.crib.server.common.patterns.RepositoryResponse;
import com.crib.server.common.patterns.RepositoryResponseWithPayload;
import com.crib.server.repositories.IRepository;

import java.util.Date;

public interface IUserRepository extends IRepository<User> {

    RepositoryResponseWithPayload<User> getUserByEmail(String email);
    RepositoryResponseWithPayload<User> getUserByEmailAndPasswordHash(String email, String passwordHash);

    RepositoryResponse updateFirstName(String id, String firstName);
    RepositoryResponse updateLastName(String id, String lastName);
    RepositoryResponse updateEmail(String id, String email);
    RepositoryResponse updatePasswordHash(String id, String passwordHash);
    RepositoryResponse updateDateOfBirth(String id, Date dateOfBirth);
    RepositoryResponse updatePhoneNumber(String id, String phoneNumber);
    RepositoryResponse updateGender(String id, Gender gender);
}

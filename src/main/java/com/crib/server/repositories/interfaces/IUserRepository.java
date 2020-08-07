package com.crib.server.repositories.interfaces;

import com.crib.server.common.entities.User;
import com.crib.server.common.enums.Gender;
import com.crib.server.common.patterns.RepoResponse;
import com.crib.server.common.patterns.RepoResponseWP;
import com.crib.server.repositories.IRepository;

import java.util.Date;

public interface IUserRepository extends IRepository<User> {

    RepoResponseWP<User> getUserByEmail(String email);
    RepoResponseWP<User> getUserByEmailAndPasswordHash(String email, String passwordHash);

    RepoResponse updateFirstAndLastName(String id, String firstName, String lastName);
    RepoResponse updateEmailAndVerified(String id, String email, boolean verified);
    RepoResponse updatePasswordHash(String id, String passwordHash);
    RepoResponse updateDateOfBirth(String id, Date dateOfBirth);
    RepoResponse updatePhoneNumberAndVerified(String id, String phoneNumber, boolean verified);
    RepoResponse updateGender(String id, Gender gender);
}

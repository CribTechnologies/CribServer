package com.crib.server.services;

import com.crib.server.Argon2Setup;
import com.crib.server.common.entities.User;
import com.crib.server.common.enums.ControllerResponseStatus;
import com.crib.server.common.enums.Gender;
import com.crib.server.common.patterns.CtrlResponse;
import com.crib.server.common.patterns.RepoResponseWP;
import com.crib.server.repositories.RepositoryFactory;
import com.crib.server.repositories.interfaces.IUserRepository;

import java.util.Date;

public class UserService extends Service {

    private IUserRepository userRepository;
    private Argon2Setup argon2;

    public UserService() {
        RepositoryFactory repositoryFactory = RepositoryFactory.getInstance();
        userRepository = repositoryFactory.getUserRepository();
        argon2 = Argon2Setup.getInstance();
    }

    public CtrlResponse updateFirstAndLastName(String userId, String firstName, String lastName) {
        return repoToCtrlResponse(userRepository.updateFirstAndLastName(userId, firstName, lastName));
    }

    public CtrlResponse updateDateOfBirth(String userId, Date dateOfBirth) {
        return repoToCtrlResponse(userRepository.updateDateOfBirth(userId, dateOfBirth));
    }

    public CtrlResponse updateGender(String userId, Gender gender) {
        return repoToCtrlResponse(userRepository.updateGender(userId, gender));
    }

    public CtrlResponse updatePassword(String userId, String oldPassword, String newPassword) {
        RepoResponseWP<User> repoResponseWP = userRepository.getById(userId);
        if (argon2.rawAndHashAreEqual(oldPassword, repoResponseWP.getPayload().getPasswordHash())) {
            String newPasswordHash = argon2.createHash(newPassword);
            return repoToCtrlResponse(userRepository.updatePasswordHash(userId, newPasswordHash));
        }
        return unauthenticatedResponse("The old password is incorrect");
    }

    public CtrlResponse updateEmail(String userId, String password, String email) {
        RepoResponseWP<User> repoResponseWP = userRepository.getById(userId);
        if (argon2.rawAndHashAreEqual(password, repoResponseWP.getPayload().getPasswordHash())) {
            // TODO send verification link to user
            return repoToCtrlResponse(userRepository.updateEmailAndVerified(userId, email, false));
        }
        return unauthenticatedResponse("The password is incorrect");
    }

    public CtrlResponse updatePhoneNumber(String userId, String password, String phoneNumber) {
        RepoResponseWP<User> repoResponseWP = userRepository.getById(userId);
        if (argon2.rawAndHashAreEqual(password, repoResponseWP.getPayload().getPasswordHash())) {
            // TODO send verification link to user
            return repoToCtrlResponse(userRepository.updatePhoneNumberAndVerified(userId, phoneNumber, false));
        }
        return unauthenticatedResponse("The password is incorrect");
    }
}

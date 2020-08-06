package com.crib.server.repositories.implementations.firestore;

import com.crib.server.common.entities.User;
import com.crib.server.common.enums.Gender;
import com.crib.server.common.patterns.RepositoryResponse;
import com.crib.server.common.patterns.RepositoryResponseWithPayload;
import com.crib.server.repositories.FirestoreRepository;
import com.crib.server.repositories.interfaces.IUserRepository;

import java.util.Date;

public class UserRepository extends FirestoreRepository<User> implements IUserRepository {

    public UserRepository() {
        super("users");
    }

    @Override
    protected Class<User> getDTOClass() {
        return User.class;
    }

    @Override
    public RepositoryResponseWithPayload<User> getUserByEmail(String email) {
        RepositoryResponseWithPayload<User> response = new RepositoryResponseWithPayload<>();
        try {
            User user = getCollectionRef()
                    .whereEqualTo("email", email)
                    .get()
                    .get()
                    .getDocuments()
                    .get(0)
                    .toObject(getDTOClass());

            response.setSuccessful(true);
            response.setPayload(user);
        }
        catch (Exception e) {
            response.setSuccessful(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public RepositoryResponseWithPayload<User> getUserByEmailAndPasswordHash(String email, String passwordHash) {
        RepositoryResponseWithPayload<User> response = new RepositoryResponseWithPayload<>();
        try {
            User user = getCollectionRef()
                    .whereEqualTo("email", email)
                    .whereEqualTo("passwordHash", passwordHash)
                    .get()
                    .get()
                    .getDocuments()
                    .get(0)
                    .toObject(getDTOClass());

            response.setSuccessful(true);
            response.setPayload(user);
        }
        catch (Exception e) {
            response.setSuccessful(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public RepositoryResponse updateFirstName(String id, String firstName) {
        return updateField(id, "firstName", firstName);
    }

    @Override
    public RepositoryResponse updateLastName(String id, String lastName) {
        return updateField(id, "lastName", lastName);
    }

    @Override
    public RepositoryResponse updateEmail(String id, String email) {
        return updateField(id, "email", email);
    }

    @Override
    public RepositoryResponse updatePasswordHash(String id, String passwordHash) {
        return updateField(id, "passwordHash", passwordHash);
    }

    @Override
    public RepositoryResponse updateDateOfBirth(String id, Date dateOfBirth) {
        return updateField(id, "dateOfBirth", dateOfBirth);
    }

    @Override
    public RepositoryResponse updatePhoneNumber(String id, String phoneNumber) {
        return updateField(id, "phoneNumber", phoneNumber);
    }

    @Override
    public RepositoryResponse updateGender(String id, Gender gender) {
        return updateField(id, "gender", gender);
    }
}

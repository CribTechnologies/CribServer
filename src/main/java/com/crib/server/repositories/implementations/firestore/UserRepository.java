package com.crib.server.repositories.implementations.firestore;

import com.crib.server.common.entities.User;
import com.crib.server.common.enums.Gender;
import com.crib.server.common.patterns.RepoResponse;
import com.crib.server.common.patterns.RepoResponseWP;
import com.crib.server.repositories.FirestoreRepository;
import com.crib.server.repositories.interfaces.IUserRepository;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository extends FirestoreRepository<User> implements IUserRepository {

    public UserRepository() {
        super("users");
    }

    @Override
    protected Class<User> getDTOClass() {
        return User.class;
    }

    @Override
    public RepoResponseWP<User> getUserByEmail(String email) {
        RepoResponseWP<User> response = new RepoResponseWP<>();
        try {
            List<QueryDocumentSnapshot> user = getCollectionRef()
                    .whereEqualTo("email", email)
                    .get()
                    .get()
                    .getDocuments();

            if (!user.isEmpty()) {
                response.setPayload(user
                        .get(0)
                        .toObject(getDTOClass()));
            }
            response.setSuccessful(true);
        }
        catch (Exception e) {
            response.setSuccessful(false);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public RepoResponse setEmailVerifiedToTrue(String userId) {
        return updateField(userId, "emailVerified", true);
    }

    @Override
    public RepoResponse updateDetails(String id, String firstName, String lastName, Date dateOfBirth, Gender gender) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("firstName", firstName);
        fields.put("lastName", lastName);
        fields.put("dateOfBirth", dateOfBirth);
        fields.put("gender", gender);
        return updateFields(id, fields);
    }

    @Override
    public RepoResponse updateEmailAndVerified(String id, String email, boolean verified) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("email", email);
        fields.put("emailVerified", verified);
        return updateFields(id, fields);
    }

    @Override
    public RepoResponse updatePasswordHash(String id, String passwordHash) {
        return updateField(id, "passwordHash", passwordHash);
    }

    @Override
    public RepoResponse updatePhoneNumberAndVerified(String id, String phoneNumber, boolean verified) {
        Map<String, Object> fields = new HashMap<>();
        fields.put("phoneNumber", phoneNumber);
        fields.put("phoneNumberVerified", verified);
        return updateFields(id, fields);
    }
}

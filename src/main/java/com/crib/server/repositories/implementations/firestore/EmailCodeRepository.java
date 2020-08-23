package com.crib.server.repositories.implementations.firestore;

import com.crib.server.common.entities.EmailCode;
import com.crib.server.common.entities.User;
import com.crib.server.common.patterns.RepoResponseWP;
import com.crib.server.repositories.FirestoreRepository;
import com.crib.server.repositories.interfaces.IEmailCodeRepository;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import java.util.List;

public class EmailCodeRepository extends FirestoreRepository<EmailCode> implements IEmailCodeRepository {

    public EmailCodeRepository() {
        super("email-verification-codes");
    }

    @Override
    protected Class<EmailCode> getDTOClass() {
        return EmailCode.class;
    }

    @Override
    public RepoResponseWP<EmailCode> getEmailCodeByCode(String code) {
        RepoResponseWP<EmailCode> response = new RepoResponseWP<>();
        try {
            List<QueryDocumentSnapshot> emailCode = getCollectionRef()
                    .whereEqualTo("code", code)
                    .get()
                    .get()
                    .getDocuments();

            if (!emailCode.isEmpty()) {
                response.setPayload(emailCode
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
}

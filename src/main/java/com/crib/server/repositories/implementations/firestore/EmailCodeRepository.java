package com.crib.server.repositories.implementations.firestore;

import com.crib.server.common.entities.EmailCode;
import com.crib.server.repositories.FirestoreRepository;
import com.crib.server.repositories.interfaces.IEmailCodeRepository;

public class EmailCodeRepository extends FirestoreRepository<EmailCode> implements IEmailCodeRepository {

    public EmailCodeRepository() {
        super("email-verification-codes");
    }

    @Override
    protected Class<EmailCode> getDTOClass() {
        return EmailCode.class;
    }
}

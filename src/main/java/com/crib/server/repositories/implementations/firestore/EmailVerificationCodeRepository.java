package com.crib.server.repositories.implementations.firestore;

import com.crib.server.common.entities.EmailVerificationCode;
import com.crib.server.repositories.FirestoreRepository;
import com.crib.server.repositories.interfaces.IEmailVerificationCodeRepository;

public class EmailVerificationCodeRepository extends FirestoreRepository<EmailVerificationCode> implements IEmailVerificationCodeRepository {

    public EmailVerificationCodeRepository() {
        super("email-verification-codes");
    }

    @Override
    protected Class<EmailVerificationCode> getDTOClass() {
        return EmailVerificationCode.class;
    }
}

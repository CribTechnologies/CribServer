package com.crib.server.repositories.interfaces;

import com.crib.server.common.entities.EmailCode;
import com.crib.server.common.patterns.RepoResponseWP;
import com.crib.server.repositories.IRepository;

public interface IEmailCodeRepository extends IRepository<EmailCode> {

    RepoResponseWP<EmailCode> getEmailCodeByCode(String code);
}

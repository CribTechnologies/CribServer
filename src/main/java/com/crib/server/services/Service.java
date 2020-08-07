package com.crib.server.services;

import com.crib.server.common.entities.User;
import com.crib.server.common.enums.ControllerResponseStatus;
import com.crib.server.common.patterns.CtrlResponse;
import com.crib.server.common.patterns.CtrlResponseWP;
import com.crib.server.common.patterns.RepoResponse;
import com.crib.server.common.patterns.RepoResponseWP;

public abstract class Service {

    protected CtrlResponse repoToCtrlResponse(RepoResponse repoResponse) {
        CtrlResponse response = new CtrlResponse();

        response.setStatus(repoResponse.isSuccessful()
                ? ControllerResponseStatus.SUCCESS
                : ControllerResponseStatus.REPOSITORY_ERROR);
        response.addMessage(repoResponse.getMessage());
        return response;
    }

    protected <T1> CtrlResponseWP<T1> repoToCtrlResponseWithPayload(RepoResponseWP<T1> repoResponse) {
        CtrlResponseWP<T1> response = new CtrlResponseWP<>();

        response.setStatus(repoResponse.isSuccessful()
                ? ControllerResponseStatus.SUCCESS
                : ControllerResponseStatus.REPOSITORY_ERROR);
        response.addMessage(repoResponse.getMessage());
        response.setPayload(repoResponse.getPayload());
        return response;
    }

    protected <T1, T2> CtrlResponseWP<T2> repoToCtrlResponseWithCustomPayload(RepoResponseWP<T1> repoResponse, T2 payload) {
        CtrlResponseWP<T2> response = new CtrlResponseWP<>();

        response.setStatus(repoResponse.isSuccessful()
                ? ControllerResponseStatus.SUCCESS
                : ControllerResponseStatus.REPOSITORY_ERROR);
        response.addMessage(repoResponse.getMessage());
        response.setPayload(payload);
        return response;
    }

    protected CtrlResponse unauthenticatedResponse(String message) {
        CtrlResponse ctrlResponse = new CtrlResponse();
        ctrlResponse.setStatus(ControllerResponseStatus.VALIDATION_ERROR);
        ctrlResponse.addMessage(message);
        return ctrlResponse;
    }
}

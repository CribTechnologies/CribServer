package com.crib.server.common.ctrl_requests;

import com.crib.server.common.patterns.CtrlRequest;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateHomeRequest extends CtrlRequest {

    @NotNull
    @Size(min = 1, max = 128)
    private String userCreatorId;

    @NotNull
    @Size(min = 1, max = 128)
    private String homeName;

    @NotNull
    @Size(min = 1, max = 128)
    private String addressPrimaryStreet;

    @Max(value = 128)
    private String addressSecondaryStreet;

    @NotNull
    @Size(min = 1, max = 128)
    private String addressCity;

    @NotNull
    @Size(min = 1, max = 128)
    private String addressState;

    @NotNull
    @Size(min = 1, max = 128)
    private String addressCountry;

    @NotNull
    @Size(min = 1, max = 128)
    private String addressZipCode;

    public CreateHomeRequest() {
    }

    public String getUserCreatorId() {
        return userCreatorId;
    }

    public void setUserCreatorId(String userCreatorId) {
        this.userCreatorId = userCreatorId;
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public String getAddressPrimaryStreet() {
        return addressPrimaryStreet;
    }

    public void setAddressPrimaryStreet(String addressPrimaryStreet) {
        this.addressPrimaryStreet = addressPrimaryStreet;
    }

    public String getAddressSecondaryStreet() {
        return addressSecondaryStreet;
    }

    public void setAddressSecondaryStreet(String addressSecondaryStreet) {
        this.addressSecondaryStreet = addressSecondaryStreet;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getAddressState() {
        return addressState;
    }

    public void setAddressState(String addressState) {
        this.addressState = addressState;
    }

    public String getAddressCountry() {
        return addressCountry;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    public String getAddressZipCode() {
        return addressZipCode;
    }

    public void setAddressZipCode(String addressZipCode) {
        this.addressZipCode = addressZipCode;
    }
}

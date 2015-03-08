package com.klarna.ondemand;

import java.io.Serializable;

/**
 * Defines the settings for Klarna's Registration Activity
 */
public class RegistrationSettings implements Serializable {
    private String confirmedUserDataId;
    private String phoneNumber;

    RegistrationSettings(RegistrationSettingsBuilder builder) {
        this.confirmedUserDataId = builder.confirmedUserDataId;
        this.phoneNumber = builder.phoneNumber;
    }

    public String getConfirmedUserDataId() {
        return confirmedUserDataId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}

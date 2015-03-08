package com.klarna.ondemand;

import java.io.Serializable;

/**
 * Defines the settings for Klarna's Registration Activity
 */
public class RegistrationSettings implements Serializable {
    private String confirmedUserDataId;
    private String prefillPhoneNumber;

    RegistrationSettings(RegistrationSettingsBuilder builder) {
        this.confirmedUserDataId = builder.confirmedUserDataId;
        this.prefillPhoneNumber = builder.prefillPhoneNumber;
    }

    public String getConfirmedUserDataId() {
        return confirmedUserDataId;
    }

    public String getPrefillPhoneNumber() {
        return prefillPhoneNumber;
    }
}

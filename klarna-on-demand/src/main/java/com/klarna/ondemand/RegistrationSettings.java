package com.klarna.ondemand;

import java.io.Serializable;

/**
 * Defines the settings for Klarna's Registration Activity
 */
public class RegistrationSettings implements Serializable {
    String confirmedUserDataId;
    String prefillPhoneNumber;

    RegistrationSettings(String confirmedUserDataId, String prefillPhoneNumber) {
        this.confirmedUserDataId = confirmedUserDataId;
        this.prefillPhoneNumber = prefillPhoneNumber;
    }

    /**
     * @return Confirmed user data id
     */
    public String getConfirmedUserDataId() {
        return confirmedUserDataId;
    }

    /**
     * @return Pre-fill phone number
     */
    public String getPrefillPhoneNumber() {
        return prefillPhoneNumber;
    }
}

package com.klarna.ondemand;

import java.io.Serializable;

/**
 * Defines the settings for Klarna's Registration Activity
 */
public class RegistrationSettings implements Serializable {
    String confirmedUserDataId;
    String prefillPhoneNumber;

    public RegistrationSettings(String prefillPhoneNumber, String confirmedUserDataId) {
        this.prefillPhoneNumber = prefillPhoneNumber;
        this.confirmedUserDataId = confirmedUserDataId;
    }

    RegistrationSettings() {
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

    void setPrefillPhoneNumberIfBlank(String phoneNumber) {
        if(prefillPhoneNumber == null || prefillPhoneNumber.isEmpty()) {
            prefillPhoneNumber = phoneNumber;
        }
    }
}

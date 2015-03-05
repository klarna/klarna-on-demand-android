package com.klarna.ondemand;

import java.io.Serializable;

public class RegistrationSettings implements Serializable {
    private String confirmedUserDataId;

    RegistrationSettings(RegistrationSettingsBuilder builder) {
        this.confirmedUserDataId = builder.confirmedUserDataId;
    }

    public String getConfirmedUserDataId() {
        return confirmedUserDataId;
    }
}

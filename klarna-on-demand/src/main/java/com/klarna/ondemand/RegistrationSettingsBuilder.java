package com.klarna.ondemand;

public class RegistrationSettingsBuilder {
    
    String confirmedUserDataId;
    
    public RegistrationSettingsBuilder setConfirmedUserDataId(String confirmedUserDataId) {
        this.confirmedUserDataId = confirmedUserDataId; 
        
        return this;
    }
    
    public RegistrationSettings build() {
        return new RegistrationSettings(this);
    }
}

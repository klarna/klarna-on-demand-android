package com.klarna.ondemand;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 *  The user's Klarna registration result, which includes the token used for making purchaces.
 */
public class RegistrationResult implements Serializable{
    private String token;
    private String phoneNumber;
    private HashMap<Object, Object> userDetails;

    public RegistrationResult(String token, String phoneNumber, Map<?, ?> userDetails) {
        this.token = token;
        this.phoneNumber = phoneNumber;
        this.userDetails = new HashMap<>(userDetails);
    }

    /**
     * @return the user's token, which is used for making orders.
     */
    public String getToken() {
        return token;
    }

    /**
     * @return the user's validated phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @return The user's registration details.
     */
    public Map<Object, Object> getUserDetails() {
        return userDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegistrationResult that = (RegistrationResult) o;

        if (phoneNumber != null ? !phoneNumber.equals(that.phoneNumber) : that.phoneNumber != null)
            return false;
        if (token != null ? !token.equals(that.token) : that.token != null) return false;
        if (userDetails != null ? !userDetails.equals(that.userDetails) : that.userDetails != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = token != null ? token.hashCode() : 0;
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (userDetails != null ? userDetails.hashCode() : 0);
        return result;
    }
}

package com.klarna.ondemand;

import android.content.Intent;

import java.util.Map;

/**
 * Responsible for registering a new user and setting his Klarna payment method.
 */
public class RegistrationActivity extends WebViewActivity {

    public static final String EXTRA_USER_TOKEN = "userToken";

    @Override
    protected String url() {
        return UrlHelper.registrationUrl();
    }

    @Override
    protected void handleUserReadyEventWithPayload(Map<Object, Object> payload) {
        String token = (String) payload.get("userToken");
        Intent result = new Intent();
        result.putExtra(EXTRA_USER_TOKEN, token);
        setResult(RESULT_OK, result);
        finish();
    }
}

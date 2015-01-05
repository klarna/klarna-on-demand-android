package com.klarna.ondemand;

import java.util.Map;

/**
 * Responsible for registering a new user and setting his Klarna payment method.
 */
public class RegistrationActivity extends WebViewActivity {

    @Override
    protected String url() {
        return UrlHelper.registrationUrl();
    }

    @Override
    protected void handleUserReadyEventWithPayload(Map<Object, Object> payload) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void handleUserErrorEvent() {
        setResult(RESULT_ERROR);
        finish();
    }
}

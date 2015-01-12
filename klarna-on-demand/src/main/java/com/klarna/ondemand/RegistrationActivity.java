package com.klarna.ondemand;

/**
 * Responsible for registering a new user and setting his Klarna payment method.
 */
public class RegistrationActivity extends WebViewActivity {

    @Override
    protected String getUrl() {
        return UrlHelper.registrationUrl(getApplicationContext());
    }
}

package com.klarna.ondemand;

import junit.framework.TestCase;

public class KODUrlTest  extends TestCase {

    String token = "my_token";

    protected void setUp() throws java.lang.Exception {
        Context.setApiKey("test_skadoo");
    }

    public void testRegistrationUrlShouldReturnPlaygroundUrlWhenTokenIsForPlayground() {
        assertTrue(KODUrl.registrationUrl().startsWith("https://inapp.playground.klarna.com/registration/new"));
    }

    public void testRegistrationUrlShouldReturnProductionUrlWhenTokenIsForProduction() {
        Context.setApiKey("skadoo");
        assertTrue(KODUrl.registrationUrl().startsWith("https://inapp.klarna.com/registration/new"));
    }

    public void testPreferencesUrlShouldReturnPlaygroundUrlWhenTokenIsForPlayground() {
        String expectedPrefix = String.format("%s%s%s", "https://inapp.playground.klarna.com/users/", token, "/preferences");
        assertTrue(KODUrl.preferencesUrl(token).startsWith(expectedPrefix));
    }

    public void testPreferencesUrlShouldReturnProductionUrlWhenTokenIsForProduction() {
        Context.setApiKey("skadoo");
        String expectedPrefix = String.format("%s%s%s", "https://inapp.klarna.com/users/", token, "/preferences");
        assertTrue(KODUrl.preferencesUrl(token).startsWith(expectedPrefix));
    }
}

package com.klarna.ondemand;

import junit.framework.TestCase;

import java.util.Locale;

public class UrlHelperTest extends TestCase {

    String token = "my_token";

    public void setUp() throws java.lang.Exception {
        Context.setApiKey("test_skadoo");
    }

    //region .registrationUrl
    public void testRegistrationUrl_ShouldReturnPlaygroundUrl_WhenTokenIsForPlayground() {
        assertTrue(UrlHelper.registrationUrl().startsWith("https://inapp.playground.klarna.com/registration/new"));
    }

    public void testRegistrationUrl_ShouldReturnProductionUrl_WhenTokenIsForProduction() {
        Context.setApiKey("skadoo");
        assertTrue(UrlHelper.registrationUrl().startsWith("https://inapp.klarna.com/registration/new"));
    }

    public void testRegistrationUrl_ShouldReturnUrlWithSwedishLocale_WhenLocaleIsSwedish() {
        Locale.setDefault(new Locale("sv"));

        assertTrue(UrlHelper.registrationUrl().contains("locale=sv"));
    }
    //endregion

    //region .PreferencesUrlWithToken
    public void testPreferencesUrlWithToken_ShouldReturnPlaygroundUrl_WhenTokenIsForPlayground() {
        String expectedPrefix = String.format("%s%s%s", "https://inapp.playground.klarna.com/users/", token, "/preferences");
        assertTrue(UrlHelper.preferencesUrl(token).startsWith(expectedPrefix));
    }

    public void testPreferencesUrlWithToken_ShouldReturnProductionUrl_WhenTokenIsForProduction() {
        Context.setApiKey("skadoo");
        String expectedPrefix = String.format("%s%s%s", "https://inapp.klarna.com/users/", token, "/preferences");
        assertTrue(UrlHelper.preferencesUrl(token).startsWith(expectedPrefix));
    }

    public void testPreferencesUrlWithToken_ShouldReturnUrlWithSwedishLocale_WhenLocaleIsSwedish() {
        Locale.setDefault(new Locale("sv"));

        assertTrue(UrlHelper.preferencesUrl(token).contains("locale=sv"));
    }
    //endregion
}
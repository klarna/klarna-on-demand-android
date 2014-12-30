package com.klarna.ondemand;

import junit.framework.TestCase;
import java.util.Locale;

public class KODUrlTest  extends TestCase {

    String token = "my_token";

    protected void setUp() throws java.lang.Exception {
        Context.setApiKey("test_skadoo");
    }

    //region .registrationUrl
    public void testRegistrationUrl_ShouldReturnPlaygroundUrl_WhenTokenIsForPlayground() {
        assertTrue(KODUrl.registrationUrl().startsWith("https://inapp.playground.klarna.com/registration/new"));
    }

    public void testRegistrationUrl_ShouldReturnProductionUrl_WhenTokenIsForProduction() {
        Context.setApiKey("skadoo");
        assertTrue(KODUrl.registrationUrl().startsWith("https://inapp.klarna.com/registration/new"));
    }

    public void testRegistrationUrl_ShouldReturnUrlWithSwedishLocale_WhenLocaleIsSwedish() {
        //PowerMockito.when(Locale.getDefault().getLanguage()).thenReturn("se");
        //assertTrue(KODUrl.registrationUrl().contains("locale=sv"));

    }
    //endregion

    //region .PreferencesUrlWithToken
    public void testPreferencesUrl_ShouldReturnPlaygroundUrl_WhenTokenIsForPlayground() {
        String expectedPrefix = String.format("%s%s%s", "https://inapp.playground.klarna.com/users/", token, "/preferences");
        assertTrue(KODUrl.preferencesUrl(token).startsWith(expectedPrefix));
    }

    public void testPreferencesUrl_ShouldReturnProductionUrl_WhenTokenIsForProduction() {
        Context.setApiKey("skadoo");
        String expectedPrefix = String.format("%s%s%s", "https://inapp.klarna.com/users/", token, "/preferences");
        assertTrue(KODUrl.preferencesUrl(token).startsWith(expectedPrefix));
    }
    //endregion
}

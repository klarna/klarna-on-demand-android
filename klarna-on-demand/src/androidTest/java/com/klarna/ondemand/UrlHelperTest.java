package com.klarna.ondemand;


import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

public class UrlHelperTest extends TestCase {

    String token = "my_token";

    @Before
    public void setUp() throws java.lang.Exception {
        Context.setApiKey("test_skadoo");
    }

    //region .registrationUrl
    @Test
    public void testRegistrationUrl_ShouldReturnPlaygroundUrl_WhenTokenIsForPlayground() {
        Assert.assertTrue(UrlHelper.registrationUrl().startsWith("https://inapp.playground.klarna.com/registration/new"));
    }

    @Test
    public void testRegistrationUrl_ShouldReturnProductionUrl_WhenTokenIsForProduction() {
        Context.setApiKey("skadoo");
        Assert.assertTrue(UrlHelper.registrationUrl().startsWith("https://inapp.klarna.com/registration/new"));
    }

    @Test
    public void testRegistrationUrl_ShouldReturnUrlWithFrenchLocale_WhenLocaleIsFrench() {
        Locale.setDefault(Locale.FRENCH);

        Assert.assertTrue(UrlHelper.registrationUrl().contains("locale=fr"));
    }
    //endregion

    //region .PreferencesUrlWithToken
    @Test
    public void testPreferencesUrlWithToken_ShouldReturnPlaygroundUrl_WhenTokenIsForPlayground() {
        String expectedPrefix = String.format("%s%s%s", "https://inapp.playground.klarna.com/users/", token, "/preferences");
        Assert.assertTrue(UrlHelper.preferencesUrl(token).startsWith(expectedPrefix));
    }

    @Test
    public void testPreferencesUrlWithToken_ShouldReturnProductionUrl_WhenTokenIsForProduction() {
        Context.setApiKey("skadoo");
        String expectedPrefix = String.format("%s%s%s", "https://inapp.klarna.com/users/", token, "/preferences");
        Assert.assertTrue(UrlHelper.preferencesUrl(token).startsWith(expectedPrefix));
    }

    @Test
    public void testPreferencesUrlWithToken_ShouldReturnUrlWithFrenchLocale_WhenLocaleIsFrench() {
        Locale.setDefault(Locale.FRENCH);

        Assert.assertTrue(UrlHelper.preferencesUrl(token).contains("locale=fr"));
    }
    //endregion
}

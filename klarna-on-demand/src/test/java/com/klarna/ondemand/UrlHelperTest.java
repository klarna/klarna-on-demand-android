package com.klarna.ondemand;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.robolectric.annotation.Config;

import java.util.Locale;

@RunWith(PowerMockRunner.class)
@Config(emulateSdk = 18)
public class UrlHelperTest {

    String token = "my_token";

    @Before
    public void setUp() throws java.lang.Exception {
        Context.setApiKey("test_skadoo");
    }

    //region .registrationUrl
    @Test
    public void registrationUrl_ShouldReturnPlaygroundUrl_WhenTokenIsForPlayground() {
        Assert.assertTrue(UrlHelper.registrationUrl().startsWith("https://inapp.playground.klarna.com/registration/new"));
    }

    @Test
    public void registrationUrl_ShouldReturnProductionUrl_WhenTokenIsForProduction() {
        Context.setApiKey("skadoo");
        Assert.assertTrue(UrlHelper.registrationUrl().startsWith("https://inapp.klarna.com/registration/new"));
    }

    @Test
    public void registrationUrl_ShouldReturnUrlWithSwedishLocale_WhenLocaleIsSwedish() {
        Locale.setDefault(new Locale("sv"));

        Assert.assertTrue(UrlHelper.registrationUrl().contains("locale=sv"));
    }
    //endregion

    //region .PreferencesUrlWithToken
    @Test
    public void preferencesUrlWithToken_ShouldReturnPlaygroundUrl_WhenTokenIsForPlayground() {
        String expectedPrefix = String.format("%s%s%s", "https://inapp.playground.klarna.com/users/", token, "/preferences");
        Assert.assertTrue(UrlHelper.preferencesUrl(token).startsWith(expectedPrefix));
    }

    @Test
    public void preferencesUrlWithToken_ShouldReturnProductionUrl_WhenTokenIsForProduction() {
        Context.setApiKey("skadoo");
        String expectedPrefix = String.format("%s%s%s", "https://inapp.klarna.com/users/", token, "/preferences");
        Assert.assertTrue(UrlHelper.preferencesUrl(token).startsWith(expectedPrefix));
    }

    @Test
    public void preferencesUrlWithToken_ShouldReturnUrlWithSwedishLocale_WhenLocaleIsSwedish() {
        Locale.setDefault(new Locale("sv"));

        Assert.assertTrue(UrlHelper.preferencesUrl(token).contains("locale=sv"));
    }
    //endregion
}
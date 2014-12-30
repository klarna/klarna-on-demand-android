package com.klarna.ondemand;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Locale;


@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class KODUrlTest {

    String token = "my_token";

    @Before
    public void setUp() throws java.lang.Exception {
        Context.setApiKey("test_skadoo");
    }

    //region .registrationUrl
    @Test
    public void testRegistrationUrl_ShouldReturnPlaygroundUrl_WhenTokenIsForPlayground() {
        Assert.assertTrue(KODUrl.registrationUrl().startsWith("https://inapp.playground.klarna.com/registration/new"));
    }

    @Test
    public void testRegistrationUrl_ShouldReturnProductionUrl_WhenTokenIsForProduction() {
        Context.setApiKey("skadoo");
        Assert.assertTrue(KODUrl.registrationUrl().startsWith("https://inapp.klarna.com/registration/new"));
    }

    @Test
    public void testRegistrationUrl_ShouldReturnUrlWithSwedishLocale_WhenLocaleIsSwedish() {
        //PowerMockito.when(Locale.getDefault().getLanguage()).thenReturn("se");
        //assertTrue(KODUrl.registrationUrl().contains("locale=sv"));

    }
    //endregion

    //region .PreferencesUrlWithToken
    @Test
    public void testPreferencesUrl_ShouldReturnPlaygroundUrl_WhenTokenIsForPlayground() {
        String expectedPrefix = String.format("%s%s%s", "https://inapp.playground.klarna.com/users/", token, "/preferences");
        Assert.assertTrue(KODUrl.preferencesUrl(token).startsWith(expectedPrefix));
    }

    @Test
    public void testPreferencesUrl_ShouldReturnProductionUrl_WhenTokenIsForProduction() {
        Context.setApiKey("skadoo");
        String expectedPrefix = String.format("%s%s%s", "https://inapp.klarna.com/users/", token, "/preferences");
        Assert.assertTrue(KODUrl.preferencesUrl(token).startsWith(expectedPrefix));
    }
    //endregion
}

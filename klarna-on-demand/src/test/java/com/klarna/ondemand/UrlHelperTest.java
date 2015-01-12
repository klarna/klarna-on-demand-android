package com.klarna.ondemand;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import java.util.Locale;

public class UrlHelperTest {

    String token = "my_token";

    @Before
    public void beforeEach() {
        Context.setApiKey("test_skadoo");
    }

    @After
    public void afterEach() {
        Context.setApiKey(null);
    }

    //region .registrationUrl
    @Test
    public void registrationUrl_ShouldReturnPlaygroundUrl_WhenTokenIsForPlayground() {
        android.content.Context context = Mockito.mock(android.content.Context.class);
        Assert.assertTrue(UrlHelper.registrationUrl(context).startsWith("https://inapp.playground.klarna.com/registration/new"));
    }

    @Test
    public void registrationUrl_ShouldReturnProductionUrl_WhenTokenIsForProduction() {
        Context.setApiKey("skadoo");

        android.content.Context context = Mockito.mock(android.content.Context.class);

        Assert.assertTrue(UrlHelper.registrationUrl(context).startsWith("https://inapp.klarna.com/registration/new"));
    }

    @Test
    public void registrationUrl_ShouldReturnUrlWithSwedishLocale_WhenLocaleIsSwedish() {
        Locale.setDefault(new Locale("sv"));

        android.content.Context context = Mockito.mock(android.content.Context.class);
        Assert.assertTrue(UrlHelper.registrationUrl(context).contains("locale=sv"));
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
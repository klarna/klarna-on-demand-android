package com.klarna.ondemand;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Locale;


@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
@PrepareForTest(Context.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
public class UrlHelperTest {

    private String token = "my_token";

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Before
    public void beforeEach() {
        PowerMockito.mockStatic(Context.class);
        Mockito.when(Context.getApiKey()).thenReturn("test_skadoo");
    }

    //region .registrationUrl
    @Test
    public void registrationUrl_ShouldReturnPlaygroundUrl_WhenTokenIsForPlayground() {
        Assert.assertTrue(UrlHelper.registrationUrl().startsWith("https://inapp.playground.klarna.com/registration/new"));
    }

    @Test
    public void registrationUrl_ShouldReturnProductionUrl_WhenTokenIsForProduction() {
        PowerMockito.mockStatic(Context.class);
        Mockito.when(Context.getApiKey()).thenReturn("skadoo");

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
        PowerMockito.mockStatic(Context.class);
        Mockito.when(Context.getApiKey()).thenReturn("skadoo");

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
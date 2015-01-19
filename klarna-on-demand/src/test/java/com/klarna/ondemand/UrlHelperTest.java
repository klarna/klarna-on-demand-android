package com.klarna.ondemand;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Locale;

import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
@PrepareForTest({Context.class, Locale.class})
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
public class UrlHelperTest {

    private String token = "my_token";

    @Rule
    public final PowerMockRule rule = new PowerMockRule();

    @Before
    public void beforeEach() {
        mockStatic(Context.class);
        when(Context.getApiKey()).thenReturn("test_skadoo");
    }

    //region .registrationUrl
    @Test
    public void registrationUrl_ShouldReturnPlaygroundUrl_WhenTokenIsForPlayground() {
        android.content.Context context = Robolectric.application.getApplicationContext();

        Assert.assertTrue(UrlHelper.registrationUrl(context).startsWith("https://inapp.playground.klarna.com/registration/new"));
    }

    @Test
    public void registrationUrl_ShouldReturnProductionUrl_WhenTokenIsForProduction() {
        when(Context.getApiKey()).thenReturn("skadoo");
        android.content.Context context = Robolectric.application.getApplicationContext();

        Assert.assertTrue(UrlHelper.registrationUrl(context).startsWith("https://inapp.klarna.com/registration/new"));
    }

    @Test
    public void registrationUrl_ShouldReturnUrlWithTheDefaultLocale() {
        Locale.setDefault(new Locale("my_locale"));
        android.content.Context context = Robolectric.application.getApplicationContext();

        Assert.assertTrue(UrlHelper.registrationUrl(context).contains("locale=my_locale"));
    }
    //endregion

    //region .PreferencesUrlWithToken
    @Test
    public void preferencesUrlWithToken_ShouldReturnPlaygroundUrl_WhenTokenIsForPlayground() {
        String expectedPrefix = "https://inapp.playground.klarna.com/users/" + token + "/preferences";
        Assert.assertTrue(UrlHelper.preferencesUrl(token).startsWith(expectedPrefix));
    }

    @Test
    public void preferencesUrlWithToken_ShouldReturnProductionUrl_WhenTokenIsForProduction() {
        mockStatic(Context.class);
        when(Context.getApiKey()).thenReturn("skadoo");

        String expectedPrefix = "https://inapp.klarna.com/users/" + token +"/preferences";
        Assert.assertTrue(UrlHelper.preferencesUrl(token).startsWith(expectedPrefix));
    }

    @Test
    public void preferencesUrlWithToken_ShouldReturnUrlWithTheDefaultLocale() {
        Locale.setDefault(new Locale("my_locale"));

        Assert.assertTrue(UrlHelper.preferencesUrl(token).contains("locale=my_locale"));
    }
    //endregion
}
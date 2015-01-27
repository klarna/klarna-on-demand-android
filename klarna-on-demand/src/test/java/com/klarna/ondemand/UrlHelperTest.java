package com.klarna.ondemand;

import com.klarna.ondemand.crypto.Crypto;
import com.klarna.ondemand.crypto.CryptoFactory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
@PrepareForTest({Context.class, Locale.class, CryptoFactory.class})
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
public class UrlHelperTest {

    private String token = "my_token";
    private android.content.Context context;

    @Rule
    public final PowerMockRule rule = new PowerMockRule();

    @Before
    public void beforeEach() {
        context = Robolectric.application.getApplicationContext();

        mockStatic(Context.class);
        when(Context.getApiKey()).thenReturn("test_skadoo");


        Crypto cryptoMock = mock(Crypto.class);
        when(cryptoMock.getPublicKeyBase64Str()).thenReturn("my_publicKey");

        mockStatic(CryptoFactory.class);
        when(CryptoFactory.getInstance(context)).thenReturn(cryptoMock);
    }

    //region .registrationUrl
    @Test
    public void registrationUrl_ShouldReturnPlaygroundUrl_WhenTokenIsForPlayground() {
        assertThat(UrlHelper.registrationUrl(context)).startsWith("https://inapp.playground.klarna.com/registration/new");
    }

    @Test
    public void registrationUrl_ShouldReturnProductionUrl_WhenTokenIsForProduction() {
        when(Context.getApiKey()).thenReturn("skadoo");
        assertThat(UrlHelper.registrationUrl(context)).startsWith("https://inapp.klarna.com/registration/new");
    }

    @Test
    public void registrationUrl_ShouldReturnUrlWithTheDefaultLocale() {
        Locale.setDefault(new Locale("my_locale"));

        assertThat(UrlHelper.registrationUrl(context)).contains("locale=my_locale");
    }

    @Test
    public void registrationUrl_ShouldIncludeThePublicKeyInTheRegistrationUrl() {
        assertThat(UrlHelper.registrationUrl(context)).contains("public_key=my_publicKey");
    }
    //endregion

    //region .PreferencesUrlWithToken
    @Test
    public void preferencesUrlWithToken_ShouldReturnPlaygroundUrl_WhenTokenIsForPlayground() {
        String expectedPrefix = "https://inapp.playground.klarna.com/users/" + token + "/preferences";
        assertThat(UrlHelper.preferencesUrl(token)).startsWith(expectedPrefix);
    }

    @Test
    public void preferencesUrlWithToken_ShouldReturnProductionUrl_WhenTokenIsForProduction() {
        mockStatic(Context.class);
        when(Context.getApiKey()).thenReturn("skadoo");

        String expectedPrefix = "https://inapp.klarna.com/users/" + token +"/preferences";
        assertThat(UrlHelper.preferencesUrl(token)).startsWith(expectedPrefix);
    }

    @Test
    public void preferencesUrlWithToken_ShouldReturnUrlWithTheDefaultLocale() {
        Locale.setDefault(new Locale("my_locale"));

        assertThat(UrlHelper.preferencesUrl(token)).contains("locale=my_locale");
    }
    //endregion
}
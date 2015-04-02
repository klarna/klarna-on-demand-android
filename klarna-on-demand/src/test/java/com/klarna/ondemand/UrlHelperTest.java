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

    private static final String TOKEN = "my_token";
    private android.content.Context context;
    private Crypto cryptoMock;


    @Rule
    public final PowerMockRule rule = new PowerMockRule();
    @Before
    public void beforeEach() {
        context = Robolectric.application.getApplicationContext();

        mockStatic(Context.class);
        when(Context.getApiKey()).thenReturn("test_skadoo");

        cryptoMock = mock(Crypto.class);
        when(cryptoMock.getPublicKeyBase64Str()).thenReturn("my_publicKey");

        mockStatic(CryptoFactory.class);
        when(CryptoFactory.getInstance(context)).thenReturn(cryptoMock);
    }

    //region .registrationUrl
    @Test
    public void registrationUrl_ShouldReturnPlaygroundUrl_WhenTokenIsForPlayground() {
        assertThat(UrlHelper.registrationUrl(context, null)).startsWith("https://inapp.playground.klarna.com/registration/new");
    }

    @Test
    public void registrationUrl_ShouldReturnProductionUrl_WhenTokenIsForProduction() {
        when(Context.getApiKey()).thenReturn("skadoo");
        assertThat(UrlHelper.registrationUrl(context, null)).startsWith("https://inapp.klarna.com/registration/new");
    }

    @Test
    public void registrationUrl_ShouldReturnUrlWithTheDefaultLocale() {
        Locale.setDefault(new Locale("my_locale"));

        assertThat(UrlHelper.registrationUrl(context, null)).contains("locale=my_locale");
    }

    @Test
    public void registrationUrl_ShouldIncludeThePublicKeyInTheRegistrationUrl() {
        assertThat(UrlHelper.registrationUrl(context, null)).contains("public_key=my_publicKey");
    }

    @Test
    public void registrationUrl_ShouldEncodeUrlParameters() {
        when(cryptoMock.getPublicKeyBase64Str()).thenReturn("my+publicKey");

        assertThat(UrlHelper.registrationUrl(context, null)).contains("public_key=my%2BpublicKey");
    }

    @Test
    public void registrationUrl_ShouldIncludeConfirmedUserDataIdWhenSupplied() {
        assertThat(UrlHelper.registrationUrl(context, new RegistrationSettings(null, "abcd"))).contains("confirmed_user_data_id=abcd");
    }

    @Test
    public void registrationUrl_ShouldIncludePrefillPhoneNumberWhenSupplied() {
        assertThat(UrlHelper.registrationUrl(context, new RegistrationSettings("12345678", null))).contains("prefill_phone_number=12345678");
    }

    //endregion

    //region .PreferencesUrlWithToken
    @Test
    public void preferencesUrlWithToken_ShouldReturnPlaygroundUrl_WhenTokenIsForPlayground() {
        String expectedPrefix = "https://inapp.playground.klarna.com/users/" + TOKEN + "/preferences";
        assertThat(UrlHelper.preferencesUrl(TOKEN)).startsWith(expectedPrefix);
    }

    @Test
    public void preferencesUrlWithToken_ShouldReturnProductionUrl_WhenTokenIsForProduction() {
        mockStatic(Context.class);
        when(Context.getApiKey()).thenReturn("skadoo");

        String expectedPrefix = "https://inapp.klarna.com/users/" + TOKEN + "/preferences";
        assertThat(UrlHelper.preferencesUrl(TOKEN)).startsWith(expectedPrefix);
    }

    @Test
    public void preferencesUrlWithToken_ShouldReturnUrlWithTheDefaultLocale() {
        Locale.setDefault(new Locale("my_locale"));

        assertThat(UrlHelper.preferencesUrl(TOKEN)).contains("locale=my_locale");
    }
    //endregion
}
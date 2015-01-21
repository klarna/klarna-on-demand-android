package com.klarna.ondemand;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
@PrepareForTest(CryptoSharedPreferencesBaseImpl.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*", "org.json.*" })
public class CryptoSharedPreferencesBaseImplTest {

    private Crypto crypto;

    @Before
    public void beforeEach() {
        android.content.Context context = Robolectric.application.getApplicationContext();
        crypto = CryptoSharedPreferencesBaseImpl.getInstance(context);
    }

    @Test
    public void getPublicKeyBase64Str_shouldReturnSameKeyOnTwoConsecutiveCalls () {
        String publicKeyA = crypto.getPublicKeyBase64Str();
        String publicKeyB = crypto.getPublicKeyBase64Str();

        assertThat(publicKeyA).isEqualTo(publicKeyB);
    }

    //region #sign
    @Test
    public void sign_shouldReturnSameSignOnTwoIdenticalCalls() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String signA = crypto.sign("my_messge");
        String signB = crypto.sign("my_messge");

        assertThat(signA).isEqualTo(signB);
    }

    @Test
    public void sign_shouldReturnDifferentSignOnTwoDifferentCalls() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String signA = crypto.sign("my_messgeA");
        String signB = crypto.sign("my_messgeB");

        assertThat(signA).isNotEqualTo(signB);
    }
    //endregion
}

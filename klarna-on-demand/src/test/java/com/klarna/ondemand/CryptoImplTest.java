package com.klarna.ondemand;

import org.junit.Assert;
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

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
@PrepareForTest(CryptoImpl.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*", "org.json.*" })
public class CryptoImplTest {

    private android.content.Context context;

    @Before
    public void init() {
        context = Robolectric.application.getApplicationContext();
    }

    @Test
    public void getPublicKeyBase64Str_shouldReturnSameKeyOnTwoConsecutiveCall () {
        String publicKeyA = CryptoImpl.getInstance(context).getPublicKeyBase64Str();
        String publicKeyB = CryptoImpl.getInstance(context).getPublicKeyBase64Str();

        assertThat(publicKeyA).isEqualTo(publicKeyB);
    }

    //region #sign
    @Test
    public void sign_shouldReturnSameSignOnTwoIdenticalCalls() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String signA = CryptoImpl.getInstance(context).sign("my_messge");
        String signB = CryptoImpl.getInstance(context).sign("my_messge");

        assertThat(signA).isEqualTo(signB);
    }

    @Test
    public void sign_shouldReturnSameSignOnTwoDifferCalls() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String signA = CryptoImpl.getInstance(context).sign("my_messgeA");
        String signB = CryptoImpl.getInstance(context).sign("my_messgeB");

        assertThat(signA).isNotEqualTo(signB);
    }
    //endregion
}

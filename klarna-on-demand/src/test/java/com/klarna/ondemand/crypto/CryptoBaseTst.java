package com.klarna.ondemand.crypto;

import android.content.Context;
import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class CryptoBaseTst {

    protected Crypto crypto;

    @Before
    public void beforeEach() throws Exception {
        Robolectric.Reflection.setFinalStaticField(Build.VERSION.class,
                "SDK_INT", getTestSdkVersion());
        android.content.Context context = Robolectric.application.getApplicationContext();
        crypto = getTestSubject(context);
    }

    protected abstract Crypto getTestSubject(Context context) throws Exception;
    protected abstract int getTestSdkVersion();

    @Test
    public void getPublicKeyBase64Str_shouldReturnSameKeyOnTwoConsecutiveCalls () {
        String publicKeyA = crypto.getPublicKeyBase64Str();
        String publicKeyB = crypto.getPublicKeyBase64Str();

        assertThat(publicKeyA)
                .isNotEmpty()
                .isEqualTo(publicKeyB);
    }

    //region #sign
    @Test
    public void sign_shouldReturnSameSignOnTwoIdenticalCalls() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String signA = crypto.sign("my_messge");
        String signB = crypto.sign("my_messge");

        assertThat(signA)
                .isNotEmpty()
                .isEqualTo(signB);
    }

    @Test
    public void sign_shouldReturnDifferentSignOnTwoDifferentCalls() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String signA = crypto.sign("my_messgeA");
        String signB = crypto.sign("my_messgeB");

        assertThat(signA)
                .isNotEmpty()
                .isNotEqualTo(signB);

        assertThat(signB).isNotEmpty();
    }
    //endregion
}


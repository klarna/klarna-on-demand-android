package com.klarna.ondemand.crypto;

import android.content.Context;
import android.util.Base64;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import static org.assertj.core.api.Assertions.assertThat;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
@PrepareForTest(CryptoBase.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*", "org.json.*" })
public class SharedPreferencesCryptoImplTest extends CryptoBaseTest {

    @Override
    protected int getTestSdkVersion() {
        return 17;
    }

    @Override
    protected CryptoBase getTestSubject(Context context) throws Exception {
        return new SharedPreferencesCryptoImpl(context);
    }

}
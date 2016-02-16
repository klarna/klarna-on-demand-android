package com.klarna.ondemand.crypto;

import android.content.Context;
import android.os.Build;
import android.util.Base64;

import org.junit.Before;
import org.junit.Test;

import org.robolectric.Robolectric;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class CryptoBaseTest {

    protected CryptoBase crypto;
    protected abstract CryptoBase getTestSubject(Context context) throws Exception;
    protected abstract int getTestSdkVersion();

    //region Before each

    @Before
    public void beforeEach() throws Exception {
        Robolectric.Reflection.setFinalStaticField(Build.VERSION.class,
                "SDK_INT", getTestSdkVersion());
        android.content.Context context = Robolectric.application.getApplicationContext();
        crypto = getTestSubject(context);
    }

    //endregion

    //region Instance methods

    @Test
    public void getPublicKeyBase64Str_shouldReturnSameKeyOnTwoConsecutiveCalls () {
        String publicKeyA = crypto.getPublicKeyBase64Str();
        String publicKeyB = crypto.getPublicKeyBase64Str();

        assertThat(publicKeyA)
                .isNotEmpty()
                .isEqualTo(publicKeyB);
    }

    /*
    TODO: https://jira.internal.machines/jira/browse/IACO-1036 - Support latest Android Studio (fix unit tests)
          (JUnit4 makes it almost impossible to write the test for this use-case)

    @Test
    public void sign_shouldSignMessageWithInternalPrivateKey () throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        mockStatic(CryptoBase.class);
        when(CryptoBase.sign("data", crypto.getPrivateKey())).thenReturn("my_signature");

        assertThat(crypto.sign("data")).isEqualTo("my_signature");
    }
    */

    //endregion

    //region Class methods

    @Test
    public void sign_shouldReturnSameSignOnTwoIdenticalCalls() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        PrivateKey privateKey = generatePrivateKey();
        String signA = CryptoBase.sign("my_message", privateKey);
        String signB = CryptoBase.sign("my_message", privateKey);

        assertThat(signA)
                .isNotEmpty()
                .isEqualTo(signB);
    }

    @Test
    public void sign_shouldReturnDifferentSignWhenProvidedWithDifferentMessages() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        PrivateKey privateKey = generatePrivateKey();
        String signA = CryptoBase.sign("my_message_A", privateKey);
        String signB = CryptoBase.sign("my_message_B", privateKey);

        assertThat(signA)
                .isNotEmpty()
                .isNotEqualTo(signB);

        assertThat(signB).isNotEmpty();
    }

    @Test
    public void sign_shouldReturnDifferentSignWhenProvidedWithDifferentPrivateKeys() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String signA = CryptoBase.sign("my_message", generatePrivateKey());
        String signB = CryptoBase.sign("my_message", generatePrivateKey());

        assertThat(signA)
                .isNotEmpty()
                .isNotEqualTo(signB);

        assertThat(signB).isNotEmpty();
    }

    @Test
    public void sign_shouldCaclculateAValidSignature() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decode("MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAskcFcO+vuYAQfRjCGkhZXMGlrtpbJZRq9y0SJi2DlKo2Ph58Ni9j6mN4DVKdSSJhuR/myNzXmszLzZIgq1AwpQIDAQABAkA641rYw1O4YqUPrW3wYJWkHhMsftQ8xZnPrAOiuMYOBN5jJ1pKBXiy3nHxBCiSWAJY+kWSWzbY1zzecBtmbCDBAiEA1lrU0Uhk3H7v7qyZPWIgu2pHYi+/W17c3jsl/NgHQfMCIQDU6dea1HCfWcU31cW/wShhzXd4h1H6AqEmRIEGnCLRBwIhAINNAC9x+NZXqwC4GOXQxdwHLdKnDMAbS4+VC5/ldAyhAiEAs4/PhMWbgdisyi0gzFpz2x/0nRLK4SXskKB/jHqLpmsCICTcahC8Xm8EFD/kxkDpI2oMZuWa2ghvZ1zuSMpnfkgE", Base64.DEFAULT));
        KeyFactory keyFact = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFact.generatePrivate(pkcs8EncodedKeySpec);

        assertThat(CryptoBase.sign("my_data", privateKey)).isEqualTo("n8PEnZ5s5Cn39Jx2nMRf+cTbw7YIw5ak0HBEQJUl4mX/rMjo1SQe/56elt95c5H9TydbEcmvDeDt\n2SpHXylNDA==\n");
    }

    //endregion

    //region Helper methods

    private PrivateKey generatePrivateKey() throws NoSuchAlgorithmException {
        return SharedPreferencesCryptoImpl.generateKeyPair().getPrivate();
    }

    //endregion
}


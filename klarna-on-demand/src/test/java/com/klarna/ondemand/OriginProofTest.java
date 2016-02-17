package com.klarna.ondemand;

import android.util.Base64;

import com.klarna.ondemand.crypto.Crypto;
import com.klarna.ondemand.crypto.CryptoBase;
import com.klarna.ondemand.crypto.CryptoFactory;
import com.klarna.ondemand.crypto.SharedPreferencesCryptoImpl;

import org.json.JSONException;
import org.json.JSONObject;
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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SignatureException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
@PrepareForTest({CryptoFactory.class, CryptoBase.class})
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*", "org.json.*" })
public class OriginProofTest {

    private static final String UUID_PATTERN = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";
    private android.content.Context context;

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    //region Before all

    @Before
    public void init() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        context = Robolectric.application.getApplicationContext();
    }

    //endregion

    //region OriginProof

    @Test
    public void constructor_shouldReturnAbase64EncodedJsonInTheCorrectFormat() throws Exception {
        Crypto cryptoMock = mock(Crypto.class);
        when(cryptoMock.sign(anyString())).thenReturn("my_signature");
        mockStatic(CryptoFactory.class);
        when(CryptoFactory.getInstance(context)).thenReturn(cryptoMock);

        OriginProof originProof = new OriginProof(3600, "SEK", "my_token", context);

        JSONObject originProofJson = getOriginProofJson(originProof);
        assertThat(originProofJson.getString("signature")).isEqualTo("my_signature");
        assertOriginProofData(originProof, 3600, "SEK", "my_token");
    }

    @Test
    public void constructor_shouldGenerateADifferentIdForEachOriginProof() throws Exception {
        OriginProof originProofA = new OriginProof(3600, "SEK", "my_token", context);
        JSONObject dataA = getDataJson(originProofA);

        OriginProof originProofB = new OriginProof(3600, "SEK", "my_token", context);
        JSONObject dataB = getDataJson(originProofB);

        assertThat(dataA.getString("id")).isNotEqualTo(dataB.getString("id"));
    }

    @Test(expected=RuntimeException.class)
    public void constructor_ShouldThrowExceptionWhenItCantGenerateSignature() throws Exception {
        Crypto cryptoMock = mock(Crypto.class);
        when(cryptoMock.sign(anyString())).thenThrow(new SignatureException());
        when(CryptoFactory.getInstance(context)).thenReturn(cryptoMock);

        new OriginProof(3600, "SEK", "my_token", context);
    }

    //endregion

    //region OriginProof with a provided external private key

    @Test
    public void constructor_shouldReturnAbase64EncodedJsonInTheCorrectFormat_whenGivenExternalPrivateKey() throws Exception {
        PrivateKey externalPrivateKey = CryptoBase.generateKeyPair().getPrivate();
        mockStatic(CryptoBase.class);
        when(CryptoBase.sign(anyString(), eq(externalPrivateKey))).thenReturn("my_external_private_key_signature");

        OriginProof originProof = new OriginProof(3600, "SEK", "my_token", externalPrivateKey);

        JSONObject originProofJson = getOriginProofJson(originProof);
        assertThat(originProofJson.getString("signature")).isEqualTo("my_external_private_key_signature");
        assertOriginProofData(originProof, 3600, "SEK", "my_token");
    }

    @Test
    public void constructor_shouldGenerateADifferentIdForEachOriginProof_whenGivenExternalPrivateKey() throws Exception {
        OriginProof originProofA = new OriginProof(3600, "SEK", "my_token", SharedPreferencesCryptoImpl.generateKeyPair().getPrivate());
        JSONObject dataA = getDataJson(originProofA);

        OriginProof originProofB = new OriginProof(3600, "SEK", "my_token", SharedPreferencesCryptoImpl.generateKeyPair().getPrivate());
        JSONObject dataB = getDataJson(originProofB);

        assertThat(dataA.getString("id")).isNotEqualTo(dataB.getString("id"));
    }

    @Test(expected=RuntimeException.class)
    public void constructor_ShouldThrowExceptionWhenItCantGenerateSignature_whenGivenExternalPrivateKey() throws Exception {
        PrivateKey externalPrivateKey = SharedPreferencesCryptoImpl.generateKeyPair().getPrivate();
        mockStatic(CryptoBase.class);
        when(CryptoBase.sign(anyString(), eq(externalPrivateKey))).thenThrow(new SignatureException());

        new OriginProof(3600, "SEK", "my_token", externalPrivateKey);
    }

    //endregion

    //region Helper methods

    private JSONObject getOriginProofJson(OriginProof originProof) throws JSONException {
        String decodedOriginProof = new String(Base64.decode(originProof.toString(), Base64.DEFAULT));
        return new JSONObject(decodedOriginProof);
    }

    private JSONObject getDataJson(OriginProof originProof) throws JSONException {
        JSONObject originProofJson = getOriginProofJson(originProof);
        return new JSONObject(originProofJson.getString("data"));
    }

    private void assertOriginProofData(OriginProof originProof, int amount, String currency, String userToken) throws JSONException {
        JSONObject data = getDataJson(originProof);
        assertThat(data.getInt("amount")).isEqualTo(3600);
        assertThat(data.getString("currency")).isEqualTo("SEK");
        assertThat(data.getString("user_token")).isEqualTo("my_token");
        assertThat(data.getString("id")).matches(UUID_PATTERN);
    }

    //endregion
}

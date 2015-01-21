package com.klarna.ondemand;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
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
import java.security.SignatureException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
@PrepareForTest(CryptoImpl.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*", "org.json.*" })
public class OriginProofTest {

    private static final String UUID_PATTERN = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";
    private android.content.Context context;

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Before
    public void init() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        context = Robolectric.application.getApplicationContext();
        Crypto cryptoMock = mock(Crypto.class);
        when(cryptoMock.sign(anyString())).thenReturn("my_signature");

        mockStatic(CryptoImpl.class);
        when(CryptoImpl.getInstance(context)).thenReturn(cryptoMock);
    }

    @Test(expected=RuntimeException.class)
    public void constructor_ShouldThrowExceptionWhenItCantGenerateSignature() throws Exception {
        Crypto cryptoMock = mock(Crypto.class);
        when(cryptoMock.sign(anyString())).thenThrow(new SignatureException());
        mockStatic(CryptoImpl.class);

        new OriginProof(3600, "SEK", "my_token", context);
    }

    @Test
    public void sign_shouldReturnAbase64EncodedJsonInTheCorrectFormat() throws Exception {
        OriginProof originProof = new OriginProof(3600, "SEK", "my_token", context);

        JSONObject originProofJson = getOriginProofJson(originProof);
        Assert.assertEquals(originProofJson.getString("signature"), "my_signature");

        JSONObject data = getDataJson(originProof);
        Assert.assertEquals(data.getInt("amount"), 3600);
        Assert.assertEquals(data.getString("currency"), "SEK");
        Assert.assertEquals(data.getString("user_token"), "my_token");
        Assert.assertTrue(data.getString("id").matches(UUID_PATTERN));
    }

    @Test
    public void sign_generateDifferentIdForEachOrder() throws Exception {
        OriginProof originProofA = new OriginProof(3600, "SEK", "my_token", context);
        JSONObject dataA = getDataJson(originProofA);

        OriginProof originProofB = new OriginProof(3600, "SEK", "my_token", context);
        JSONObject dataB = getDataJson(originProofB);

        Assert.assertNotEquals(dataA.getString("id"), (dataB.getString("id")));
    }

    private JSONObject getOriginProofJson(OriginProof originProof) throws JSONException {
        String decodeOriginProof = new String(Base64.decode(originProof.toString(), Base64.DEFAULT));
        return new JSONObject(decodeOriginProof);
    }

    private JSONObject getDataJson(OriginProof originProof) throws JSONException {
        JSONObject originProofJson = getOriginProofJson(originProof);
        return new JSONObject(originProofJson.getString("data"));
    }

}

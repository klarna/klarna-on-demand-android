package com.klarna.ondemand;

import android.util.Base64;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
@PrepareForTest(CryptoImpl.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
public class OriginProofTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Test
    public void sign_shouldReturnAbase64EncodedJsonInTheCorrectFormat() throws Exception {
        android.content.Context context = Robolectric.application.getApplicationContext();
        Crypto cryptoMock = mock(Crypto.class);
        when(cryptoMock.sign(anyString())).thenReturn("my_signature");

        mockStatic(CryptoImpl.class);
        when(CryptoImpl.getInstance(context)).thenReturn(cryptoMock);

        String originProof = new OriginProof(context).generate(3600, "SEK", "my_token");

        String decodeOriginProof = Base64.decode(originProof, Base64.DEFAULT).toString();
        JSONObject originProofJson = new JSONObject(decodeOriginProof);
        Assert.assertTrue(originProofJson.getString("signature").equals("my_signature"));

        JSONObject data = new JSONObject(originProofJson.getString("data"));
        Assert.assertTrue(data.getInt("amount") == 3600);
        Assert.assertTrue(data.getString("currency").equals("SEK"));
        Assert.assertTrue(data.getString("user_token").equals("my_token"));
        Assert.assertTrue(data.getString("nonce") == "dd");
    }

}

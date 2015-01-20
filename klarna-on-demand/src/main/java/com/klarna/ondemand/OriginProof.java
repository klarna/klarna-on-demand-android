package com.klarna.ondemand;

import android.util.Base64;
import org.json.JSONObject;
import java.util.UUID;

public class OriginProof {

    private final android.content.Context context;
    private final int amount;
    private final String currency;
    private final String userToken;
    private final UUID uuid;
    private final String originProofBase64Str;

    public OriginProof(int amount, String currency, String userToken, android.content.Context context) {
        this.amount = amount;
        this.currency = currency;
        this.userToken = userToken;
        this.context = context;
        this.uuid = UUID.randomUUID();
        originProofBase64Str = generateBase64Str();
    }

    @Override
    public String toString() {
        return originProofBase64Str;
    }

    private String generateBase64Str(){
        try {
            JSONObject data = new JSONObject()
                    .put("amount", amount)
                    .put("currency", currency)
                    .put("user_token", userToken)
                    .put("id", uuid.toString());
            String signature = CryptoImpl.getInstance(context).sign(data.toString());

            JSONObject originProof = new JSONObject()
                    .put("data", data.toString())
                    .put("signature", signature);

            return Base64.encodeToString(
                    originProof.toString().getBytes(), Base64.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException("Could not generate origin proof String", e);
        }
    }
}
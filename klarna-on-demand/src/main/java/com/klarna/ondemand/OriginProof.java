package com.klarna.ondemand;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.UUID;

public class OriginProof {

    private final android.content.Context context;

    public OriginProof(android.content.Context context) {
        this.context = context;
    }

    public String generate(int amount, String currency, String userToken){
        try {
            JSONObject data = new JSONObject()
                    .put("amount", amount)
                    .put("currency", currency)
                    .put("user_token", userToken)
                    .put("id", UUID.randomUUID().toString());
            String signature = CryptoImpl.getInstance(context).sign(data.toString());

            JSONObject originProof = new JSONObject()
                    .put("data", data.toString())
                    .put("signature", signature);

            return Base64.encodeToString(
                    originProof.toString().getBytes(), Base64.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException("Could not generate origin proof ", e);
        }
    }
}
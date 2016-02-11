package com.klarna.ondemand;

import android.util.Base64;

import com.klarna.ondemand.crypto.CryptoBase;
import com.klarna.ondemand.crypto.CryptoFactory;

import org.json.JSONObject;
import java.util.UUID;

/**
 * An origin proof that allows performing secure purchases.
 */
public class OriginProof {

    private final android.content.Context context;
    private final int amount;
    private final String currency;
    private final String userToken;
    private final String externalPrivateKey;
    private final UUID uuid;
    private final String originProofBase64Str;

    private OriginProof(int amount, String currency, String userToken, String externalPrivateKey, android.content.Context context) {
        this.amount = amount;
        this.currency = currency;
        this.userToken = userToken;
        this.context = context;
        this.externalPrivateKey = externalPrivateKey;
        this.uuid = UUID.randomUUID();
        originProofBase64Str = generateBase64Str();
    }
    /**
     * Generates an origin proof that can be used to verify the details of the purchase.
     * @param amount             The purchase's total amount in "cents"
     * @param currency           The currency used in this purchase ("SEK", "EUR", or other <a href="http://en.wikipedia.org/wiki/ISO_4217">ISO 4217 codes</a>)
     * @param userToken          Token identifying the user making the purchase
     * @param externalPrivateKey Private key stored in device, in case cryptographic keys generation is performed outside of the SDK
     */
    public OriginProof(int amount, String currency, String userToken, String externalPrivateKey) {
        this(amount, currency, userToken, externalPrivateKey, null);
    }

    /**
     * Generates an origin proof that can be used to verify the details of the purchase.
     * @param amount    The purchase's total amount in "cents"
     * @param currency  The currency used in this purchase ("SEK", "EUR", or other <a href="http://en.wikipedia.org/wiki/ISO_4217">ISO 4217 codes</a>)
     * @param userToken Token identifying the user making the purchase
     * @param context   The android context
     */
    public OriginProof(int amount, String currency, String userToken, android.content.Context context) {
        this(amount, currency, userToken, null, context);
    }

    /**
     *  Returns a textual representation of the origin proof that needs to be sent along with the purchase for it to succeed.
     *
     *  @return A textual representation of the origin proof that needs to be sent along with the purchase for it to succeed.
     */
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

            String signature = null;
            if(externalPrivateKey == null) {
                signature = CryptoFactory.getInstance(context).sign(data.toString());
            }
            else {
                signature = CryptoBase.sign(data.toString(), externalPrivateKey);
            }

            JSONObject originProof = new JSONObject()
                    .put("data", data.toString())
                    .put("signature", signature);

            return Base64.encodeToString(
                    originProof.toString().getBytes(), Base64.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException("Could not generate origin proof String.", e);
        }
    }
}
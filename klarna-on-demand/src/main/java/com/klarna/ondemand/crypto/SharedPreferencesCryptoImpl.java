package com.klarna.ondemand.crypto;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class SharedPreferencesCryptoImpl extends CryptoBase {
    private static final String PUBLIC_KEY = "PublicKey";
    private static final String PRIVATE_KEY = "PrivateKey";

    protected SharedPreferencesCryptoImpl(android.content.Context context) throws NoSuchAlgorithmException, InvalidKeySpecException {
        super();
        SharedPreferences sharedPreferences = getSharedPreferences(context);

        if (!isAlreadyInUse(sharedPreferences)) {
            KeyPair keyPair = generateKeyPair();
            persistKeyPair(sharedPreferences, keyPair);
        }

        publicKey = readPublicKey(sharedPreferences);
        privateKey = readPrivateKey(sharedPreferences);
        publicKeyBase64Str = toBase64(publicKey);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(
                    SharedPreferencesCryptoImpl.class.getPackage().getName(),
                    Context.MODE_PRIVATE);
    }

    private void persistKeyPair(SharedPreferences sharedPreferences, KeyPair keyPair) {
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

        sharedPreferencesEditor.putString(PUBLIC_KEY, toBase64(keyPair.getPublic()));
        sharedPreferencesEditor.putString(PRIVATE_KEY, toBase64(keyPair.getPrivate()));

        sharedPreferencesEditor.commit();
    }

    private String toBase64(Key key) {
        return new String(Base64.encode(key.getEncoded(), Base64.DEFAULT));
    }

    private PublicKey readPublicKey(SharedPreferences sharedPreferences) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String pubKeyStr = sharedPreferences.getString(PUBLIC_KEY, null);
        if (pubKeyStr == null) {
            return null;
        }
        byte[] sigBytes = Base64.decode(pubKeyStr, Base64.DEFAULT);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(sigBytes);
        KeyFactory keyFact = KeyFactory.getInstance(ALGORITHM);
        return keyFact.generatePublic(x509KeySpec);
    }

    private PrivateKey readPrivateKey(SharedPreferences sharedPreferences) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String privKeyStr = sharedPreferences.getString(PRIVATE_KEY, null);
        if (privKeyStr == null) {
            return null;
        }
        byte[] sigBytes = Base64.decode(privKeyStr, Base64.DEFAULT);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(sigBytes);
        KeyFactory keyFact = KeyFactory.getInstance(ALGORITHM);
        return keyFact.generatePrivate(pkcs8EncodedKeySpec);
    }

    public static boolean isAlreadyInUse(android.content.Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return isAlreadyInUse(sharedPreferences);
    }

    private static boolean isAlreadyInUse(SharedPreferences sharedPreferences) {
        return sharedPreferences.contains(PUBLIC_KEY) &&
                sharedPreferences.contains(PRIVATE_KEY);
    }
}

package com.klarna.ondemand;

import android.content.*;
import android.content.Context;
import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

class CryptoSharedPreferencesBaseImpl implements Crypto {
    private static final String PUBLIC_KEY = "PublicKey";
    private static final String PRIVATE_KEY = "PrivateKey";
    private static final String ALGORITHM = "RSA";
    private static final String DIGEST_ALGORITHM = "SHA256withRSA";
    private static final int KEYSIZE = 512;
    public static final String KLARNA = ".KLARNA.";

    private static CryptoSharedPreferencesBaseImpl cyptoInstance;

    private String publicKeyBase64Str;
    private PublicKey publicKey;
    private PrivateKey privateKey;


    public synchronized static Crypto getInstance(android.content.Context context) {
        if (cyptoInstance == null) {
            try {
                cyptoInstance = new CryptoSharedPreferencesBaseImpl(context.getApplicationContext());
            } catch (Exception e) {
                throw new RuntimeException("Could not initialize " + CryptoSharedPreferencesBaseImpl.class.getName(), e);
            }
        }
        return cyptoInstance;
    }

    private CryptoSharedPreferencesBaseImpl(android.content.Context context) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getPackageName() + KLARNA,
                Context.MODE_PRIVATE);

        publicKey = readPublicKey(sharedPreferences);
        privateKey = readPrivateKey(sharedPreferences);
        if (publicKey == null || privateKey == null) {
            KeyPair keyPair = generateKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();

            persistKeyPair(sharedPreferences, keyPair);
        }

        publicKeyBase64Str = toBase64(publicKey);
    }

    @Override
    public String getPublicKeyBase64Str() {
        return publicKeyBase64Str;
    }

    protected PrivateKey getPrivateKey() {
        return privateKey;
    }

    @Override
    public String sign(String message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sign = Signature.getInstance(DIGEST_ALGORITHM);
        sign.initSign(getPrivateKey());
        sign.update(message.getBytes());
        return new String(Base64.encode(sign.sign(), Base64.DEFAULT));
    }

    private void persistKeyPair(SharedPreferences sharedPreferences, KeyPair keyPair) {
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

        sharedPreferencesEditor.putString(PUBLIC_KEY, toBase64(keyPair.getPublic()));
        sharedPreferencesEditor.putString(PRIVATE_KEY, toBase64(keyPair.getPrivate()));

        sharedPreferencesEditor.commit();
    }

    private void persistKey(SharedPreferences.Editor sharedPreferencesEditor, String keyName, Key key) {
        byte[] keyBytes = key.getEncoded();
        String keyStr = new String(Base64.encode(keyBytes, Base64.DEFAULT));
        sharedPreferencesEditor.putString(keyName, keyStr);
    }

    private String toBase64(Key key) {
        return new String(Base64.encode(key.getEncoded(), Base64.DEFAULT));
    }

    private KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
        kpg.initialize(KEYSIZE);
        return kpg.genKeyPair();
    }

    private PublicKey readPublicKey(SharedPreferences sharedPerfernces) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String pubKeyStr = sharedPerfernces.getString(PUBLIC_KEY, null);
        if (pubKeyStr == null) {
            return null;
        }
        byte[] sigBytes = Base64.decode(pubKeyStr, Base64.DEFAULT);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(sigBytes);
        KeyFactory keyFact = KeyFactory.getInstance(ALGORITHM);
        return keyFact.generatePublic(x509KeySpec);
    }

    private PrivateKey readPrivateKey(SharedPreferences sharedPerferences) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String privKeyStr = sharedPerfernces.getString(PRIVATE_KEY, null);
        if (privKeyStr == null) {
            return null;
        }
        byte[] sigBytes = Base64.decode(privKeyStr, Base64.DEFAULT);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(sigBytes);
        KeyFactory keyFact = KeyFactory.getInstance(ALGORITHM);
        return keyFact.generatePrivate(pkcs8EncodedKeySpec);
    }
}

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

class CryptoImpl implements Crypto {
    public static final String PUBLIC_KEY = "PublicKey";
    public static final String PRIVATE_KEY = "PrivateKey";
    public static final String PREFERENCES_FILE_NAME = "KeyPair";
    public static final String ALGORITHM = "RSA";

    private static CryptoImpl objCrypto;

    private String publicKeyBase64Str;
    private PublicKey publicKey;
    private PrivateKey privateKey;


    public static Crypto getInstance(android.content.Context context) {
        if (objCrypto == null) {
            try {
                objCrypto = new CryptoImpl(context);
            } catch (Exception e) {
                throw new RuntimeException("Could not initialize " + CryptoImpl.class.getName(), e);
            }
        }
        return objCrypto;
    }

    private CryptoImpl(android.content.Context context) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);

        publicKey = readPublicKey(sharedPreferences);
        privateKey = readPrivateKey(sharedPreferences);
        if (publicKey == null || privateKey == null) {
            KeyPair keyPair = generateKeyPair();
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();

            persistKeyPair(sharedPreferences, keyPair);
        }

        publicKeyBase64Str = new String(Base64.encode(publicKey.getEncoded(), Base64.DEFAULT));
    }

    @Override
    public String getPublicKeyBase64Str() {
        return publicKeyBase64Str;
    }

    @Override
    public String sign(String message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(privateKey);
        sign.update(message.getBytes());
        return new String(Base64.encode(sign.sign(), Base64.DEFAULT));
    }

    private void persistKeyPair(SharedPreferences sharedPreferences, KeyPair keyPair) {
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();

        persistKey(sharedPreferencesEditor, PUBLIC_KEY, keyPair.getPublic());
        persistKey(sharedPreferencesEditor, PRIVATE_KEY, keyPair.getPrivate());

        sharedPreferencesEditor.commit();
    }

    private void persistKey(SharedPreferences.Editor sharedPreferencesEditor, String keyName, Key key) {
        byte[] keyBytes = key.getEncoded();
        String keyStr = new String(Base64.encode(keyBytes, Base64.DEFAULT));
        sharedPreferencesEditor.putString(keyName, keyStr);
    }

    private KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
        kpg.initialize(512);
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

    private PrivateKey readPrivateKey(SharedPreferences sharedPerfernces) throws NoSuchAlgorithmException, InvalidKeySpecException {
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

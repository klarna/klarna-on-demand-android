package com.klarna.ondemand.crypto;

import android.util.Base64;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;

public abstract class CryptoBase implements Crypto {

    private static final String DIGEST_ALGORITHM = "SHA256withRSA";
    protected static final String ALGORITHM = "RSA";
    private static final int KEYSIZE = 512;
    protected String publicKeyBase64Str;
    protected PublicKey publicKey;
    protected PrivateKey privateKey;

    @Override
    public String getPublicKeyBase64Str() {
        return publicKeyBase64Str;
    }

    public static String sign(String message, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sign = Signature.getInstance(DIGEST_ALGORITHM);
        sign.initSign(privateKey);
        sign.update(message.getBytes());
        return new String(Base64.encode(sign.sign(), Base64.DEFAULT));
    }

    @Override
    public String sign(String message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return sign(message, getPrivateKey());
    }

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
        kpg.initialize(KEYSIZE);
        return kpg.genKeyPair();
    }

    protected PrivateKey getPrivateKey() {
        return privateKey;
    }

}

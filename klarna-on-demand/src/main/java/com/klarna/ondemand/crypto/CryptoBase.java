package com.klarna.ondemand.crypto;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

/**
 * This class contains common functionality for Crypto objects.
 */
public abstract class CryptoBase implements Crypto {

    private static final String DIGEST_ALGORITHM = "SHA256withRSA";
    protected static final String ALGORITHM = "RSA";
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

    protected PrivateKey getPrivateKey() {
        return privateKey;
    }

}

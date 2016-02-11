package com.klarna.ondemand.crypto;

import android.util.Base64;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
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

    public static String sign(String message, String privateKey) throws GeneralSecurityException {
        return sign(message, loadPrivateKey(privateKey));
    }

    @Override
    public String sign(String message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        return sign(message, getPrivateKey());
    }

    protected PrivateKey getPrivateKey() {
        return privateKey;
    }

    public static PrivateKey loadPrivateKey(String key64) throws GeneralSecurityException {
        byte[] clear = Base64.decode(key64, Base64.DEFAULT);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey generatedKey = keyFactory.generatePrivate(keySpec);
        Arrays.fill(clear, (byte) 0);
        return generatedKey;
    }

}

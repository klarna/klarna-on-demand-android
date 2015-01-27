package com.klarna.ondemand.crypto;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

abstract class CryptoBase implements Crypto {

    private static final String DIGEST_ALGORITHM = "SHA256withRSA";

    protected String publicKeyBase64Str;
    protected PublicKey publicKey;
    protected PrivateKey privateKey;

    @Override
    public String getPublicKeyBase64Str() {
        return publicKeyBase64Str;
    }

    @Override
    public String sign(String message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sign = Signature.getInstance(DIGEST_ALGORITHM);
        sign.initSign(getPrivateKey());
        sign.update(message.getBytes());
        return new String(Base64.encode(sign.sign(), Base64.DEFAULT));
    }

    protected PrivateKey getPrivateKey() {
        return privateKey;
    }
}

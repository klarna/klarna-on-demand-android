package com.klarna.ondemand.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/**
 * This class is internal and shouldn't be used directly.
 */
public interface Crypto {

    public String getPublicKeyBase64Str();

    public String sign(String message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException;
}

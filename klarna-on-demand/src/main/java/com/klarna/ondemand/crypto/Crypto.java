package com.klarna.ondemand.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/**
 * Interface for Crypto instances.
 * a Crypto instance is an instance that stores public and private keys, and allows signing messages using its public key.
 * This interface is internal and shouldn't be used directly.
 */
public interface Crypto {

    public String getPublicKeyBase64Str();

    public String sign(String message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException;
}

package com.klarna.ondemand;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public interface Crypto {

    public String getPublicKeyBase64Str();

    public String sign(String message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException;
}

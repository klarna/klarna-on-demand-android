package com.klarna.ondemand;

import android.util.Base64;

import java.io.IOException;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;

class Crypto {

    private static Crypto objCrypto;

    private PublicKey publicKey;
    private PrivateKey privateKey;


    protected static Crypto getInstance()
    {
        if (objCrypto == null)
        {
            objCrypto = new Crypto();
        }
        return objCrypto;
    }

    private Crypto() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(512);
            KeyPair kp = kpg.genKeyPair();
            publicKey = kp.getPublic();
            privateKey = kp.getPrivate();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getPublicKeyBase64Str() {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey.getEncoded());
        System.out.println(Base64.encode(publicKey.getEncoded(), Base64.DEFAULT));

        OpenSSLRSAPublicKey k =new OpenSSLRSAPublicKey();

        System.out.println(keySpec.toString());
        System.out.println(publicKey.toString());
        System.out.println(publicKey.getFormat());
        return new String(publicKey.getEncoded());
    }
}

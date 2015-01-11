package com.klarna.ondemand;

import android.security.KeyPairGeneratorSpec;
import android.util.Base64;

import org.spongycastle.util.io.pem.PemObject;
import org.spongycastle.util.io.pem.PemWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;

class Crypto {
    static {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

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
            kpg.initialize(KeyPairGeneratorSpec.Builder(getApplicationContext())
                    .setAlias("bla")
                    .setKeySize(512)
                    .build());
            KeyPair kp = kpg.genKeyPair();
            publicKey = kp.getPublic();
            privateKey = kp.getPrivate();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getPublicKeyBase64Str() {
        StringWriter writer = new StringWriter();
        PemWriter pemWriter = new PemWriter(writer);
        try {
            pemWriter.writeObject(new PemObject("RSA PUBLIC KEY", publicKey.getEncoded()));
            pemWriter.flush();
            pemWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(writer.toString());
        return writer.toString();
    }
}

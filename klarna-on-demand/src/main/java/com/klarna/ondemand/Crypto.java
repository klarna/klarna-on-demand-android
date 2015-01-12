package com.klarna.ondemand;

import android.content.SharedPreferences;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;
import android.util.Log;

import org.spongycastle.util.io.pem.PemObject;
import org.spongycastle.util.io.pem.PemWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;

class Crypto {
    static {
       // Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    private static Crypto objCrypto;
    private SharedPreferences.Editor sharedPreferencesEditor;
    private SharedPreferences sharedPerfernces;
    private android.content.Context context;

    private PublicKey publicKey;
    private PrivateKey privateKey;


    protected static Crypto getInstance(android.content.Context context) {
        if (objCrypto == null)
        {
            objCrypto = new Crypto(context);
        }
        return objCrypto;
    }

    private Crypto(android.content.Context context) {
        try {
            this.context = context;
            sharedPerfernces = context.getSharedPreferences("KeyPair", android.content.Context.MODE_PRIVATE);
            sharedPreferencesEditor = sharedPerfernces.edit();

            publicKey = getPublicKey();
            privateKey = getPrivateKey();

            if (publicKey == null || privateKey == null) {
                KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
                kpg.initialize(512);
                KeyPair kp = kpg.genKeyPair();
                publicKey = kp.getPublic();
                privateKey = kp.getPrivate();

                byte[] publicKeyBytes = publicKey.getEncoded();
                String pubKeyStr = new String(Base64.encode(publicKeyBytes, Base64.DEFAULT));
                byte[] privKeyBytes = privateKey.getEncoded();
                String privKeyStr = new String(Base64.encode(privKeyBytes, Base64.DEFAULT));


                System.out.print(pubKeyStr);
                getPublicKeyBase64Str();



                sharedPreferencesEditor.putString("PublicKey", pubKeyStr);
                sharedPreferencesEditor.putString("PrivateKey", privKeyStr);
                sharedPreferencesEditor.commit();
            }



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

    public PublicKey getPublicKey(){
        String pubKeyStr = sharedPerfernces.getString("PublicKey", "");
        byte[] sigBytes = Base64.decode(pubKeyStr, Base64.DEFAULT);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(sigBytes);
        KeyFactory keyFact = null;
        try {
            keyFact = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            return  keyFact.generatePublic(x509KeySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }


    public PrivateKey getPrivateKey(){
        String privKeyStr = sharedPerfernces.getString("PrivateKey", "");
        byte[] sigBytes = Base64.decode(privKeyStr, Base64.DEFAULT);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(sigBytes);
        KeyFactory keyFact = null;
        try {
            keyFact = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            return  keyFact.generatePrivate(pkcs8EncodedKeySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.klarna.ondemand;

import android.content.SharedPreferences;
import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

class Crypto {
    static {
       // Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
    }

    private static Crypto objCrypto;
    private String publicKeyBase64Str;
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



                sharedPreferencesEditor.putString("PublicKey", pubKeyStr);
                sharedPreferencesEditor.putString("PrivateKey", privKeyStr);
                sharedPreferencesEditor.commit();
            }
            publicKeyBase64Str =  new String(Base64.encode(publicKey.getEncoded(), Base64.DEFAULT));
            String bla = sign("blabla");
            System.out.print(getPublicKeyBase64Str());
            System.out.println("##################################");
            System.out.println(bla);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected String getPublicKeyBase64Str() {
        System.out.print(publicKeyBase64Str);
        return publicKeyBase64Str;
    }

    protected String sign(String message){
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(privateKey);
            sign.update(message.getBytes());
            return new String(Base64.encode(sign.sign(), Base64.DEFAULT));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;

    }

    private PublicKey getPublicKey(){
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


    private PrivateKey getPrivateKey(){
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

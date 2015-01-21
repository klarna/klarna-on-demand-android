package com.klarna.ondemand;


import android.annotation.TargetApi;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;
import android.util.Log;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

import javax.security.auth.x500.X500Principal;

public class CryptoBaseKeyStoreImpl implements Crypto {


    private final String alias = "al";
    private String publicKeyBase64Str;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public CryptoBaseKeyStoreImpl(android.content.Context context) {
        try{

            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);
            KeyStore.Entry entry = ks.getEntry(alias, null);

            if (entry == null) {

                privateKey = ((KeyStore.PrivateKeyEntry) entry).getPrivateKey();
                publicKey = ((KeyStore.PrivateKeyEntry) entry).getCertificate().getPublicKey();

            } else {

                Calendar cal = Calendar.getInstance();
                Date now = cal.getTime();
                cal.add(Calendar.YEAR, 1);
                Date end = cal.getTime();

                KeyPairGenerator kpg = null;
                kpg = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                kpg.initialize(new KeyPairGeneratorSpec.Builder(context)
                        .setAlias(alias)
                        .setStartDate(now)
                        .setEndDate(end)
                        .setSerialNumber(BigInteger.valueOf(1))
                        .setSubject(new X500Principal("CN=test1"))
                        .setKeySize(512)
                        .build());


                KeyPair keyPair = kpg.generateKeyPair();
                publicKey = keyPair.getPublic();
                privateKey = keyPair.getPrivate();

            }

            publicKeyBase64Str = new String(Base64.encode(publicKey.getEncoded(), Base64.DEFAULT));







        } catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public String getPublicKeyBase64Str() {
        return null;
    }

    @Override
    public String sign(String message) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        try{





            KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
            ks.load(null);

            KeyStore.Entry entry = ks.getEntry(alias, null);
            if (!(entry instanceof KeyStore.PrivateKeyEntry)) {
                return null;
            }

            PrivateKey pprivateKey = ((KeyStore.PrivateKeyEntry) entry).getPrivateKey();
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(pprivateKey);
            sign.update("blabla".getBytes());
            return new String(Base64.encode(sign.sign(), Base64.DEFAULT));

        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

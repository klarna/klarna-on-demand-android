package com.klarna.ondemand.crypto;


import android.annotation.TargetApi;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Calendar;
import java.util.Date;

import javax.security.auth.x500.X500Principal;

class KeyStoreCryptoImpl extends CryptoBase {

    private final String ALIAS = "KLARNA";
    private final KeyStore ks;

    protected KeyStoreCryptoImpl(android.content.Context context) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableEntryException, NoSuchProviderException, InvalidAlgorithmParameterException {
        ks = KeyStore.getInstance("AndroidKeyStore");
        ks.load(null);
        KeyStore.Entry entry = ks.getEntry(ALIAS, null);

        if (entry == null) {
            KeyPair keyPair = generateKeyPair(context);
            publicKey = keyPair.getPublic();
            privateKey = keyPair.getPrivate();
        } else {
            privateKey = ((KeyStore.PrivateKeyEntry) entry).getPrivateKey();
            publicKey = ((KeyStore.PrivateKeyEntry) entry).getCertificate().getPublicKey();
        }

        publicKeyBase64Str = new String(Base64.encode(publicKey.getEncoded(), Base64.DEFAULT));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private KeyPair generateKeyPair(android.content.Context context) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.add(Calendar.YEAR, 20);
        Date end = cal.getTime();

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
        kpg.initialize(new KeyPairGeneratorSpec.Builder(context)
                .setAlias(ALIAS)
                .setStartDate(now)
                .setEndDate(end)
                .setSerialNumber(BigInteger.valueOf(1))
                .setSubject(new X500Principal("CN=test1"))
                .build());

        return kpg.generateKeyPair();
    }
}
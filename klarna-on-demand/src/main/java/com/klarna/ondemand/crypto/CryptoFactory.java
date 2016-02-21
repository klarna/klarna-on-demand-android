package com.klarna.ondemand.crypto;

import android.content.Context;
import android.os.Build;

/**
 * This factory class creates new cryptographic objects depending on the current Android version.
 * Note: currently this factory creates only SharedPreferencesCryptoImpl objects.
 */
public class CryptoFactory {

    private volatile static Crypto cyptoInstance;

    public synchronized static Crypto getInstance(android.content.Context context) {
        if (cyptoInstance == null) {
            try {
                synchronized(CryptoFactory.class) {
                    if (cyptoInstance == null) {
                        Context applicationContext = context.getApplicationContext();

                        cyptoInstance = new SharedPreferencesCryptoImpl(applicationContext);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Could not initialize " + CryptoFactory.class.getName(), e);
            }
        }
        return cyptoInstance;
    }
}

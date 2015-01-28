package com.klarna.ondemand.crypto;

import android.content.Context;
import android.os.Build;

public class CryptoFactory {

    private static Crypto cyptoInstance;

    public synchronized static Crypto getInstance(android.content.Context context) {
        if (cyptoInstance == null) {
            try {
                synchronized(CryptoFactory.class) {
                    if (cyptoInstance == null) {
                        Context applicationContext = context.getApplicationContext();

                        if (isVersionSmallerThenJellyBeanMr2() ||
                                SharedPreferencesCryptoImpl.isAlreadyUseing(context)) {
                            cyptoInstance = new SharedPreferencesCryptoImpl(applicationContext);
                        } else {
                            cyptoInstance = new KeyStoreCryptoImpl(applicationContext);
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Could not initialize " + CryptoFactory.class.getName(), e);
            }
        }
        return cyptoInstance;
    }

    private static boolean isVersionSmallerThenJellyBeanMr2() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2;
    }
}

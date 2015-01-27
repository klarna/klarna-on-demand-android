package com.klarna.ondemand;

import com.klarna.ondemand.crypto.CryptoFactory;

import java.util.Locale;

final class UrlHelper {

    private static final String KlarnaPlaygroundUrl = "https://inapp.playground.klarna.com";
    private static final String KlarnaProductionUrl = "https://inapp.klarna.com";

    static String baseUrl() {
        if(com.klarna.ondemand.Context.getApiKey().startsWith("test_")) {
            return KlarnaPlaygroundUrl;
        }
        return KlarnaProductionUrl;
    }

    static String registrationUrl(android.content.Context context) {
        return String.format("%s/registration/new?api_key=%s&locale=%s&public_key=%s",
                baseUrl(),
                Context.getApiKey(),
                defaultLocale(),
                CryptoFactory.getInstance(context).getPublicKeyBase64Str());
    }

    static String preferencesUrl(String token) {
        return String.format("%s/users/%s/preferences?api_key=%s&locale=%s", baseUrl(), token, Context.getApiKey(), defaultLocale());
    }

    static String defaultLocale() {
        return Locale.getDefault().getLanguage();
    }
}

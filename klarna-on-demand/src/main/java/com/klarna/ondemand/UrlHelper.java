package com.klarna.ondemand;

import java.util.Locale;

public final class UrlHelper {

    private static final String KlarnaPlaygroundUrl = "https://inapp.playground.klarna.com";
    private static final String KlarnaProductionUrl = "https://inapp.klarna.com";

    static String baseUrl() {
        if(Context.getApiKey().startsWith("test_")) {
            return KlarnaPlaygroundUrl;
        }
        return KlarnaProductionUrl;
    }

    static String registrationUrl() {
        return String.format("%s/registration/new?api_key=%s&locale=%s&public_key=%s",
                baseUrl(),
                Context.getApiKey(),
                defaultLocale(),
                Crypto.getInstance().getPublicKeyBase64Str());
    }

    static String preferencesUrl(String token) {
        return String.format("%s/users/%s/preferences?api_key=%s&locale=%s", baseUrl(), token, Context.getApiKey(), defaultLocale());
    }

    static String defaultLocale() {
        return Locale.getDefault().getLanguage();
    }
}

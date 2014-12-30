package com.klarna.ondemand;

import java.util.Locale;

public final class KODUrl {

    private static final String KlarnaPlaygroundUrl = "https://inapp.playground.klarna.com";
    private static final String KlarnaProductionUrl = "https://inapp.klarna.com";

    static String baseUrl() {
        if(Context.getApiKey().startsWith("test_")) {
            return KlarnaPlaygroundUrl;
        }
        return KlarnaProductionUrl;
    }

    static String registrationUrl() {
        return String.format("%s/registration/new?api_key=%s&locale=%s", KODUrl.baseUrl(), Context.getApiKey(), Locale.getDefault().getLanguage());
    }

    static String preferencesUrl(String token) {
        return String.format("%s/users/%s/preferences?api_key=%s", KODUrl.baseUrl(), token, Context.getApiKey());
    }
}

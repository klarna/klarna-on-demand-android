package com.klarna.ondemand;

import android.net.Uri;

import com.klarna.ondemand.crypto.CryptoFactory;

import java.util.Locale;

final class UrlHelper {

    private static final String KLARNA_PLAYGROUND_AUTHORITY = "inapp.playground.klarna.com";
    private static final String KLARNA_PRODUCTION_AUTHORITY = "inapp.klarna.com";

    static String getAuthority() {
        if(com.klarna.ondemand.Context.getApiKey().startsWith("test_")) {
            return KLARNA_PLAYGROUND_AUTHORITY;
        }
        return KLARNA_PRODUCTION_AUTHORITY;
    }

    static String registrationUrl(android.content.Context applicationContext, RegistrationSettings settings) {
        Uri.Builder builder = new Uri.Builder();

        builder.scheme("https")
                .authority(getAuthority())
                .appendPath("registration")
                .appendPath("new")
                .appendQueryParameter("api_key", Context.getApiKey())
                .appendQueryParameter("locale", Context.getLanguage(applicationContext))
                .appendQueryParameter("public_key", CryptoFactory.getInstance(applicationContext).getPublicKeyBase64Str());

        if(settings != null) {
            if (!HelperMethods.isBlank(settings.getPrefillPhoneNumber())) {
                builder.appendQueryParameter("prefill_phone_number", settings.getPrefillPhoneNumber());
            }

            if (!HelperMethods.isBlank(settings.getConfirmedUserDataId())) {
                builder.appendQueryParameter("confirmed_user_data_id", settings.getConfirmedUserDataId());
            }
        }

        return builder.build().toString();
    }

    static String preferencesUrl(android.content.Context applicationContext, String token) {
        Uri.Builder builder = new Uri.Builder();
        return builder.scheme("https")
                .authority(getAuthority())
                .appendPath("users")
                .appendPath(token)
                .appendPath("preferences")
                .appendQueryParameter("api_key", Context.getApiKey())
                .appendQueryParameter("locale", Context.getLanguage(applicationContext))
                .build()
                .toString();
    }
}

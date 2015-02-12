package com.klarna.ondemand;

import android.net.Uri;

import com.klarna.ondemand.crypto.CryptoFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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

    static String registrationUrl(android.content.Context context, RegistrationSettings settings) {
        Uri.Builder builder = new Uri.Builder();

        builder.scheme("https")
                .authority(getAuthority())
                .appendPath("registration")
                .appendPath("new")
                .appendQueryParameter("api_key", Context.getApiKey())
                .appendQueryParameter("locale", defaultLocale())
                .appendQueryParameter("public_key", CryptoFactory.getInstance(context).getPublicKeyBase64Str());

        if(settings != null && settings.confirmedUserDataId != null) {
            builder.appendQueryParameter("confirmed_user_data_id", settings.confirmedUserDataId);
        }

        return builder.build().toString();
    }

    static String preferencesUrl(String token) {
        Uri.Builder builder = new Uri.Builder();
        return builder.scheme("https")
                .authority(getAuthority())
                .appendPath("users")
                .appendPath(token)
                .appendPath("preferences")
                .appendQueryParameter("api_key", Context.getApiKey())
                .appendQueryParameter("locale", defaultLocale())
                .build()
                .toString();
    }

    static String defaultLocale() {
        return Locale.getDefault().getLanguage();
    }
}

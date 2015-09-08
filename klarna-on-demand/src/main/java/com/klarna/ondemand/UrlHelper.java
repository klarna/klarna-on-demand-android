package com.klarna.ondemand;

import android.net.Uri;

import com.klarna.ondemand.crypto.CryptoFactory;

import java.util.Locale;

final class UrlHelper {

    private static final String KLARNA_PLAYGROUND_AUTHORITY = "ondemand-dg.klarna.com";
    private static final String KLARNA_PRODUCTION_AUTHORITY = "ondemand.klarna.com";

    static String getAuthority() {
        if(com.klarna.ondemand.Context.getApiKey().startsWith("test_")) {
            return KLARNA_PLAYGROUND_AUTHORITY;
        }
        return KLARNA_PRODUCTION_AUTHORITY;
    }

    static String registrationUrl(android.content.Context context, RegistrationSettings settings) {
        Uri.Builder builder = Uri.parse("http://192.168.56.1:8000").buildUpon()
                                               .appendPath("web")
                                               .appendPath("registration")
                                               .appendQueryParameter("in_app", "true")
                                               .appendQueryParameter("api_key", Context.getApiKey())
                                               .appendQueryParameter("locale", defaultLocale())
                                               .appendQueryParameter("flow", "registration")
                                               .appendQueryParameter("public_key", "4444");

        if(Context.getButtonColor() != null) {
            builder = builder.appendQueryParameter("color_button", HelperMethods.hexStringFromColor(Context.getButtonColor()));
        }

        if(Context.getLinkColor() != null) {
            builder = builder.appendQueryParameter("color_link", HelperMethods.hexStringFromColor(Context.getLinkColor()));
        }

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

    static String preferencesUrl(String token) {
        Uri.Builder builder = new Uri.Builder().scheme("https")
                                               .authority(getAuthority())
                                               .appendPath("web")
                                               .appendPath("preferences")
                                               .appendQueryParameter("in_app", "true")
                                               .appendQueryParameter("user_token", token)
                                               .appendQueryParameter("api_key", Context.getApiKey())
                                               .appendQueryParameter("flow", "purchase")
                                               .appendQueryParameter("locale", defaultLocale());

        if(Context.getButtonColor() != null) {
            builder = builder.appendQueryParameter("color_button", HelperMethods.hexStringFromColor(Context.getButtonColor()));
        }

        if(Context.getLinkColor() != null) {
            builder = builder.appendQueryParameter("color_link", HelperMethods.hexStringFromColor(Context.getLinkColor()));
        }

        return builder.build().toString();
    }

    static String defaultLocale() {
        return Locale.getDefault().getLanguage();
    }
}

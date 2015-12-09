package com.klarna.ondemand;

import java.util.Locale;

/**
 * Manages the application-wide context for Klarna on Demand payments.
 */
public final class Context {
    private static String apiKey;

    /**
     * Sets the API key to use in following calls.
     * @param apiKey Merchant's public API key for this application.
     */
    public static void setApiKey(String apiKey){
        Context.apiKey = apiKey;
    }

    /**
     * Returns the API key set using {@link #setApiKey(String) setApiKey}.
     * @return Merchant's public API key for this application.
     */
    protected static String getApiKey(){
        if (apiKey == null) {
            throw new RuntimeException("You must set the API key first.");
        }
        return apiKey;
    }

    static String getLocale(android.content.Context context) {
        Locale locale = context.getResources().getConfiguration().locale;

        return locale.getLanguage();
    }
}

package com.klarna.ondemand;

/**
 * Manages the application-wide context for Klarna on Demand payments.
 */
public class Context {
    private static String apiKey;

    /**
     * Sets the API key to use in following calls.
     * @param apiKey Merchant's public API key for this application.
     */
    public static void setApiKey(String apiKey){
        Context.apiKey = apiKey;
    }

    /**
     * Returns the API key set using setApiKey:.
     * @return Merchant's public API key for this application.
     */
    protected static String getApiKey(){
        if (apiKey == null) {
            throw new RuntimeException("You must set the API key first.");
        }
        return apiKey;
    }
}

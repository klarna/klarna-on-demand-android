package com.klarna.ondemand;

/**
 * Klarna On Demand context
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
    static String getApiKey(){
        if (apiKey == null) {
            throw new RuntimeException("You must set the API key first.");
        }
        return apiKey;
    }
}

package com.klarna.ondemand;

public class Context {
    private static String apiKey;

    public static void setApiKey(String apiKey){
        Context.apiKey = apiKey;
    }

    protected static String getApiKey(){
        if (apiKey == null) {
            throw new RuntimeException("You must set the API key first.");
        }
        return apiKey;
    }
}

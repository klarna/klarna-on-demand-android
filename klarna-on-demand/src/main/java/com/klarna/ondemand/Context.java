package com.klarna.ondemand;

public class Context {
    private static String apiKey;

    public static void setApiKey(String apiKey){
        Context.apiKey = apiKey;
    }

    protected static String getApiKey(){
        return apiKey;
    }
}

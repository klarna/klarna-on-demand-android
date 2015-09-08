package com.klarna.ondemand;

/**
 * Manages the application-wide context for Klarna on Demand payments.
 */
public final class Context {
    private static String apiKey;
    private static Integer buttonColor;
    private static Integer linkColor;

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

    /**
     * Sets the button color for Klarna views in this application.
     * @param buttonColor Button color for Klarna views in this application.
     */
    public static void setButtonColor(Integer buttonColor) { Context.buttonColor = buttonColor; }

    /**
     * Returns the button color set using {@link #setButtonColor(Integer)}.
     * @return Button color for Klarna views in this application.
     */
    public static Integer getButtonColor() { return buttonColor; }

    /**
     * Sets the link color for Klarna views in this application.
     * @param linkColor Link color for Klarna views in this application.
     */
    public static void setLinkColor(Integer linkColor) { Context.linkColor = linkColor; }

    /**
     * Returns the link color set using {@link #setLinkColor(Integer)}.
     * @return Link color for Klarna views in this application.
     */
    public static Integer getLinkColor() { return linkColor; }

}

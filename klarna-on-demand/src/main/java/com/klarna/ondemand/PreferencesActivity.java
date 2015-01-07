package com.klarna.ondemand;

import junit.framework.Assert;

import java.util.Map;

/**
 * Responsible for showing and modifying a user's payment methods.
 */
public class PreferencesActivity extends WebViewActivity {

    /**
     * Token that uniquely identifies the user.
     * This extra item is required.
     */
    public static final String EXTRA_USER_TOKEN = "userToken";

    @Override
    protected String url() {
        String token = getIntent().getStringExtra(EXTRA_USER_TOKEN);
        if(token == null) {
            throw new IllegalStateException("EXTRA_USER_TOKEN is not set for preferences activity");
        }
        return UrlHelper.preferencesUrl(token);
    }

    @Override
    protected void handleUserReadyEventWithPayload(Map<Object, Object> payload) {
        webView.loadUrl(url());
    }
}

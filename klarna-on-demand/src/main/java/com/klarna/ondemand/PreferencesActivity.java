package com.klarna.ondemand;

import java.util.Map;

import android.os.Bundle;
import android.text.TextUtils;

/**
 * Responsible for showing and modifying a user's payment methods.
 */
public class PreferencesActivity extends WebViewActivity {

    /**
     * Token that uniquely identifies the user at Klarna.
     * This extra item is required.
     */
    public static final String EXTRA_USER_TOKEN = "userToken";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            getWebView().loadUrl(getUrl());
        } catch (Exception e) {
            this.finish();
        }
    }

    protected String getUrl() {
        String token = getIntent().getStringExtra(EXTRA_USER_TOKEN);

        if (TextUtils.isEmpty(token))
        //This also checks if the Token is not null but empty string for some reason.
        {
            throw new IllegalStateException("EXTRA_USER_TOKEN is not set for preferences activity");
        }
        return UrlHelper.preferencesUrl(getApplicationContext(), token);
    }

    @Override
    protected void handleUserReadyEvent(Map<Object, Object> payload) {
        getWebView().loadUrl(getUrl());
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected int homeButtonResultCode() {
        return RESULT_OK;
    }
}

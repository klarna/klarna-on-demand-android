package com.klarna.ondemand;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;


public class RegistrationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration);

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.clearCache(true);
        webView.loadUrl(KODUrl.registrationUrl());
    }

}

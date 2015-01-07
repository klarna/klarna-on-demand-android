package com.klarna.ondemand;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jockeyjs.Jockey;
import com.jockeyjs.JockeyHandler;
import com.jockeyjs.JockeyImpl;

import java.util.Map;

public abstract class WebViewActivity extends Activity {

    private ProgressDialog progressDialog;
    protected WebView webView;
    private WebViewClient webViewClient;
    private Jockey jockey;

    public static final int RESULT_ERROR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview);

        webView = (WebView) findViewById(R.id.webView);

        addSpinner();

        initializeActionBar();

        initializeWebView();

        registerJockeyEvents();

        webView.loadUrl(url());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showDismissAlert();
    }

    @Override
    protected void onDestroy() {
        jockey.off("userReady");
        jockey.off("userError");

        super.onDestroy();
    }

    protected abstract String url();

    protected abstract void handleUserReadyEventWithPayload(Map<Object, Object> payload);

    protected void handleUserErrorEvent() {
        setResult(RESULT_ERROR);
        finish();
    }

    private void initializeWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.clearCache(true);
        webView.setWebViewClient(webViewClient = new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressDialog.dismiss();
            }
        });
    }
    
    private void addSpinner() {
        progressDialog = new ProgressDialog(WebViewActivity.this);
        progressDialog.setMessage(getString(R.string.LOADING_SPINNER));
        progressDialog.show();
    }

    private void initializeActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void registerJockeyEvents() {
        jockey = JockeyImpl.getDefault();
        jockey.configure(webView);
        jockey.setWebViewClient(webViewClient);

        jockey.on("userReady", new JockeyHandler() {
            @Override
            protected void doPerform(Map<Object, Object> payload) {
                handleUserReadyEventWithPayload(payload);
            }
        });

        jockey.on("userError", new JockeyHandler() {
            @Override
            protected void doPerform(Map<Object, Object> payload) {
                handleUserErrorEvent();
            }
        });
    }

    private void showDismissAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(this.getTitle());
        builder.setMessage("Are you sure you want to exit?");
        builder.setCancelable(true);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}

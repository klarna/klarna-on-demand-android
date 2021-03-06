package com.klarna.ondemand;

import android.app.AlertDialog;
import android.content.*;
import android.os.Bundle;

import java.util.Map;

/**
 * Responsible for registering a new user and setting his Klarna payment method.
 */
public class RegistrationActivity extends WebViewActivity {

    /**
     * Extra item that is returned by the activity when the registration finishes.
     * This item uniquely identifies the user at Klarna.
     */
    private static final String PAYLOAD_USER_TOKEN = "userToken";
    public static final String EXTRA_USER_TOKEN = "userToken";
    public static final String EXTRA_SETTINGS = "settings";
    private RegistrationSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = (RegistrationSettings) getIntent().getSerializableExtra(EXTRA_SETTINGS);
        try {
            getWebView().loadUrl(getUrl());
        } catch (Exception e) {
            this.finish();
        }
    }

    protected String getUrl() {
        return UrlHelper.registrationUrl(getApplicationContext(), settings);
    }

    @Override
    protected void handleUserReadyEvent(Map<Object, Object> payload) {
        Intent result = new Intent();

        result.putExtra(EXTRA_USER_TOKEN, (String)payload.get(PAYLOAD_USER_TOKEN));

        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    public void onBackPressed() {
        showDismissAlert();
    }

    protected void showDismissAlert() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(this.getTitle());
        builder.setMessage(R.string.DISMISS_ALERT_MESSAGE);
        builder.setCancelable(true);

        builder.setPositiveButton(getString(R.string.DISMISS_ALERT_POSITIVE_BUTTON_TEXT), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });

        builder.setNegativeButton(getString(R.string.DISMISS_ALERT_NEGATIVE_BUTTON_TEXT), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected int homeButtonResultCode() {
        return RESULT_CANCELED;
    }
}

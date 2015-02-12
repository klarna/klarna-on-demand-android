package com.klarna.ondemand;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for registering a new user and setting his Klarna payment method.
 */
public class RegistrationActivity extends WebViewActivity {

    /**
     * Extra item that is returned by the activity when the registration finishes.
     * This item uniquely identifies the user at Klarna.
     */
    public static final String EXTRA_REGISTRATION_RESULT = "registrationResult";
    private static final String PAYLOAD_USER_TOKEN = "userToken";
    private static final String PAYLOAD_PHONE_NUMBER= "phone";
    private static final String PAYLOAD_USER_DETAILS = "userDetails";

    @Override
    protected String getUrl() {
        return UrlHelper.registrationUrl(getApplicationContext());
    }

    @Override
    protected void handleUserReadyEvent(Map<Object, Object> payload) {
        Intent result = new Intent();

        Map<?, ?> userDetails =  payload.containsKey(PAYLOAD_USER_DETAILS) ? (Map<?, ?>)payload.get(PAYLOAD_USER_DETAILS) : (Map<?, ?>)Collections.EMPTY_MAP;
        RegistrationResult registrationResult = new RegistrationResult(
                (String)payload.get(PAYLOAD_USER_TOKEN),
                (String)payload.get(PAYLOAD_PHONE_NUMBER),
                userDetails);

        result.putExtra(EXTRA_REGISTRATION_RESULT, registrationResult);

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

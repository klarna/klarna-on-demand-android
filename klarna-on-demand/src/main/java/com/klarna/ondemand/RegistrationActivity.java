package com.klarna.ondemand;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import java.util.Map;

/**
 * Responsible for registering a new user and setting his Klarna payment method.
 */
public class RegistrationActivity extends WebViewActivity {

    /**
     * Extra item that is returned by the activity when the registration finishes.
     * This item uniquely identifies the user at Klarna.
     */
    public static final String EXTRA_USER_TOKEN = "userToken";

    @Override
    protected String getUrl() {
        return UrlHelper.registrationUrl(getApplicationContext());
    }

    @Override
    protected void handleUserReadyEvent(Map<Object, Object> payload) {
        String token = (String) payload.get("userToken");
        Intent result = new Intent();
        result.putExtra(EXTRA_USER_TOKEN, token);
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

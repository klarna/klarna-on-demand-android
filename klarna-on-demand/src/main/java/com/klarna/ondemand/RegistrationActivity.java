package com.klarna.ondemand;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import java.util.Map;

/**
 * Responsible for registering a new user and setting his Klarna payment method.
 */
public class RegistrationActivity extends WebViewActivity {

    public static final String EXTRA_USER_TOKEN = "userToken";

    @Override
    protected String url() {
        return UrlHelper.registrationUrl();
    }

    @Override
    protected void handleUserReadyEventWithPayload(Map<Object, Object> payload) {
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

    @Override
    protected int homeButtonResultCode() {
        return RESULT_CANCELED;
    }
}

package com.klarna.ondemand;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import java.util.Map;

/**
 * Responsible for registering a new user and setting his Klarna payment method.
 */
public class RegistrationActivity extends WebViewActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Extra item that is returned by the activity when the registration finishes.
     * This item uniquely identifies the user at Klarna.
     */
    private static final String PAYLOAD_USER_TOKEN = "userToken";
    public static final String EXTRA_USER_TOKEN = "userToken";
    private static String myProfilePhoneNumber = null;

    public static final String EXTRA_SETTINGS = "settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeLoader();
    }

    void initializeLoader() {
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected String getUrl() {
        RegistrationSettings settings = (RegistrationSettings) getIntent().getSerializableExtra(EXTRA_SETTINGS);

        if(settings == null) {
            settings = new RegistrationSettingsBuilder().build();
        }

        if(settings.prefillPhoneNumber == null) {
            settings.prefillPhoneNumber = getDevicePhoneNumber();
        }

        return UrlHelper.registrationUrl(getApplicationContext(), settings);
    }

    String getDevicePhoneNumber() {
        String simCardPhoneNumber = getSimCardPhoneNumber();

        return (simCardPhoneNumber != null && !simCardPhoneNumber.isEmpty()) ? simCardPhoneNumber : myProfilePhoneNumber;
    }

    String getSimCardPhoneNumber() {
        TelephonyManager tMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        return tMgr == null ? null : tMgr.getLine1Number();
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

    //region LoaderManager.LoaderCallbacks<Cursor> implementation

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle arguments) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(
                        ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
                ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE + " = ?",
                new String[]{ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        cursor.moveToFirst();

        myProfilePhoneNumber = cursor.getString(ProfileQuery.NUMBER);

        getWebView().loadUrl(getUrl());
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        int NUMBER = 0;
    }

    //endregion
}

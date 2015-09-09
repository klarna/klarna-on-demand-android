package com.klarna.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.klarna.ondemand.OriginProof;
import com.klarna.ondemand.PreferencesActivity;
import com.klarna.ondemand.RegistrationActivity;
import com.klarna.ondemand.RegistrationSettings;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends Activity {

    public static final int REGISTRATION_REQUEST_CODE = 1;
    public static final int PREFERENCES_REQUEST_CODE = 2;
    private static final String USER_TOKEN_KEY = "userToken";

    private View registerTextView;
    private View changePaymentButton;
    private View qrCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        com.klarna.ondemand.Context.setApiKey("test_d8324b98-97ce-4974-88de-eaab2fdf4f14");
        com.klarna.ondemand.Context.setLinkColor(Color.rgb(31, 120, 200));
        com.klarna.ondemand.Context.setButtonColor(Color.GRAY);

        initializeUIElements();
        updateUIElements();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REGISTRATION_REQUEST_CODE) {
            switch (resultCode) {
                case RegistrationActivity.RESULT_OK:
                    // Extract the user token from the activity's extra data
                    String userToken = data.getStringExtra(RegistrationActivity.EXTRA_USER_TOKEN);

                    // Saves the user token so that we can identify the user in future calls
                    saveUserToken(userToken);
                    updateUIElements();

                    buyTicket();
                    break;
                case RegistrationActivity.RESULT_CANCELED:
                    break;
                case RegistrationActivity.RESULT_ERROR:
                    // You may want to convey this failure to your user
                    break;
                default:
                    break;
            }
        }
        else if(requestCode == PREFERENCES_REQUEST_CODE) {
            switch(resultCode) {
                case PreferencesActivity.RESULT_OK:
                    break;
                case PreferencesActivity.RESULT_ERROR:
                    // You may also want to convey this failure to your user
                    break;
                default:
                    break;
            }
        }
    }

    //region UI behaviours

    private void initializeUIElements() {
        registerTextView = findViewById(R.id.registerTextView);
        changePaymentButton = findViewById(R.id.changePaymentButton);
        qrCodeView = findViewById(R.id.qrCodeView);
    }

    private void updateUIElements() {
        registerTextView.setVisibility(hasUserToken() == false ? View.VISIBLE : View.INVISIBLE);
        changePaymentButton.setVisibility(hasUserToken() == true ? View.VISIBLE : View.INVISIBLE);
    }

    public void showQRCode() {
        qrCodeView.setVisibility(View.VISIBLE);
    }

    public void hideQRCode(View view) {
        qrCodeView.setVisibility(View.INVISIBLE);
    }

    private void showAlert(String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getTitle())
                .setMessage(message)
                .setPositiveButton(getString(R.string.alertDefaultPositionButtonText), null)
                .setCancelable(true)
                .create()
                .show();
    }

    //endregion

    //region Purchase using Klarna

    public void openKlarnaPreferences(View view) {
        // Start the preferences activity with the user token that was saved when the user completed the registration process
        Intent intent = new Intent(this, PreferencesActivity.class);
        intent.putExtra(PreferencesActivity.EXTRA_USER_TOKEN, getUserToken());
        startActivityForResult(intent, PREFERENCES_REQUEST_CODE);
    }

    public void onBuyPressed(View view) {
        // If a token has not been previously created
        if (this.hasUserToken()) {
            buyTicket();
        } else {
            // Open Klarna registration
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivityForResult(intent, REGISTRATION_REQUEST_CODE);
        }
    }

    private void buyTicket() {
        // Create an origin proof for the order
        OriginProof originProof = new OriginProof(9900, "SEK", getUserToken(), getApplicationContext());

        // Run a background thread to perform the purchase
        Thread thread = new Thread(new purchaseItemRunnable("TCKT0001", originProof));
        thread.start();
    }

    private void performPurchaseOfItem(String reference, OriginProof originProof) throws IOException, JSONException, HttpHostConnectException {
        // Create a post request to instruct the backend to perform the purchase.
        // For Genymotion devices, use the following path: http://10.0.3.2:9292/pay.
        // Remember that this expects to work with our sample backend: https://github.com/klarna/sample-ondemand-backend.
        HttpPost httpPost = new HttpPost("http://10.0.2.2:9292/pay");

        JSONObject jsonParams = new JSONObject();
        jsonParams.put("origin_proof", originProof.toString());
        jsonParams.put("reference", reference);
        jsonParams.put("user_token", getUserToken());

        StringEntity params = new StringEntity(jsonParams.toString());
        params.setContentType("application/json; charset=UTF-8");

        httpPost.setEntity(params);

        final HttpResponse response = new DefaultHttpClient().execute(httpPost);

        // Handle response on UI thread (main)
        runOnMainThread(new Runnable() {
            @Override
            public void run() {
                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode >= 200 && statusCode < 300) {
                    // Show QR Code for the movie
                    showQRCode();
                }
                else {
                    // Log the error and display it
                    Log.e(getClass().getName(), response.toString());
                    showAlert("Error: " + response.toString());
                }
            }});
    }

    //endregion

    //region User token persistence

    private void saveUserToken(String token) {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USER_TOKEN_KEY, token);
        editor.commit();
    }

    private String getUserToken() {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        String token = preferences.getString(USER_TOKEN_KEY, null);
        return token;
    }

    private boolean hasUserToken() {
        return getUserToken() != null;
    }

    //endregion

    //region purchaseItemRunnable class

    // Runnable command for performing a purchase in a background thread
    private class purchaseItemRunnable implements Runnable {
        String reference;
        OriginProof originProof;

        purchaseItemRunnable(String reference, OriginProof originProof) {
            this.reference = reference;
            this.originProof = originProof;
        }

        @Override
        public void run() {
            try {
                performPurchaseOfItem(reference, originProof);
            } catch (final Exception e) {
                runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        // Log the error and display it
                        Log.e(getClass().getName(), "Error in performPurchaseOfItem", e);
                        showAlert("Error: " + e.toString());
                    }
                });
            }
        }
    }

    //endregion

    //region helper methods

    private void runOnMainThread(Runnable runnable) {
        Handler mainHandler = new Handler(getApplicationContext().getMainLooper());
        mainHandler.post(runnable);
    }

    //endregion
}

package com.klarna.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.klarna.ondemand.OriginProof;
import com.klarna.ondemand.PreferencesActivity;
import com.klarna.ondemand.RegistrationActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends Activity {

    public static final int REGISTRATION_REQUEST_CODE = 1;
    public static final int PREFERENCES_REQUEST_CODE = 2;
    private static final String USER_TOKEN_KEY = "userToken";
    
    private View registerTextView;
    private View changePaymentButton;
    private View buyButton;
    private View qrCodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        
        com.klarna.ondemand.Context.setApiKey("test_d8324b98-97ce-4974-88de-eaab2fdf4f14");


        initializeUIElements();
        updateUIElements();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REGISTRATION_REQUEST_CODE) {
            switch (resultCode) {
                case RegistrationActivity.RESULT_OK:
                    String token = data.getStringExtra(RegistrationActivity.EXTRA_USER_TOKEN);
                    saveUserToken(token);
                    updateUIElements();
                    break;
                case RegistrationActivity.RESULT_CANCELED:
                    break;
                case RegistrationActivity.RESULT_ERROR:
                    // You may want to convey this failure to your user.
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
                    // You may also want to convey this failure to your user.
                    break;
                default:
                    break;
            }
        }
    }
    
    //region UI behaviours

    private void initializeUIElements() {
        registerTextView = findViewById(R.id.registerTextView);
        buyButton = findViewById(R.id.buyButton);
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

        builder.setTitle(this.getTitle());
        builder.setMessage(message);
        builder.setPositiveButton("OK",null);

        AlertDialog alert = builder.create();
        alert.show();
    }
    
    //endregion
    
    //region Purchase using Klarna

    public void openKlarnaRegistration() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivityForResult(intent, REGISTRATION_REQUEST_CODE);
    }

    public void openKlarnaPreferences(View view) {
        Intent intent = new Intent(this, PreferencesActivity.class);
        intent.putExtra(PreferencesActivity.EXTRA_USER_TOKEN, getUserToken());
        startActivityForResult(intent, PREFERENCES_REQUEST_CODE);
    }

    public void onBuyPressed(View view) {
        if (!this.hasUserToken()) {
            openKlarnaRegistration();
        }
        
        OriginProof originProof = new OriginProof(3600, "SEK", getUserToken(), getApplicationContext());

        class purchaseItemRunnable implements Runnable {
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
                    Handler mainHandler = new Handler(getApplicationContext().getMainLooper());

                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            showAlert("Error: " + e.toString());
                            Log.e(getClass().getName(), "Error on performPurchaseOfItem", e);
                        }
                    });
                }
            }
        }

        Thread thread = new Thread(new purchaseItemRunnable("TCKT0001", originProof));
        thread.start();
    }

    private void performPurchaseOfItem(String reference, OriginProof originProof) throws IOException, JSONException {
        HttpClient httpClient = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost("http://192.168.56.1:9292/pay");

        JSONObject jsonParams = new JSONObject();
        jsonParams.put("originProof", originProof.toString());
        jsonParams.put("reference", reference);
        jsonParams.put("user_token", getUserToken());
        
        StringEntity params = new StringEntity(jsonParams.toString());
        params.setContentType("application/json;charset=UTF-8");
        params.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));

        httpPost.setEntity(params);

        final HttpResponse response = httpClient.execute(httpPost);
        
        Handler mainHandler = new Handler(getApplicationContext().getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode >= 200 && statusCode < 300) {
                     showQRCode();
                }
                else {
                     showAlert("Error: " + response.toString());
                     Log.e(getClass().getName(), response.toString());
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
}

package com.klarna.example;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import com.klarna.ondemand.PreferencesActivity;
import com.klarna.ondemand.RegistrationActivity;

public class MainActivity extends Activity {

    public static final int REGISTRATION_REQUEST_CODE = 1;
    public static final int PREFERENCES_REQUEST_CODE = 2;
    private static final String USER_TOKEN_KEY = "userToken";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        com.klarna.ondemand.Context.setApiKey("test_d8324b98-97ce-4974-88de-eaab2fdf4f14");

        initializeUIElements();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REGISTRATION_REQUEST_CODE) {
            switch (resultCode) {
                case RegistrationActivity.RESULT_OK:
                    String token = data.getStringExtra(RegistrationActivity.EXTRA_USER_TOKEN);
                    saveUserToken(token);
                    findViewById(R.id.registerButton).setVisibility(View.INVISIBLE);
                    findViewById(R.id.preferencesButton).setVisibility(View.VISIBLE);
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

    private void initializeUIElements() {
        findViewById(R.id.registerButton).setVisibility(hasUserToken() == false ? View.VISIBLE : View.INVISIBLE);
        findViewById(R.id.preferencesButton).setVisibility(hasUserToken() == true ? View.VISIBLE : View.INVISIBLE);
    }

    public void openKlarnaRegistration(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivityForResult(intent, REGISTRATION_REQUEST_CODE);
    }

    public void openKlarnaPreferences(View view) {
        Intent intent = new Intent(this, PreferencesActivity.class);
        intent.putExtra(PreferencesActivity.EXTRA_USER_TOKEN, getUserToken());
        startActivityForResult(intent, PREFERENCES_REQUEST_CODE);

    }

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
}

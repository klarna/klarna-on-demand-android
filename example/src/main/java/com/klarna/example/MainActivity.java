package com.klarna.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.klarna.ondemand.Context;
import com.klarna.ondemand.RegistrationActivity;


public class MainActivity extends Activity {

    public final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Context.setApiKey("skadoo");
    }

    public void openKlarnaRegistration(View view) {
        RegistrationActivity registrationWebView = new RegistrationActivity();

        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            //TODO: handle resultCode.

        }
    }
}

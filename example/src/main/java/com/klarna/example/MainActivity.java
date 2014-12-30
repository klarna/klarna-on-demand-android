package com.klarna.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.klarna.ondemand.Context;
import com.klarna.ondemand.RegistrationActivity;
import com.klarna.ondemand.WebViewActivity;


public class MainActivity extends Activity {

    public final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Context.setApiKey("test_d8324b98-97ce-4974-88de-eaab2fdf4f14");
    }

    public void openKlarnaRegistration(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case WebViewActivity.RESULT_CANCELED:
                    break;
                default:
                    break;
            }


        }
    }
}

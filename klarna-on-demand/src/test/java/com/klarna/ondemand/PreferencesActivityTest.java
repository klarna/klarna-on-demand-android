package com.klarna.ondemand;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.tester.android.view.TestMenuItem;

import java.util.HashMap;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
public class PreferencesActivityTest {

    private  PreferencesActivity preferencesActivity;

    @Before
    public void beforeEach() {
        Context.setApiKey("test_skadoo");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra(preferencesActivity.EXTRA_USER_TOKEN, "my_token");
        preferencesActivity = Mockito.spy(Robolectric.buildActivity(PreferencesActivity.class).withIntent(intent).create().get());
    }

    @After
    public void afterEach() {
        Context.setApiKey(null);
    }

    @Test
    public void handleUserErrorEvent_ShouldCallFinishWithRESULTERROR() {
        preferencesActivity.handleUserErrorEvent();

        Mockito.verify(preferencesActivity).setResult(eq(RegistrationActivity.RESULT_ERROR));
        Mockito.verify(preferencesActivity).finish();
    }

    @Test
    public void homeButtonPress_ShouldCallFinishWithRESULTCANCELED() {
        MenuItem item = new TestMenuItem() {
            public int getItemId() {
                return android.R.id.home;
            }
        };
        preferencesActivity.onOptionsItemSelected(item);

        Mockito.verify(preferencesActivity).setResult(eq(RegistrationActivity.RESULT_OK));
        Mockito.verify(preferencesActivity).finish();
    }


}

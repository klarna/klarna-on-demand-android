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
import org.robolectric.util.ActivityController;

import java.util.HashMap;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
public class RegistrationActivityTest {

    private ActivityController<RegistrationActivity> registrationActivityController;
    RegistrationActivity registrationActivity;


    @Before
    public void beforeEach() {
        Context.setApiKey("test_skadoo");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        registrationActivityController = Robolectric.buildActivity(RegistrationActivity.class).withIntent(intent);
        registrationActivity = spy(registrationActivityController.get());
    }

    @After
    public void afterEach() {
        Context.setApiKey(null);
    }

    @Test
    public void handleUserReadyEventWithPayload_ShouldCallFinishWithRESULTOK_WhenATokenIsReceived() {
        registrationActivity.handleUserReadyEventWithPayload(new HashMap<Object,Object>(){{ put("userToken", "my_token"); }});

        Mockito.verify(registrationActivity).setResult(eq(Activity.RESULT_OK), any(Intent.class));
        Mockito.verify(registrationActivity).finish();
    }

    @Test
    public void handleUserErrorEvent_ShouldCallFinishWithRESULTERROR() {
        registrationActivity.handleUserErrorEvent();

        Mockito.verify(registrationActivity).setResult(eq(RegistrationActivity.RESULT_ERROR));
        Mockito.verify(registrationActivity).finish();
    }

    @Test
    public void homeButtonPress_ShouldCallFinishWithRESULTCANCELED() {
        MenuItem item = new TestMenuItem() {
            public int getItemId() {
                return android.R.id.home;
            }
        };
        registrationActivity.onOptionsItemSelected(item);

        Mockito.verify(registrationActivity).setResult(eq(RegistrationActivity.RESULT_CANCELED));
        Mockito.verify(registrationActivity).finish();
    }
}

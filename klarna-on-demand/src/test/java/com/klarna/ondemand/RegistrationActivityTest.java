package com.klarna.ondemand;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;

import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.tester.android.view.TestMenuItem;
import org.robolectric.util.ActivityController;

import java.util.HashMap;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
@PrepareForTest(Context.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
public class RegistrationActivityTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    private ActivityController<RegistrationActivity> registrationActivityController;

    @Before
    public void beforeEach() {
        PowerMockito.mockStatic(Context.class);
        Mockito.when(Context.getApiKey()).thenReturn("test_skadoo");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        registrationActivityController = Robolectric.buildActivity(RegistrationActivity.class).withIntent(intent).create();
    }

    @Test
    public void handleUserReadyEventWithPayload_ShouldCallFinishWithRESULTOK_WhenATokenIsReceived() {
        RegistrationActivity registrationActivity = Mockito.spy(registrationActivityController.get());

        registrationActivity.handleUserReadyEventWithPayload(new HashMap<Object,Object>(){{ put("userToken", "my_token"); }});

        Mockito.verify(registrationActivity).setResult(eq(Activity.RESULT_OK), any(Intent.class));
        Mockito.verify(registrationActivity).finish();
    }

    @Test
    public void handleUserErrorEvent_ShouldCallFinishWithRESULTERROR() {
        RegistrationActivity registrationActivity = Mockito.spy(registrationActivityController.get());

        registrationActivity.handleUserErrorEvent();

        Mockito.verify(registrationActivity).setResult(RegistrationActivity.RESULT_ERROR);
        Mockito.verify(registrationActivity).finish();
    }

    @Test
    public void homeButtonPress_ShouldCallFinishWithRESULTCANCELED() {
        RegistrationActivity registrationActivity = Mockito.spy(registrationActivityController.get());

        MenuItem item = new TestMenuItem() {
            public int getItemId() {
                return android.R.id.home;
            }
        };
        registrationActivity.onOptionsItemSelected(item);

        Mockito.verify(registrationActivity).setResult(RegistrationActivity.RESULT_CANCELED);
        Mockito.verify(registrationActivity).finish();
    }
}

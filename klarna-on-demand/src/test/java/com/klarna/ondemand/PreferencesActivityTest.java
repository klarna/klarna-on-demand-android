package com.klarna.ondemand;

import android.content.Intent;
import android.view.MenuItem;
import android.webkit.WebView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Mockito;
import org.mockito.configuration.DefaultMockitoConfiguration;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;

import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.tester.android.view.TestMenuItem;
import org.robolectric.util.ActivityController;


@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
@PrepareForTest(Context.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
public class PreferencesActivityTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    private PreferencesActivity preferencesActivity;
    private ActivityController<PreferencesActivity> preferencesActivityController;

    @Before
    public void beforeEach() {
        PowerMockito.mockStatic(Context.class);
        Mockito.when(Context.getApiKey()).thenReturn("test_skadoo");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra(preferencesActivity.EXTRA_USER_TOKEN, "my_token");
        preferencesActivityController = Robolectric.buildActivity(PreferencesActivity.class).withIntent(intent).create();
    }

    @Test
    public void handleUserErrorEvent_ShouldCallFinishWithRESULTERROR() {
        PreferencesActivity preferencesActivity = Mockito.spy(preferencesActivityController.get());

        preferencesActivity.handleUserErrorEvent();

        Mockito.verify(preferencesActivity).setResult(RegistrationActivity.RESULT_ERROR);
        Mockito.verify(preferencesActivity).finish();
    }

    @Test
    public void handleUserReadyEventWithPayload_ShouldLoadPreferencesUrl() {
        WebView webView = Mockito.mock(WebView.class);
        PreferencesActivity preferencesActivity = Mockito.spy(preferencesActivityController.get());
        Mockito.when(preferencesActivity.getWebView()).thenReturn(webView);

        preferencesActivity.handleUserReadyEventWithPayload(null);

        Mockito.verify(webView).loadUrl(UrlHelper.preferencesUrl("my_token"));
    }

    @Test
    public void homeButtonPress_ShouldCallFinishWithRESULTCANCELED() {
        PreferencesActivity preferencesActivity = Mockito.spy(preferencesActivityController.get());

        MenuItem item = new TestMenuItem() {
            public int getItemId() {
                return android.R.id.home;
            }
        };
        preferencesActivity.onOptionsItemSelected(item);

        Mockito.verify(preferencesActivity).setResult(RegistrationActivity.RESULT_OK);
        Mockito.verify(preferencesActivity).finish();
    }

    @Test
    public void backButtonPress_ShouldCallFinishWithRESULTOK() {
        PreferencesActivity preferencesActivity = Mockito.spy(preferencesActivityController.get());

        preferencesActivity.onBackPressed();

        Mockito.verify(preferencesActivity).setResult(RegistrationActivity.RESULT_OK);
        Mockito.verify(preferencesActivity).finish();
    }
}

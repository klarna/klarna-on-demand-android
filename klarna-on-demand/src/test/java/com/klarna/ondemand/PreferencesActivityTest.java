package com.klarna.ondemand;

import android.content.Intent;
import android.view.MenuItem;
import android.webkit.WebView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.tester.android.view.TestMenuItem;
import org.robolectric.util.ActivityController;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
@PrepareForTest(Context.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
public class PreferencesActivityTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    private ActivityController<PreferencesActivity> preferencesActivityController;
    private PreferencesActivity preferencesActivity;

    @Before
    public void beforeEach() {
        PowerMockito.mockStatic(Context.class);
        when(Context.getApiKey()).thenReturn("test_skadoo");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra(preferencesActivity.EXTRA_USER_TOKEN, "my_token");
        preferencesActivityController = Robolectric.buildActivity(PreferencesActivity.class).withIntent(intent).create();
        preferencesActivity = spy(preferencesActivityController.get());
    }

    @Test
    public void handleUserErrorEvent_ShouldCallFinishWithResultError() {
        preferencesActivity.handleUserErrorEvent();

        verify(preferencesActivity).setResult(RegistrationActivity.RESULT_ERROR);
        verify(preferencesActivity).finish();
    }

    @Test
    public void handleUserReadyEvent_ShouldLoadPreferencesUrl() {
        WebView webView = mock(WebView.class);
        when(preferencesActivity.getWebView()).thenReturn(webView);

        preferencesActivity.handleUserReadyEvent(null);

        verify(webView).loadUrl(UrlHelper.preferencesUrl("my_token"));
    }

    @Test
    public void homeButtonPress_ShouldCallFinishWithResultCanceled() {
        MenuItem item = new TestMenuItem() {
            public int getItemId() {
                return android.R.id.home;
            }
        };
        preferencesActivity.onOptionsItemSelected(item);

        verify(preferencesActivity).setResult(RegistrationActivity.RESULT_OK);
        verify(preferencesActivity).finish();
    }

    @Test
    public void backButtonPress_ShouldCallFinishWithResultOK() {
        preferencesActivity.onBackPressed();

        verify(preferencesActivity).setResult(RegistrationActivity.RESULT_OK);
        verify(preferencesActivity).finish();
    }
}

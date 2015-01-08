package com.klarna.ondemand;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.robolectric.annotation.Config;

@RunWith(PowerMockRunner.class)
@Config(emulateSdk = 18)
public class ContextTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void beforeEach() throws java.lang.Exception {
        Context.setApiKey(null);
    }

    @Test
    public void setApiKey(){
        String apiKey = "my_key";
        Context.setApiKey(apiKey);
        Assert.assertEquals(Context.getApiKey(), apiKey);
    }

    @Test
    public void getApiKeyShouldThrowExceptionWhenThereIsNoApiKey() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("You must set the API key first.");

        Context.getApiKey();
    }
}
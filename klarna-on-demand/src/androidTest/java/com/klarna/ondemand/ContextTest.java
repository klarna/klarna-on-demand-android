package com.klarna.ondemand;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.RuntimeException;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class ContextTest {

    @Before
    public void setUp() throws java.lang.Exception {
        Context.setApiKey(null);
    }

    @Test
    public void testSetApiKey(){
        String apiKey = "my_key";
        Context.setApiKey(apiKey);
        Assert.assertEquals(Context.getApiKey(), apiKey);
    }

    @Test
    public void testGetApiKeyShouldThrowExceptionWhenThereIsNoApiKey() {
        try {
            Context.getApiKey();
            Assert.fail("Exception missing");
        }
        catch(RuntimeException e) {
            Assert.assertEquals(e.getMessage(), "You must set the API key first.");
        }
    }
}

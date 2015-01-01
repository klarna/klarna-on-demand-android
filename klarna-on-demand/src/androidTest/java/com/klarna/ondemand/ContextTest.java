package com.klarna.ondemand;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.RuntimeException;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
public class ContextTest{

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void before(){
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

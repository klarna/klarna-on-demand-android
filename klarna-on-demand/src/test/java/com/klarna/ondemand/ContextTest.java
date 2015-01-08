package com.klarna.ondemand;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.RuntimeException;

public class ContextTest {

    @Before
    public void setUp() throws java.lang.Exception {
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
        try {
            Context.getApiKey();
            Assert.fail("Exception missing");
        }
        catch(RuntimeException e) {
            Assert.assertEquals(e.getMessage(), "You must set the API key first.");
        }
    }
}
package com.klarna.ondemand;

import junit.framework.TestCase;

import java.lang.RuntimeException;

public class ContextTest extends TestCase {

    protected void setUp() throws java.lang.Exception {
        Context.setApiKey(null);
    }

    public void testSetApiKey(){
        String apiKey = "my_key";
        Context.setApiKey(apiKey);
        assertEquals(Context.getApiKey(), apiKey);
    }

    public void testGetApiKeyShouldThrowExceptionWhenThereIsNoApiKey() {
        try {
            Context.getApiKey();
            fail("Exception missing");
        }
        catch(RuntimeException e) {
            assertEquals(e.getMessage(), "You must set the API key first.");
        }
    }
}
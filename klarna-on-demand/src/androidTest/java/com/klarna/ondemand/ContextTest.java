package com.klarna.ondemand;

import junit.framework.TestCase;


public class ContextTest extends TestCase {

    protected void setUp() throws java.lang.Exception {
        Context.setApiKey(null);
    }

    public void testSetApiKey(){
        String apiKey = "my_key";
        Context.setApiKey(apiKey);
        assertEquals(Context.getApiKey(), apiKey);
    }
}

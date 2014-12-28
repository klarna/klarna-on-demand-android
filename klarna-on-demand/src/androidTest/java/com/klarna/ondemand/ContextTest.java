package com.klarna.ondemand;

import junit.framework.TestCase;


public class ContextTest extends TestCase {

    public void setUp(){
        Context.setApiKey(null);
    }

    public void testSetApiKey(){
        String apiKey = "my_key";
        Context.setApiKey(apiKey);
        assertEquals(Context.getApiKey(), apiKey);
    }
}

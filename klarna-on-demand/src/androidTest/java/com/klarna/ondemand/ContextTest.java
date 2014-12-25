package com.klarna.ondemand;

import junit.framework.TestCase;

public class ContextTest extends TestCase {

    public void testSetApiKey(){
        String apiKey = "myKey";
        Context.setApiKey(apiKey);
        assertEquals(Context.getApiKey(), apiKey);
    }
}

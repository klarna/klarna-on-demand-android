package com.klarna.ondemand;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

public class ContextTest {

    @Before
    public void beforeEach(){
        Context.setApiKey(null);
    }

    @Test
    public void setApiKey(){
        String apiKey = "my_key";
        Context.setApiKey(apiKey);
        assertEquals(Context.getApiKey(), apiKey);
    }

    @Test(expected = RuntimeException.class)
    public void getApiKeyThrowsRuntimeExceptionWhenApiKeyNotSet(){
        Context.getApiKey();
    }
}

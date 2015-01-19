package com.klarna.ondemand;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ContextTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void setApiKey_ShouldReturnThePreviouslySetApiKey() {
        String apiKey = "my_key";
        Context.setApiKey(apiKey);
        Assert.assertEquals(Context.getApiKey(), apiKey);
        Context.setApiKey(null);
    }

    @Test
    public void getApiKey_ShouldThrowExceptionWhenThereIsNoApiKey() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("You must set the API key first.");

        Context.getApiKey();
    }
}
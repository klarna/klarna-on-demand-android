package com.klarna.ondemand;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

public class ContextTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @After
    public void afterEach() {
        Context.setApiKey(null);
    }

    @Test
    public void setApiKey_ShouldReturnThePreviouslySetApiKey() {
        String apiKey = "my_key";
        Context.setApiKey(apiKey);

        assertThat(Context.getApiKey()).isEqualTo(apiKey);
    }

    @Test
    public void getApiKey_ShouldThrowExceptionWhenThereIsNoApiKey() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("You must set the API key first.");

        Context.getApiKey();
    }
}
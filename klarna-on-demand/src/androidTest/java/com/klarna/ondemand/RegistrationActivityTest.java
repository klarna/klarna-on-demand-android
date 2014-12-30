package com.klarna.ondemand;

import junit.framework.TestCase;

public class RegistrationActivityTest extends TestCase  {

    RegistrationActivity registrationActivity = new RegistrationActivity();

    protected void setUp() throws java.lang.Exception {
        Context.setApiKey("test_skadoo");
    }
}

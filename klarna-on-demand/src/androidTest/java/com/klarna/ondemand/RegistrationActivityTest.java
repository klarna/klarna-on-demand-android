package com.klarna.ondemand;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class RegistrationActivityTest {

    RegistrationActivity registrationActivity = new RegistrationActivity();

    @Before
    public void setUp() throws java.lang.Exception {
        Context.setApiKey("test_skadoo");
    }
}

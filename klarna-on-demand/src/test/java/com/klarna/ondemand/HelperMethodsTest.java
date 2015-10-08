package com.klarna.ondemand;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HelperMethodsTest {

    //region .isBlank

    @Test
    public void isBlank_shouldReturnTrueIfStringIsNull() {
        assertThat(HelperMethods.isBlank(null)).isTrue();
    }

    @Test
    public void isBlank_shouldReturnTrueIfStringIsEmpty() {
        assertThat(HelperMethods.isBlank("")).isTrue();
    }

    @Test
    public void isBlank_shouldReturnFalseIfStringHasContent() {
        assertThat(HelperMethods.isBlank("a")).isFalse();
    }

    //endregion
}
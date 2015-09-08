package com.klarna.ondemand;

import android.graphics.Color;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
@PowerMockIgnore({ "android.*" })
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

    //region .hexStringFromColor

    @Test
    public void hexStringFromColor_shouldIgnoreTheAlphaChannel() {
        assertThat(HelperMethods.hexStringFromColor(Color.BLACK).equals("#000000"));
        assertThat(HelperMethods.hexStringFromColor(Color.WHITE).equals("#FFFFFF"));
        assertThat(HelperMethods.hexStringFromColor(Color.rgb(78,255,161)).equals("#4EFFA1"));
    }

    @Test
    public void hexStringFromColor_shouldReturnAHexStringForValidColors() {
        assertThat(HelperMethods.hexStringFromColor(Color.argb(256, 0, 256, 0)).equals("#FF00FF"));
        assertThat(HelperMethods.hexStringFromColor(Color.argb(256, 0, 256, 128)).equals("#FF00FF"));
    }

    @Test
    public void hexStringFromColor_shouldReturnNullWhenColorIsNull() {
        assertThat(HelperMethods.hexStringFromColor(null) == null);
    }

    //endregion
}
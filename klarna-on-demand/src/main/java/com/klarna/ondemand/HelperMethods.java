package com.klarna.ondemand;

import android.os.Build;

class HelperMethods {

    static Boolean isBlank(String str) {
        return str == null || str.isEmpty();
    }

    static Boolean isVersionSmallerThenMarshmallow() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
    }
}

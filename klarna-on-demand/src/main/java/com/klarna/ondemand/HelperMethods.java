package com.klarna.ondemand;

class HelperMethods {

    static Boolean isBlank(String str) {
        return str == null || str.isEmpty();
    }

    static String hexStringFromColor(Integer color) {
        if(color == null) {
            return null;
        }

        return String.format("#%06X", (0xFFFFFF & color));
    }
}

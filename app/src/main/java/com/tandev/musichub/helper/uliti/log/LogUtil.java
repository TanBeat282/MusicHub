package com.tandev.musichub.helper.uliti.log;

import android.util.Log;

public class LogUtil {

    // Method to log debug messages
    public static void d(String tag, String message) {
        Log.d(tag, message);
    }

    // Method to log info messages
    public static void i(String tag, String message) {
        Log.i(tag, message);
    }

    // Method to log error messages
    public static void e(String tag, String message) {
        Log.e(tag, message);
    }

    // Method to log verbose messages
    public static void v(String tag, String message) {
        Log.v(tag, message);
    }

    // Method to log warning messages
    public static void w(String tag, String message) {
        Log.w(tag, message);
    }

    // Method to log assert messages
    public static void wtf(String tag, String message) {
        Log.wtf(tag, message);
    }
}

package me.dlet.rxupdatecheckerlib;

import android.util.Log;

/**
 * Created by darwinlouistoledo on 11/8/16.
 */

class Logger {
    public static final String LOG_TAG="RxUpdateChecker";

    public static void d(String msg, boolean shouldShow){
        if (shouldShow)
            Log.d(LOG_TAG, msg);
    }

    public static void e(String msg, boolean shouldShow){
        if (shouldShow)
            Log.e(LOG_TAG, msg);
    }
}

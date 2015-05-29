package com.cmg.android.bbcaccent.utils;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.fabric.sdk.android.Fabric;

/**
 * Created by luhonghai on 2/28/15.
 * Simple log
 */
public class SimpleAppLog {

    private static final String TAG = "BBC accent";

    public static void info(String log) {
        //Log.i(TAG, log);
        if (Fabric.isInitialized())
            Crashlytics.log(Log.INFO, TAG, log);
    }

    public static void logJson(Object obj) {
        if (obj ==  null) return;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        info(gson.toJson(obj));
    }

    public static void debug(String log) {
        Log.d(TAG, log);
    }

    public static void error(String log) {
        error(log, null);
    }

    public static void error(String log, Throwable throwable) {
        if (Fabric.isInitialized())
            Crashlytics.log(Log.ERROR, TAG, log);
        if (throwable == null) {
            Log.e(TAG, log);
        } else {
            Log.e(TAG, log, throwable);
            throwable.printStackTrace();
        }
    }
}

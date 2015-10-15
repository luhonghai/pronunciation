package com.cmg.android.bbcaccent;

import android.app.Application;

/**
 * Created by luhonghai on 15/10/2015.
 */
public class MainApplication extends Application {

    private static MainApplication context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static MainApplication getContext() {
        return context;
    }
}

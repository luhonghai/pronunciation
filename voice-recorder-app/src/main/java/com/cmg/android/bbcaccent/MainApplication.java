package com.cmg.android.bbcaccent;

import android.app.Application;

import com.cmg.android.bbcaccent.data.sqlite.FreestyleDatabaseHelper;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;

/**
 * Created by luhonghai on 09/10/2015.
 */
public class MainApplication  extends Application {

    private static MainApplication context;

    private FreestyleDatabaseHelper phonemeDatabaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        try {
            phonemeDatabaseHelper = new FreestyleDatabaseHelper();
        } catch (Exception e) {
            SimpleAppLog.error("Could not init database",e);
        }
        phonemeDatabaseHelper.open();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        phonemeDatabaseHelper.close();
    }

    public static MainApplication getContext() {
        return context;
    }

    public FreestyleDatabaseHelper getFreestyleDatabaseHelper() {
        return phonemeDatabaseHelper;
    }

}

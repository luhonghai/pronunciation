package com.cmg.android.bbcaccent;

import android.app.Application;

import com.cmg.android.bbcaccent.data.sqlite.FreestyleDatabaseHelper;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;

/**
 * Created by luhonghai on 09/10/2015.
 */
public class MainApplication  extends Application {

    private static MainApplication context;

    private FreestyleDatabaseHelper freestyleDatabaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        try {
            freestyleDatabaseHelper = new FreestyleDatabaseHelper();
            freestyleDatabaseHelper.open();
        } catch (Exception e) {
            SimpleAppLog.error("Could not init database",e);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (freestyleDatabaseHelper != null)
            freestyleDatabaseHelper.close();
    }

    public static MainApplication getContext() {
        return context;
    }

    public FreestyleDatabaseHelper getFreestyleDatabaseHelper() {
        return freestyleDatabaseHelper;
    }

}

package com.cmg.android.bbcaccent;

import android.app.Application;

import com.cmg.android.bbcaccent.data.sqlite.FreestyleDatabaseHelper;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.data.sqlite.LessonDatabaseHelper;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by luhonghai on 09/10/2015.
 */
public class MainApplication  extends Application {

    private static MainApplication context;

    private FreestyleDatabaseHelper freestyleDatabaseHelper;

    private LessonDatabaseHelper lessonDatabaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        MainBroadcaster.getInstance().init();
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
    }

    public void initDatabase() {
        if (freestyleDatabaseHelper == null) {
            try {
                freestyleDatabaseHelper = new FreestyleDatabaseHelper();
                freestyleDatabaseHelper.open();
            } catch (Exception e) {
                SimpleAppLog.error("Could not init freestyle database", e);
            }
        }
        if (lessonDatabaseHelper == null) {
            try {
                lessonDatabaseHelper = new LessonDatabaseHelper();
                lessonDatabaseHelper.open();
            } catch (Exception e) {
                SimpleAppLog.error("Could not init lesson database", e);
            }
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (freestyleDatabaseHelper != null)
            freestyleDatabaseHelper.close();
        if (lessonDatabaseHelper != null)
            lessonDatabaseHelper.close();
        MainBroadcaster.getInstance().destroy();
        ImageLoader.getInstance().destroy();
    }

    public static MainApplication getContext() {
        return context;
    }

    public FreestyleDatabaseHelper getFreestyleDatabaseHelper() {
        return freestyleDatabaseHelper;
    }

    public LessonDatabaseHelper getLessonDatabaseHelper() {
        return lessonDatabaseHelper;
    }
}

package com.cmg.android.bbcaccent;

import android.app.Application;

import com.cmg.android.bbcaccent.data.sqlite.FreestyleDatabaseHelper;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.data.sqlite.LessonDatabaseHelper;
import com.cmg.android.bbcaccent.data.sqlite.LessonHistoryDatabaseHelper;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by luhonghai on 09/10/2015.
 */
public class MainApplication  extends Application {

    private static MainApplication context;

    private FreestyleDatabaseHelper freestyleDatabaseHelper;

    private LessonDatabaseHelper lessonDatabaseHelper;

    private LessonHistoryDatabaseHelper lessonHistoryDatabaseHelper;

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

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
        if (lessonHistoryDatabaseHelper == null) {
            try {
                lessonHistoryDatabaseHelper = new LessonHistoryDatabaseHelper();
                lessonHistoryDatabaseHelper.open();
            } catch (Exception e) {
                SimpleAppLog.error("Could not init lesson history database",e);
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
        if (lessonHistoryDatabaseHelper != null) {
            lessonHistoryDatabaseHelper.close();
        }
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

    public LessonHistoryDatabaseHelper getLessonHistoryDatabaseHelper() {
        return lessonHistoryDatabaseHelper;
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static <T extends Object> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
}

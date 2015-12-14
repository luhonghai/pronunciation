package com.cmg.android.bbcaccent;

import android.app.Application;
import android.content.Intent;

import com.cmg.android.bbcaccent.data.sqlite.FreestyleDatabaseHelper;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.data.sqlite.LessonDatabaseHelper;
import com.cmg.android.bbcaccent.data.sqlite.LessonHistoryDatabaseHelper;
import com.cmg.android.bbcaccent.extra.BreakDownAction;
import com.cmg.android.bbcaccent.fragment.lesson.LessonFragment;
import com.cmg.android.bbcaccent.service.UpdateDataService;
import com.cmg.android.bbcaccent.utils.GcmUtil;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.lang.reflect.Type;

/**
 * Created by luhonghai on 09/10/2015.
 */
public class MainApplication  extends Application {

    private static MainApplication context;

    private FreestyleDatabaseHelper freestyleDatabaseHelper;

    private LessonDatabaseHelper lessonDatabaseHelper;

    private LessonHistoryDatabaseHelper lessonHistoryDatabaseHelper;

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private BreakDownAction lastBreakDownAction;

    public static boolean enablePopbackFragmentAnimation = true;

    private LessonFragment.ViewState lessonViewState;

    private String selectedWord;

    private boolean skipHelpPopup;

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
        startService(new Intent(this, UpdateDataService.class));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        closeAllDatabase();
        MainBroadcaster.getInstance().destroy();
        ImageLoader.getInstance().destroy();
        GcmUtil.getInstance(this).recycle();
    }

    public void closeAllDatabase() {
        if (freestyleDatabaseHelper != null) {
            freestyleDatabaseHelper.close();
            freestyleDatabaseHelper = null;
        }
        if (lessonDatabaseHelper != null) {
            lessonDatabaseHelper.close();
            lessonDatabaseHelper = null;
        }
        if (lessonHistoryDatabaseHelper != null) {
            lessonHistoryDatabaseHelper.close();
            lessonHistoryDatabaseHelper = null;
        }
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

    public static <T extends Object> T fromJson(String json, Type type) {
        return gson.fromJson(json, type);
    }

    public void setBreakDownAction(BreakDownAction breakDownAction) {
        lastBreakDownAction = breakDownAction;
    }

    public BreakDownAction getBreakDownAction() {
        return lastBreakDownAction;
    }

    public LessonFragment.ViewState getLessonViewState() {
        return lessonViewState;
    }

    public void setLessonViewState(LessonFragment.ViewState lessonViewState) {
        this.lessonViewState = lessonViewState;
    }

    public String getSelectedWord() {
        return selectedWord;
    }

    public void setSelectedWord(String selectedWord) {
        this.selectedWord = selectedWord;
    }

    public boolean isSkipHelpPopup() {
        return skipHelpPopup;
    }

    public void setSkipHelpPopup(boolean skipHelpPopup) {
        this.skipHelpPopup = skipHelpPopup;
    }
}

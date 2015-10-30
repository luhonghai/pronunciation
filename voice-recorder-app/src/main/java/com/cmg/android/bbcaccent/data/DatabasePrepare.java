package com.cmg.android.bbcaccent.data;

import android.content.Context;
import android.os.AsyncTask;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.utils.AndroidHelper;
import com.cmg.android.bbcaccent.utils.FileHelper;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by luhonghai on 4/10/15.
 */
public class DatabasePrepare {

    private final Context context;

    private final OnPrepraredListener prepraredListener;

    public DatabasePrepare(Context context, OnPrepraredListener prepraredListener) {
        this.context = context;
        this.prepraredListener = prepraredListener;
    }

    public interface OnPrepraredListener {
        public void onComplete();
    }

    public void prepare() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                loadDatabase();
                loadTips();
                //  loadHelpContent();
                prepraredListener.onComplete();
                return null;
            }
        }.execute();
    }

    private void loadHelpContent() {
        if (!ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
        }
        for (int i = 1; i <= 11; i++)
            ImageLoader.getInstance().loadImageSync("assets://help-content/help_content_" + i + ".png");
    }

    private void loadTips() {
        TipsContainer tipsContainer = new TipsContainer(context);
        tipsContainer.loadSync();
    }

    private void loadDatabase() {
        File databaseDir = new File(FileHelper.getApplicationDir(context), "databases");
        String currentVersion= AndroidHelper.getVersionName(context);
        File fileVersion = new File(databaseDir, "version");
        if (!databaseDir.exists()) {
            databaseDir.mkdirs();
        }
        boolean willOverride = false;
        File lessonDb = new File(databaseDir, "lesson");
        SimpleAppLog.info("Current db version: " + currentVersion);
        if (!lessonDb.exists()) willOverride = true;
        if (fileVersion.exists()) {
            try {
                String oldVersion = FileUtils.readFileToString(fileVersion, "UTF-8");
                SimpleAppLog.info("Old version: " + oldVersion);
                if (!oldVersion.equalsIgnoreCase(currentVersion)) {
                    willOverride = true;
                }
            } catch (IOException e) {
                SimpleAppLog.error("Could not read old version",e);
            }
        } else {
            willOverride = true;
        }
        SimpleAppLog.info("fileVersion :"+fileVersion);
        if (willOverride) {
            SimpleAppLog.info("Try to preload sqlite database");
            try {
                FileUtils.copyInputStreamToFile(context.getAssets().open("database/lesson.db"), lessonDb);
                FileUtils.writeStringToFile(fileVersion, currentVersion, "UTF-8", true);
            } catch (Exception e) {
                SimpleAppLog.error("Could not save database from asset",e);
            }
            SimpleAppLog.info("Save new version to :"+fileVersion);
        }

        MainApplication.getContext().initDatabase();
    }
}

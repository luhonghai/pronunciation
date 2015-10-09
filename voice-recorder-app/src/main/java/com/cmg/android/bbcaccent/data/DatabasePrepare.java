package com.cmg.android.bbcaccent.data;

import android.content.Context;
import android.os.AsyncTask;

import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

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
        WordDBAdapter wordDBAdapter = new WordDBAdapter();
        try {
            wordDBAdapter.checkWord();
        } catch (Exception e) {
            SimpleAppLog.error("Could not check database",e);
        }
    }
}

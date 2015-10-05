package com.cmg.android.bbcaccent.data;

import android.content.Context;
import android.os.AsyncTask;

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
        ScoreDBAdapter scoreDBAdapter = new ScoreDBAdapter(context.getApplicationContext());
        WordDBAdapter wordDBAdapter = WordDBAdapter.getInstance(context.getApplicationContext());
        PhonemeScoreDBAdapter phonemeScoreDBAdapter = new PhonemeScoreDBAdapter(context.getApplicationContext());
        try {
            scoreDBAdapter.open();
            scoreDBAdapter.getAll("");
            phonemeScoreDBAdapter.open();
            phonemeScoreDBAdapter.getAll();
            wordDBAdapter.open();
            wordDBAdapter.getAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scoreDBAdapter.close();
            phonemeScoreDBAdapter.close();
            wordDBAdapter.close();
        }

    }
}

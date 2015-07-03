package com.cmg.android.bbcaccentamt.data;

import android.content.Context;
import android.os.AsyncTask;

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
                prepraredListener.onComplete();
                return null;
            }
        }.execute();
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
            scoreDBAdapter.getAll();
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

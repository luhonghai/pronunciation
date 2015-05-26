package com.cmg.android.bbcaccent.activity;


import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.cmg.android.bbcaccent.utils.AnalyticHelper;
import com.cmg.android.bbcaccent.utils.AndroidHelper;
import com.cmg.android.bbcaccent.utils.ExceptionHandler;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import io.fabric.sdk.android.Fabric;

public abstract class BaseActivity extends SherlockFragmentActivity  {

    public static final String DICTIONARY_ITEM = "DICTIONARY_ITEM";

    public static final String USER_VOICE_MODEL = "USER_VOICE_MODEL";

    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tracker t = AnalyticHelper.getTracker(this);
        if (t != null) {
            t.setScreenName(this.getClass().getName());
            t.send(new HitBuilders.ScreenViewBuilder().build());
        }

        Fabric.with(this, new Crashlytics());

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(configuration);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ImageLoader.getInstance().isInited())
            ImageLoader.getInstance().destroy();
    }

    @Override
    protected void onPause() {
        AndroidHelper.takeScreenShot(this);
        isRunning = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        isRunning = true;
        super.onResume();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }
}

package com.cmg.android.bbcaccent.activity;


import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.cmg.android.bbcaccent.utils.AndroidHelper;
import com.cmg.android.bbcaccent.utils.ExceptionHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public abstract class BaseActivity extends SherlockFragmentActivity  {

    public static final String DICTIONARY_ITEM = "DICTIONARY_ITEM";

    public static final String USER_VOICE_MODEL = "USER_VOICE_MODEL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(configuration);
      //  Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
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
        super.onPause();
    }
}

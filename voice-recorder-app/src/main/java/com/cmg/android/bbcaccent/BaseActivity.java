package com.cmg.android.bbcaccent;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.cmg.android.bbcaccent.fragment.FeedbackFragment;
import com.cmg.android.bbcaccent.utils.AnalyticHelper;
import com.cmg.android.bbcaccent.utils.AndroidHelper;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.fabric.sdk.android.Fabric;

public abstract class BaseActivity extends FragmentActivity {

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

        if (!ImageLoader.getInstance().isInited()) {
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        }
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (ImageLoader.getInstance().isInited())
//            ImageLoader.getInstance().destroy();
    }

    @Override
    protected void onPause() {
        isRunning = false;
        AndroidHelper.takeScreenShot(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        isRunning = true;
        super.onResume();
    }

    public boolean checkNetwork() {
        return checkNetwork(true);
    }

    public boolean checkNetwork(final boolean closeApp) {
        boolean isNetworkAvailable = AndroidHelper.isNetworkAvailable(this);
        if (!isNetworkAvailable) {
            showErrorNetworkMessage(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    if (closeApp) {
                        BaseActivity.this.finish();
                    } else {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                }
            });
        }
        return isNetworkAvailable;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.default_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_feedback:
                Intent activity = new Intent();
                activity.setClass(this, FeedbackFragment.class);
                this.startActivity(activity);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    protected void showErrorNetworkMessage(SweetAlertDialog.OnSweetClickListener onConfirmListener) {
        SweetAlertDialog d = new SweetAlertDialog(BaseActivity.this, SweetAlertDialog.ERROR_TYPE);
        d.setTitleText(getString(R.string.error_network_title));
        d.setContentText(getString(R.string.error_network_message));
        d.setConfirmText(getString(R.string.dialog_ok));
        if (onConfirmListener == null) {
            onConfirmListener = new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            };
        }
        d.setConfirmClickListener(onConfirmListener);
        d.show();
    }
}

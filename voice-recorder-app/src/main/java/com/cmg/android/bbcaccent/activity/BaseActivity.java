package com.cmg.android.bbcaccent.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.utils.AnalyticHelper;
import com.cmg.android.bbcaccent.utils.AndroidHelper;
import com.cmg.android.bbcaccent.utils.ExceptionHandler;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import cn.pedant.SweetAlert.SweetAlertDialog;
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
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ImageLoader.getInstance().isInited())
            ImageLoader.getInstance().destroy();
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
            final SpannableString s = new SpannableString("Sorry but your Internet connection does not appear to be working.");
            Linkify.addLinks(s, Linkify.ALL);
            SweetAlertDialog d = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
            d.setTitleText("Network not available");
            d.setContentText("Sorry but your Internet connection does not appear to be working.");
            d.setConfirmText("Close");
            d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    if (closeApp) {
                        BaseActivity.this.finish();
                    } else {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                }
            });
            d.show();
//            AlertDialog d = new AlertDialog.Builder(BaseActivity.this)
//                    .setTitle("Network not available")
//                    .setMessage(s)
//                    .setNegativeButton("Close", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            if (closeApp) {
//                                BaseActivity.this.finish();
//                            } else {
//                                dialog.dismiss();
//                            }
//                        }
//                    }).create();
//            d.show();
//            ((TextView) d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
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
        getSupportMenuInflater().inflate(R.menu.default_menu, menu);
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
                activity.setClass(this, FeedbackActivity.class);
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
}

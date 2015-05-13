/*
 * Copyright (c) 2013. CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package com.cmg.android.bbcaccent.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.activity.fragment.Preferences;
import com.cmg.android.bbcaccent.common.Common;
import com.cmg.android.bbcaccent.common.DeviceInfoCommon;
import com.cmg.android.bbcaccent.common.FileCommon;
import com.cmg.android.bbcaccent.data.UserProfile;
import com.cmg.android.bbcaccent.http.UploadFeedbackAsync;
import com.cmg.android.bbcaccent.utils.AndroidHelper;
import com.cmg.android.bbcaccent.utils.ContentUtils;
import com.cmg.android.bbcaccent.utils.DeviceUuidFactory;
import com.cmg.android.bbcaccent.utils.ExceptionHandler;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class FeedbackActivity extends BaseActivity {
    public static final String SEND_FEEDBACK_FINISH = "com.cmg.android.bbcaccent.activity.FeedbackActivity";

    String stackTrace;

    private AlertDialog dialogWaiting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_feedback);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(ExceptionHandler.STACK_TRACE)) {
            stackTrace =bundle.getString(ExceptionHandler.STACK_TRACE);
            final AlertDialog dialogError = new AlertDialog.Builder(this).setTitle("An unexpected error occurred")
                    .setMessage(getResources().getString(R.string.error_message))
                    .setNegativeButton(getResources().getString(R.string.dialog_close),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            }).create();
            dialogError.show();
        }
        TextView t3 = (TextView) findViewById(R.id.textView2);
        t3.setText(Html.fromHtml("<a href=\"http://www.accenteasy.com/bbcaccent/privacy\">How is my information stored and shared</a>"));
        t3.setMovementMethod(LinkMovementMethod.getInstance());
        dialogWaiting = new AlertDialog.Builder(this)
                .setMessage("Please wait a moment while your feedback is being processed")
                .setNegativeButton(getResources().getString(R.string.dialog_close),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).create();
        registerReceiver(mHandleMessageReader, new IntentFilter(SEND_FEEDBACK_FINISH));
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public String getItemSelectSpin() {
        UserProfile profile = Preferences.getCurrentProfile(this);
        return profile == null ? "unknown" : profile.getUsername();
    }

    public String getTextDescription() {
        String desc = null;
        EditText text = (EditText) findViewById(R.id.textDescription);
        desc = text.getText().toString();
        return desc;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
//        menu.add("Cancel").setShowAsAction(
//                MenuItem.SHOW_AS_ACTION_ALWAYS
//                        | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//
//        menu.add("Preview").setShowAsAction(
//                MenuItem.SHOW_AS_ACTION_ALWAYS
//                        | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        menu.add("send").setShowAsAction(
                MenuItem.SHOW_AS_ACTION_ALWAYS
                        | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
                onBackPressed();
                return true;
        }
        if (item.getTitle().equals("Cancel")) {
            super.onBackPressed();
        } else if (item.getTitle().equals("Preview")) {
            String html = ContentUtils.generatePreviewHtmlFeedback(getFormData());
            View view = getLayoutInflater().inflate(R.layout.change_log, null);
            ((WebView) view.findViewById(R.id.webView)).loadDataWithBaseURL(
                    html, html, "text/html", null, null);
            final AlertDialog dialogChangeLog = new AlertDialog.Builder(this)
                    .setTitle("Preview Feedback")
                    .setView(view)
                    .setNegativeButton(getResources().getString(R.string.dialog_close),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();

                                }
                            }).create();

            dialogChangeLog.show();
        } else if (item.getTitle().toString().equalsIgnoreCase("send")) {
            Map<String, String> params = getFormData();
            String screenshootPath = AndroidHelper.getLatestScreenShootPath(this.getApplicationContext());
            params.put(FileCommon.PARA_FILE_PATH, screenshootPath);
            params.put(FileCommon.PARA_FILE_NAME,
                    ContentUtils.getFileName(screenshootPath));
            params.put(FileCommon.PARA_FILE_TYPE, FileCommon.PNG_MIME_TYPE);
            UploadFeedbackAsync uploadAsync = new UploadFeedbackAsync(this.getApplicationContext());
            uploadAsync.execute(params);

            dialogWaiting.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private Map<String, String> getFormData() {
        Map<String, String> infos = new HashMap<String, String>();
        DeviceUuidFactory uIdFac = new DeviceUuidFactory(this);
        String pathLastScreenShot = AndroidHelper.getLatestScreenShootURL(this);
        if (pathLastScreenShot != null && pathLastScreenShot.length() > 0) {
            infos.put(ContentUtils.KEY_SCREENSHOOT, pathLastScreenShot);
        }
        UserProfile profile = Preferences.getCurrentProfile(this);

        infos.put(DeviceInfoCommon.ACCOUNT, getItemSelectSpin());
        infos.put(DeviceInfoCommon.FEEDBACK_DESCRIPTION, getTextDescription());
        infos.put(DeviceInfoCommon.IMEI, uIdFac.getDeviceUuid().toString());
        infos.put(DeviceInfoCommon.APP_VERSION, AndroidHelper.getVersionName(this.getApplicationContext()));
        infos.put(DeviceInfoCommon.MODEL, android.os.Build.MODEL);
        infos.put(DeviceInfoCommon.OS_VERSION, System.getProperty("os.version"));
        infos.put(DeviceInfoCommon.OS_API_LEVEL, android.os.Build.VERSION.SDK);
        infos.put(DeviceInfoCommon.DEVICE_NAME, android.os.Build.DEVICE);
        if (profile != null) {
            Gson gson = new Gson();
            infos.put("profile", gson.toJson(profile));
        }
        if (stackTrace != null && stackTrace.length() > 0) {
            infos.put(DeviceInfoCommon.STACK_TRACE, stackTrace);
        }
        infos.put(Common.TYPE, Common.FEEDBACK);
        return infos;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mHandleMessageReader);
    }

    private void closeFeedBack(){
        super.onBackPressed();
    }

    /**
     * receive message from server
     */
    private final BroadcastReceiver mHandleMessageReader = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras().containsKey(SEND_FEEDBACK_FINISH)) {
                if (dialogWaiting == null)
                    return;
                if (dialogWaiting.isShowing()) {
                    dialogWaiting.dismiss();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            closeFeedBack();
                        }
                    }, 3000);

                }
            }
        }
    };

}

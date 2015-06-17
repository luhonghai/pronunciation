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
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FeedbackActivity extends BaseActivity {
    public static final String SEND_FEEDBACK_FINISH = "com.cmg.android.bbcaccent.activity.FeedbackActivity";

    private String stackTrace;

    private SweetAlertDialog dialogProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_feedback);
        findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(ExceptionHandler.STACK_TRACE)) {
            stackTrace =bundle.getString(ExceptionHandler.STACK_TRACE);

            SweetAlertDialog d = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
            d.setTitleText(getString(R.string.error_message_title));
            d.setContentText(getResources().getString(R.string.error_message));
            d.setConfirmText(getResources().getString(R.string.dialog_ok));
            d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
            d.show();
        }
        TextView t3 = (TextView) findViewById(R.id.textView2);
        //t3.setText(Html.fromHtml(getString(R.string.feedback_privacy_link)));
        t3.setMovementMethod(LinkMovementMethod.getInstance());
        registerReceiver(mHandleMessageReader, new IntentFilter(FeedbackActivity.SEND_FEEDBACK_FINISH));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void showProcessDialog() {
        dialogProcess = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialogProcess.setTitleText(getString(R.string.processing));
        dialogProcess.setCancelable(false);
        dialogProcess.show();
    }

    public String getItemSelectSpin() {
        UserProfile profile = Preferences.getCurrentProfile(this);
        return profile == null ? getString(R.string.unknown) : profile.getUsername();
    }

    public String getTextDescription() {
        String desc = null;
        EditText text = (EditText) findViewById(R.id.textDescription);
        desc = text.getText().toString();
        return desc;
    }

//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//                onBackPressed();
//                return true;
//        }
//        if (item.getTitle().equals("Cancel")) {
//            super.onBackPressed();
//        } else if (item.getTitle().equals("Preview")) {
//            String html = ContentUtils.generatePreviewHtmlFeedback(getFormData());
//            View view = getLayoutInflater().inflate(R.layout.change_log, null);
//            ((WebView) view.findViewById(R.id.webView)).loadDataWithBaseURL(
//                    html, html, "text/html", null, null);
//            final AlertDialog dialogChangeLog = new AlertDialog.Builder(this)
//                    .setTitle("Preview Feedback")
//                    .setView(view)
//                    .setNegativeButton(getResources().getString(R.string.dialog_close),
//                            new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog,
//                                                    int which) {
//                                    dialog.dismiss();
//
//                                }
//                            }).create();
//
//            dialogChangeLog.show();
//        } else if (item.getTitle().toString().equalsIgnoreCase("send")) {
//            sendFeedback();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void sendFeedback() {
        if (checkNetwork(false)) {
            if (getTextDescription().trim().length() == 0) {
                SweetAlertDialog d = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
                d.setTitleText(getString(R.string.please_enter_your_message));
                d.setContentText("");
                d.setConfirmText(getString(R.string.dialog_ok));
                d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
                d.show();
            } else {
                Map<String, String> params = getFormData();
                String screenshootPath = AndroidHelper.getLatestScreenShootPath(this.getApplicationContext());
                SimpleAppLog.info("screenshootPath: " + screenshootPath);
                params.put(FileCommon.PARA_FILE_PATH, screenshootPath);
                params.put(FileCommon.PARA_FILE_NAME,
                        ContentUtils.getFileName(screenshootPath));
                params.put(FileCommon.PARA_FILE_TYPE, FileCommon.PNG_MIME_TYPE);
                UploadFeedbackAsync uploadAsync = new UploadFeedbackAsync(this.getApplicationContext(), params);
                uploadAsync.execute();
                showProcessDialog();
            }
        }
    }

    private Map<String, String> getFormData() {

        Map<String, String> infos = new HashMap<String, String>();

        String pathLastScreenShot = AndroidHelper.getLatestScreenShootURL(this);
        if (pathLastScreenShot != null && pathLastScreenShot.length() > 0) {
            infos.put(ContentUtils.KEY_SCREENSHOOT, pathLastScreenShot);
        }
        UserProfile profile = Preferences.getCurrentProfile(this);
        infos.put(DeviceInfoCommon.FEEDBACK_DESCRIPTION, getTextDescription());
        DeviceUuidFactory uIdFac = new DeviceUuidFactory(this);
        infos.put(DeviceInfoCommon.IMEI, uIdFac.getDeviceUuid().toString());
        infos.put(DeviceInfoCommon.APP_VERSION, AndroidHelper.getVersionName(this.getApplicationContext()));
        infos.put(DeviceInfoCommon.MODEL, android.os.Build.MODEL);
        infos.put(DeviceInfoCommon.OS_VERSION, System.getProperty("os.version"));
        infos.put(DeviceInfoCommon.OS_API_LEVEL, android.os.Build.VERSION.SDK);
        infos.put(DeviceInfoCommon.DEVICE_NAME, android.os.Build.DEVICE);
        if (profile != null) {
            infos.put(DeviceInfoCommon.ACCOUNT, profile.getUsername());
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
        try {
            unregisterReceiver(mHandleMessageReader);
        } catch (Exception e) {

        }
    }

    private void closeFeedBack(){
        this.finish();
    }

    /**
     * receive message from server
     */
    private final BroadcastReceiver mHandleMessageReader = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (dialogProcess != null)
                        dialogProcess.dismissWithAnimation();
                    SweetAlertDialog d = new SweetAlertDialog(FeedbackActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                    d.setTitleText(getString(R.string.successfully_submitted));
                    d.setContentText(getString(R.string.feedback_success_message));
                    d.setConfirmText(getString(R.string.dialog_ok));
                    d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            FeedbackActivity.this.finish();
                        }
                    });
                    d.show();
                }
            });
        }
    };

}

/*
 * Copyright (c) 2013. CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package com.cmg.android.bbcaccent.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.data.dto.UserProfile;
import com.cmg.android.bbcaccent.http.UploadFeedbackAsync;
import com.cmg.android.bbcaccent.http.common.Common;
import com.cmg.android.bbcaccent.http.common.DeviceInfoCommon;
import com.cmg.android.bbcaccent.http.common.FileCommon;
import com.cmg.android.bbcaccent.utils.AnalyticHelper;
import com.cmg.android.bbcaccent.utils.AndroidHelper;
import com.cmg.android.bbcaccent.utils.ContentUtils;
import com.cmg.android.bbcaccent.utils.DeviceUuidFactory;
import com.cmg.android.bbcaccent.utils.ExceptionHandler;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FeedbackFragment extends BaseFragment {

    private String stackTrace;

    private SweetAlertDialog dialogProcess;

    private int listenerId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.activity_feedback, null);
        root.findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null && bundle.containsKey(ExceptionHandler.STACK_TRACE)) {
            stackTrace =bundle.getString(ExceptionHandler.STACK_TRACE);

            SweetAlertDialog d = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
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
        TextView t3 = (TextView) root.findViewById(R.id.textView2);
        //t3.setText(Html.fromHtml(getString(R.string.feedback_privacy_link)));
        t3.setMovementMethod(LinkMovementMethod.getInstance());
        listenerId = MainBroadcaster.getInstance().register(new MainBroadcaster.ReceiverListener() {
            @Override
            public void onReceiveMessage(final MainBroadcaster.Filler filler, final Bundle bundle) {
                if (filler == MainBroadcaster.Filler.FEEDBACK && getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (dialogProcess != null)
                                dialogProcess.dismissWithAnimation();
                            boolean done = bundle != null && bundle.getBoolean(MainBroadcaster.Filler.Key.DATA.toString());
                            setDescriptionText("");
                            SweetAlertDialog d;
                            if (done) {
                                d = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
                                d.setTitleText(getString(R.string.successfully_submitted));
                                d.setContentText(getString(R.string.feedback_success_message));
                            } else {
                                d = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
                                d.setTitleText(getString(R.string.could_not_send_feedback));
                                d.setContentText(getString(R.string.could_not_connect_server_message));
                            }
                            d.setConfirmText(getString(R.string.dialog_ok));
                            d.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismissWithAnimation();
                                }
                            });
                            d.show();
                        }
                    });
                }
            }
        });
        UserProfile userProfile = Preferences.getCurrentProfile();
        AnalyticHelper.sendEvent(AnalyticHelper.Category.FEEDBACK, AnalyticHelper.Action.OPEN_FEEDBACK_PAGE, userProfile == null ? "" : userProfile.getUsername());
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainBroadcaster.getInstance().unregister(listenerId);
        if (dialogProcess != null) {
            if (dialogProcess.isShowing()) {
                dialogProcess.dismiss();
            }
            dialogProcess = null;
        }
    }

    private void showProcessDialog() {
        dialogProcess = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        dialogProcess.setTitleText(getString(R.string.processing));
        dialogProcess.setCancelable(false);
        dialogProcess.show();
    }

    public String getItemSelectSpin() {
        UserProfile profile = Preferences.getCurrentProfile(MainApplication.getContext());
        return profile == null ? getString(R.string.unknown) : profile.getUsername();
    }

    public void setDescriptionText(String text) {
        if (getView() == null) return;
        EditText editText = (EditText) getView().findViewById(R.id.textDescription);
        editText.setText(text);
    }

    public String getTextDescription() {
        if (getView() == null) return "";
        EditText text = (EditText) getView().findViewById(R.id.textDescription);
        return text.getText().toString();
    }

    private void sendFeedback() {
        if (checkNetwork(false)) {
            if (getTextDescription().trim().length() == 0) {
                SweetAlertDialog d = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
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
                String screenshootPath = AndroidHelper.getLatestScreenShootPath(MainApplication.getContext());
                SimpleAppLog.info("screenshootPath: " + screenshootPath);
                params.put(FileCommon.PARA_FILE_PATH, screenshootPath);
                params.put(FileCommon.PARA_FILE_NAME,
                        ContentUtils.getFileName(screenshootPath));
                params.put(FileCommon.PARA_FILE_TYPE, FileCommon.PNG_MIME_TYPE);
                UploadFeedbackAsync uploadAsync = new UploadFeedbackAsync(MainApplication.getContext(), params);
                uploadAsync.execute();
                showProcessDialog();
                UserProfile userProfile = Preferences.getCurrentProfile();
                AnalyticHelper.sendEvent(AnalyticHelper.Category.FEEDBACK, AnalyticHelper.Action.SEND_FEEDBACK, userProfile == null ? "" : userProfile.getUsername());
            }
        }
    }

    private Map<String, String> getFormData() {
        Map<String, String> infos = new HashMap<String, String>();

        String pathLastScreenShot = AndroidHelper.getLatestScreenShootURL(MainApplication.getContext());
        if (pathLastScreenShot != null && pathLastScreenShot.length() > 0) {
            infos.put(ContentUtils.KEY_SCREENSHOOT, pathLastScreenShot);
        }
        UserProfile profile = Preferences.getCurrentProfile(MainApplication.getContext());
        infos.put(DeviceInfoCommon.FEEDBACK_DESCRIPTION, getTextDescription());
        DeviceUuidFactory uIdFac = new DeviceUuidFactory(MainApplication.getContext());
        infos.put(DeviceInfoCommon.IMEI, uIdFac.getDeviceUuid().toString());
        infos.put(DeviceInfoCommon.APP_VERSION, AndroidHelper.getVersionName(MainApplication.getContext()));
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
}

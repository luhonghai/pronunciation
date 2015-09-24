/*
 * Copyright (c) 2013. CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */
package com.cmg.android.bbcaccentamt.http;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.cmg.android.bbcaccentamt.AppLog;
import com.cmg.android.bbcaccentamt.MainActivity;
import com.cmg.android.bbcaccentamt.activity.fragment.Preferences;
import com.cmg.android.bbcaccentamt.common.Common;
import com.cmg.android.bbcaccentamt.data.DatabaseHandlerSentence;
import com.cmg.android.bbcaccentamt.data.DatabasePrepare;
import com.cmg.android.bbcaccentamt.data.SentenceModel;
import com.cmg.android.bbcaccentamt.data.RecorderSentenceModel;
import com.cmg.android.bbcaccentamt.data.UserProfile;
import com.cmg.android.bbcaccentamt.http.exception.UploaderException;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * DOCME
 *
 * @author $Author$
 * @version $Revision$
 * @Creator Hai Lu
 * @Last changed: $LastChangedDate$
 */
public class UploaderAllAsync extends AsyncTask<List<Map<String, String>>, Void, String> {
    public static final String UPLOAD_COMPLETE_INTENT = "com.cmg.android.bbcaccent.UploaderAllAysnc";
    private final Context context;
    private final String uploadUrl;
    public UploaderAllAsync(Context context, String uploadUrl) {
        this.context = context;
        this.uploadUrl = uploadUrl;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(List<Map<String, String>>... params) {
        AppLog.logString("do upload");
        DatabaseHandlerSentence databaseHandlerSentence=new DatabaseHandlerSentence(context);
        UserProfile profile = Preferences.getCurrentProfile(context);
        int n=0;
        try {
            if (params != null && params.length > 0) {
                for (List<Map<String, String>> param : params) {
                    for (Map<String, String> p : param) {
                        AppLog.logString("do upload : " + p.get("sentence"));
                        String result = FileUploader.upload(p, uploadUrl);
                        Gson gson = new Gson();
                        DatabasePrepare.ResponseDataRecorded datas = gson.fromJson(result, DatabasePrepare.ResponseDataRecorded.class);
                        if(datas != null && datas.RecordedSentences != null && datas.RecordedSentences.size() > 0 && datas.status()!=false){
                            for(DatabasePrepare.RecordedSentence model : datas.RecordedSentences){
                                //call database update version, status, isdeleted with object model.
                                databaseHandlerSentence.updateRecorder(model.getVersion(),model.getStatus(),model.isDeteted(),model.getSentenceId(),model.getAccount());

                            }

                        }else {
                            databaseHandlerSentence.updateRecorder(databaseHandlerSentence.getLastedVersionRecorder(), Common.NOT_RECORD, Common.ISDELETED_FALSE,p.get("sentence"), profile.getUsername());
                            n=n+1;
                        }

                    }
                }
            }
            if(n!=0){
                return "has been change";
            }else {
                return "upload done";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UploaderException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onPostExecute(String v) {
        Intent intent = new Intent(UPLOAD_COMPLETE_INTENT);
        intent.putExtra(UPLOAD_COMPLETE_INTENT, v);
        context.sendBroadcast(intent);
        super.onPostExecute(v);
    }


}

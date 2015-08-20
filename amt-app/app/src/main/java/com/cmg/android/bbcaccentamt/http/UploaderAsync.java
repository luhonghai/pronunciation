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

import com.cmg.android.bbcaccentamt.AppLog;
import com.cmg.android.bbcaccentamt.http.exception.UploaderException;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * DOCME
 *
 * @author $Author$
 * @version $Revision$
 * @Creator Hai Lu
 * @Last changed: $LastChangedDate$
 */
public class UploaderAsync extends AsyncTask<Map<String, String>, Void, String> {
    public static final String UPLOAD_COMPLETE_INTENT = "com.cmg.android.bbcaccent.UploaderAysnc";
    private final Context context;
    private final String uploadUrl;
    public UploaderAsync(Context context, String uploadUrl) {
        this.context = context;
        this.uploadUrl = uploadUrl;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(Map<String, String>... params) {
        AppLog.logString("do upload");
        String results = new String();
        try {

            if (params != null && params.length > 0) {
                for (Map<String, String> param : params) {
                    AppLog.logString("do upload : " + param.get("sentence"));
                    results = FileUploader.upload(param, uploadUrl);
                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UploaderException e) {
            e.printStackTrace();
        }

        return results;
    }

    @Override
    protected void onPostExecute(String v) {
        AppLog.logString("String v : " + v);
        Intent intent = new Intent(UPLOAD_COMPLETE_INTENT);
        intent.putExtra(UPLOAD_COMPLETE_INTENT, v);

        context.sendBroadcast(intent);
        super.onPostExecute(v);
    }


}

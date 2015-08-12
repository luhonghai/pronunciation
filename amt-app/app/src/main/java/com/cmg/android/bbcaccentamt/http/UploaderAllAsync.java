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
import com.cmg.android.bbcaccentamt.data.Database;
import com.cmg.android.bbcaccentamt.data.DatabaseHandlerSentence;
import com.cmg.android.bbcaccentamt.data.SentenceModel;
import com.cmg.android.bbcaccentamt.data.SentenceUpload;
import com.cmg.android.bbcaccentamt.http.exception.UploaderException;
import com.cmg.android.bbcaccentamt.utils.SimpleAppLog;

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
        Database database=new Database(context);
        try {
            String idSentence = "";
            if (params != null && params.length > 0) {
                for (List<Map<String, String>> param : params) {
                    for (Map<String, String> p : param) {
                        AppLog.logString("do upload : " + p.get("sentence"));
                        String result = FileUploader.upload(p, uploadUrl);
                        idSentence = p.get("sentence");
                        SentenceModel sentenceModel=databaseHandlerSentence.getSentence(idSentence);
                        sentenceModel.setStatus(1);
                        sentenceModel.setIndex(4);
                        databaseHandlerSentence.updateSentence(sentenceModel);
                        SentenceUpload sentenceUpload=new SentenceUpload();
                        sentenceUpload.setSentence(idSentence);
                        database.addSentence(sentenceUpload);

                    }
                }
            }
            return "upload done";
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

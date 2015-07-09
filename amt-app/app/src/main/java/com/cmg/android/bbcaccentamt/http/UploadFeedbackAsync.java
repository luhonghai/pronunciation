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


import com.cmg.android.bbcaccentamt.R;
import com.cmg.android.bbcaccentamt.activity.FeedbackActivity;
import com.cmg.android.bbcaccentamt.http.exception.UploaderException;
import com.cmg.android.bbcaccentamt.utils.SimpleAppLog;

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
public class UploadFeedbackAsync extends AsyncTask<Void, Void, String> {
    private final Context context;
    private final Map<String, String> params;

    public UploadFeedbackAsync(Context context, Map<String, String> params) {
        this.context = context;
        this.params = params;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(Void... v) {
        try {
            String result = FileUploader.upload(params, context.getResources().getString(R.string.feedback_url));
            SimpleAppLog.info("Feedback response: " + result);
            return result;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return e.getMessage();
        } catch (UploaderException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String v) {
        Intent intent = new Intent(FeedbackActivity.SEND_FEEDBACK_FINISH);
        intent.putExtra(FeedbackActivity.SEND_FEEDBACK_FINISH, "true");
        context.sendBroadcast(intent);
        //Toast.makeText(context, "Completed", Toast.LENGTH_LONG).show();
        super.onPostExecute(v);
    }

}
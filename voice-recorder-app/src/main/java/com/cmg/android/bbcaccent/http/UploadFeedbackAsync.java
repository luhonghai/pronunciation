/*
 * Copyright (c) 2013. CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */
package com.cmg.android.bbcaccent.http;

import android.content.Context;
import android.os.AsyncTask;


import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.http.exception.UploaderException;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;

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
        MainBroadcaster.getInstance().getSender().sendMessage(MainBroadcaster.Filler.FEEDBACK, null);
        super.onPostExecute(v);
    }

}

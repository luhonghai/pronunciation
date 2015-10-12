package com.cmg.android.bbcaccent.http;

import android.content.Context;
import android.os.AsyncTask;

import com.cmg.android.bbcaccent.data.sqlite.PhonemeScoreDBAdapter;
import com.cmg.android.bbcaccent.data.dto.SphinxResult;
import com.cmg.android.bbcaccent.data.dto.UserVoiceModel;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

/**
 * Created by lantb on 2015-09-03.
 */
public class PhonemeScoreAsync extends AsyncTask<Map<String, String>, Void, String> {

    private final Context context;
    private final String uploadUrl;

    private class ResponseDataPhoneme  extends ResponseData<UserVoiceModel>{
        List<SphinxResult.PhonemeScore> phonemeScoreDBList;
    }

    public PhonemeScoreAsync(Context context, String uploadUrl) {
        this.context = context;
        this.uploadUrl = uploadUrl;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(Map<String, String>... params) {
        StringBuffer results = new StringBuffer();
        try {
            if (params != null && params.length > 0) {
                for (Map<String, String> param : params) {
                    SimpleAppLog.info("uploadUrl : " + uploadUrl);
                    HttpContacter cn = new HttpContacter(this.context);
                    results.append(cn.post(param, uploadUrl));
                }
            }
            String json = results.toString();
            updateDb(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateDb(String json){
        try {
            Gson gson = new Gson();
            PhonemeScoreAsync.ResponseDataPhoneme data = gson.fromJson(json,PhonemeScoreAsync.ResponseDataPhoneme.class);
            if(data!=null && data.phonemeScoreDBList!=null && data.phonemeScoreDBList.size() > 0 ){
                PhonemeScoreDBAdapter phonemeScoreDBAdapter = new PhonemeScoreDBAdapter();
                phonemeScoreDBAdapter.insert(data.phonemeScoreDBList);
            }
        }catch (Exception e){
            SimpleAppLog.error("Could not update database",e);
        }

    }


    @Override
    protected void onPostExecute(String v) {
        super.onPostExecute(v);
    }
}

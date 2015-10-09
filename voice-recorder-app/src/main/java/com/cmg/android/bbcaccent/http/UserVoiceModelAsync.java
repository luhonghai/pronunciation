package com.cmg.android.bbcaccent.http;

import android.content.Context;
import android.os.AsyncTask;

import com.cmg.android.bbcaccent.data.ScoreDBAdapter;
import com.cmg.android.bbcaccent.data.dto.PronunciationScore;
import com.cmg.android.bbcaccent.data.dto.UserVoiceModel;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by lantb on 2015-09-03.
 */
public class UserVoiceModelAsync extends AsyncTask<Map<String, String>, Void, String> {

    private final Context context;
    private final String uploadUrl;

    private class ResponseDataUserVoice  extends ResponseData<UserVoiceModel>{
        List<UserVoiceModel> userVoiceModelList;
    }

    public UserVoiceModelAsync(Context context, String uploadUrl) {
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
                    SimpleAppLog.info("result : " + results.toString());
                }
            }
            String json = results.toString();
            SimpleAppLog.info("clone  user voice model from server " + json);
            updateDb(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateDb(String json){
        try {
            Gson gson = new Gson();
            UserVoiceModelAsync.ResponseDataUserVoice data = gson.fromJson(json,UserVoiceModelAsync.ResponseDataUserVoice.class);
            SimpleAppLog.info("clone  responsedata from server " + data.userVoiceModelList.size());
            if(data.userVoiceModelList!=null && data.userVoiceModelList.size() > 0 ){
                ScoreDBAdapter scoreDBAdapter = new ScoreDBAdapter();
                List<PronunciationScore> pronunciationScores = new ArrayList<PronunciationScore>();
                for(UserVoiceModel model : data.userVoiceModelList){
                    PronunciationScore score = new PronunciationScore();
                    String dataId = model.getId();
                    score.setDataId(dataId);
                    score.setScore(model.getScore());
                    score.setWord(model.getWord());
                    score.setTimestamp(new Date(model.getServerTime()));
                    //DENP-238
                    score.setUsername(model.getUsername());
                    score.setVersion(model.getVersion());
                    pronunciationScores.add(score);
                }
                scoreDBAdapter.insert(pronunciationScores);
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

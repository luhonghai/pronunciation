package com.cmg.android.bbcaccent.http;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.cmg.android.bbcaccent.data.ScoreDBAdapter;
import com.cmg.android.bbcaccent.data.UserVoiceModel;
import com.cmg.android.bbcaccent.http.exception.UploaderException;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
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
                    results.append(FileUploader.upload(param, uploadUrl));
                }
            }
            String json = results.toString();
            updateDb(json);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UploaderException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateDb(String json){
        try {
            Gson gson = new Gson();
            UserVoiceModelAsync.ResponseDataUserVoice data = gson.fromJson(json,UserVoiceModelAsync.ResponseDataUserVoice.class);
            if(data!=null && data.userVoiceModelList!=null && data.userVoiceModelList.size() > 0 ){
                ScoreDBAdapter scoreDBAdapter = new ScoreDBAdapter(context);
                scoreDBAdapter.open();
                scoreDBAdapter.getDB().beginTransaction();
                for(UserVoiceModel model : data.userVoiceModelList){
                    ScoreDBAdapter.PronunciationScore score = new ScoreDBAdapter.PronunciationScore();
                    String dataId = model.getId();
                    score.setDataId(dataId);
                    score.setScore(model.getScore());
                    score.setWord(model.getWord());
                    score.setTimestamp(new Date(model.getServerTime()));
                    //DENP-238
                    score.setUsername(model.getUsername());
                    score.setVersion(model.getVersion());
                    scoreDBAdapter.insert(score);
                }
                scoreDBAdapter.getDB().setTransactionSuccessful();
                scoreDBAdapter.getDB().endTransaction();
                scoreDBAdapter.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    protected void onPostExecute(String v) {
        super.onPostExecute(v);
    }
}

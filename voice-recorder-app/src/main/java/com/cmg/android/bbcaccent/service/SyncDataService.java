package com.cmg.android.bbcaccent.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.data.sqlite.freestyle.PhonemeScoreDBAdapter;
import com.cmg.android.bbcaccent.data.sqlite.freestyle.ScoreDBAdapter;
import com.cmg.android.bbcaccent.data.dto.UserProfile;
import com.cmg.android.bbcaccent.http.PhonemeScoreAsync;
import com.cmg.android.bbcaccent.http.UserVoiceModelAsync;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class SyncDataService extends Service {
    public SyncDataService() {
    }
    private static String PARA_USERNAME = "username";
    private static String PARA_VERSION = "version";
    private static String PARA_ACTION = "action";
    private static String LIST_USER_VOICE_MODEL = "uservoicemodel";
    private static String LIST_PHONEME_SCORE = "phonemescore";
    /**
     *
     * @param intent
     * @return user profile
     */
    public UserProfile getUserProfile(Intent intent){
        Gson gson = new Gson();
        String jsonProfile = intent.getExtras().getString("jsonProfile");
        UserProfile userProfile = gson.fromJson(jsonProfile,UserProfile.class);
        return userProfile;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        UserProfile profile = getUserProfile(intent);
        String username = profile.getUsername();
        SimpleAppLog.info("start sync data with " + username);
        syncUserVoiceModel(username);
        syncPhonemeScore(username);
        return Service.START_NOT_STICKY;
    }

    public void syncUserVoiceModel(String username){
        try {
            ScoreDBAdapter scoreDBAdapter = new ScoreDBAdapter();
            scoreDBAdapter.open();
            int version = scoreDBAdapter.getLastedVersion(username);
            scoreDBAdapter.close();
            SimpleAppLog.info("start sync user voice model with " + username + " and current max version : " + version);
            Map<String, String> params = new HashMap<String, String>();
            params.put(PARA_USERNAME,username);
            params.put(PARA_VERSION,String.valueOf(version));
            params.put(PARA_ACTION, LIST_USER_VOICE_MODEL);
            UserVoiceModelAsync uAsync = new UserVoiceModelAsync(this,getResources().getString(R.string.sync_data_url));
            uAsync.execute(params);
        }catch (Exception e){
            SimpleAppLog.error("Could not sync user voice model",e);
        }
    }
    public void syncPhonemeScore(String username){
        try {
            PhonemeScoreDBAdapter phonemeScoreDBAdapter = new PhonemeScoreDBAdapter();
            phonemeScoreDBAdapter.open();
            int version = phonemeScoreDBAdapter.getLastedVersion(username);
            phonemeScoreDBAdapter.close();
            Map<String, String> params = new HashMap<String, String>();
            params.put(PARA_USERNAME,username);
            params.put(PARA_VERSION,String.valueOf(version));
            params.put(PARA_ACTION,LIST_PHONEME_SCORE);
            PhonemeScoreAsync pAsync = new PhonemeScoreAsync(this,getResources().getString(R.string.sync_data_url));
            pAsync.execute(params);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

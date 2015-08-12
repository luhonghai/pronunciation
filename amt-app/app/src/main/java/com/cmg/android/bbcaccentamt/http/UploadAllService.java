package com.cmg.android.bbcaccentamt.http;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.AudioFormat;
import android.os.Environment;
import android.os.IBinder;

import com.cmg.android.bbcaccentamt.AppLog;
import com.cmg.android.bbcaccentamt.R;
import com.cmg.android.bbcaccentamt.activity.fragment.Preferences;
import com.cmg.android.bbcaccentamt.common.FileCommon;
import com.cmg.android.bbcaccentamt.data.DatabaseHandlerSentence;
import com.cmg.android.bbcaccentamt.data.SentenceModel;
import com.cmg.android.bbcaccentamt.data.UserProfile;
import com.cmg.android.bbcaccentamt.dsp.AndroidAudioInputStream;
import com.cmg.android.bbcaccentamt.utils.AndroidHelper;
import com.cmg.android.bbcaccentamt.utils.DeviceUuidFactory;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.tarsos.dsp.io.TarsosDSPAudioFormat;

public class UploadAllService extends Service {

    UserProfile profile;
    private static final String AUDIO_RECORDER_OUTPUT_TYPE = ".wav";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + AUDIO_RECORDER_FOLDER;
    public UploadAllService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public UserProfile getUserProfile(Intent intent){
        Gson gson = new Gson();
        String jsonProfile = intent.getExtras().getString("jsonProfile");
        UserProfile userProfile = gson.fromJson(jsonProfile,UserProfile.class);
        return userProfile;
    }

    public List<SentenceModel> getListSentence(){
        DatabaseHandlerSentence databaseHandlerSentence=new DatabaseHandlerSentence(this);
        List<SentenceModel> sentenceModels=databaseHandlerSentence.getAllSentenceUpload();
        Map<String, String> params = new HashMap<String, String>();
        return sentenceModels;
    }
    private String getTmpDir() {
        PackageManager m = this.getApplicationContext().getPackageManager();
        String s = this.getApplicationContext().getPackageName();
        try {
            PackageInfo p = m.getPackageInfo(s, 0);
            return p.applicationInfo.dataDir + File.separator + AUDIO_RECORDER_FOLDER;
        } catch (PackageManager.NameNotFoundException e) {
            return Environment.getExternalStorageDirectory().getPath() + File.separator + AUDIO_RECORDER_FOLDER;
        }
    }

    public String getTmpDir(String id,String name) {
        PackageManager m = this.getApplicationContext().getPackageManager();
        String s = this.getApplicationContext().getPackageName();
        try {
            PackageInfo p = m.getPackageInfo(s, 0);
            return new File(getTmpDir(),name + id + AUDIO_RECORDER_OUTPUT_TYPE).getAbsolutePath();
        }catch (PackageManager.NameNotFoundException e){
            return Environment.getExternalStorageDirectory().getPath() + File.separator + AUDIO_RECORDER_FOLDER;
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        profile = getUserProfile(intent);
        List<SentenceModel> sentenceModels=getListSentence();
        List<Map<String, String> > list = new ArrayList<Map<String, String>>();
        for(int i=0;i<sentenceModels.size();i++) {
            if(sentenceModels.get(i).getStatus()==-1) {
                String id=sentenceModels.get(i).getID();
                String fileName = getTmpDir(id, profile.getUsername());
                File tmp = new File(fileName);
                if (tmp.exists()) {

                    if (profile != null) {
                        Gson gson = new Gson();
                        profile.setUuid(new DeviceUuidFactory(this).getDeviceUuid().toString());
                        UserProfile.UserLocation lc = new UserProfile.UserLocation();
                        Location location = AndroidHelper.getLastBestLocation(this);
                        if (location != null) {
                            lc.setLongitude(location.getLongitude());
                            lc.setLatitude(location.getLatitude());
                            AppLog.logString("Lat: " + lc.getLatitude() + ". Lon: " + lc.getLongitude());
                            profile.setLocation(lc);
                        }
                        Map<String, String> params = new HashMap<String, String>();
                        profile.setTime(System.currentTimeMillis());
                        params.put(FileCommon.PARA_FILE_NAME, tmp.getName());
                        params.put(FileCommon.PARA_FILE_PATH, tmp.getAbsolutePath());
                        params.put(FileCommon.PARA_FILE_TYPE, "audio/wav");
                        params.put("profile", gson.toJson(profile));
                        params.put("sentence", id);
                        list.add(params);
                    } else {
                        AppLog.logString("Could not get user profile");
                    }
                }
            }
        }
        if(list.size()> 0){
            UploaderAllAsync uploadTask = new UploaderAllAsync(this, getResources().getString(R.string.upload_url));
            uploadTask.execute(list);
        }
        return Service.START_NOT_STICKY;
    }



}

package com.cmg.android.bbcaccentamt.data;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.AsyncTask;
import android.os.Environment;

import com.cmg.android.bbcaccentamt.AppLog;
import com.cmg.android.bbcaccentamt.R;
import com.cmg.android.bbcaccentamt.activity.fragment.Preferences;
import com.cmg.android.bbcaccentamt.dsp.AndroidAudioInputStream;
import com.cmg.android.bbcaccentamt.http.ResponseData;
import com.cmg.android.bbcaccentamt.utils.FileHelper;
import com.cmg.android.bbcaccentamt.utils.SimpleAppLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;

import be.tarsos.dsp.io.TarsosDSPAudioFormat;

/**
 * Created by luhonghai on 4/10/15.
 */
public class DatabasePrepare {

    private final Context context;

    private final OnPrepraredListener prepraredListener;
    private static final String AUDIO_RECORDER_OUTPUT_TYPE = ".wav";
    private static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";

    public DatabasePrepare(Context context, OnPrepraredListener prepraredListener) {
        this.context = context;
        this.prepraredListener = prepraredListener;
    }

    public interface OnPrepraredListener {
        public void onComplete();
    }

    public void prepare() {
        loadDatabase();
        prepraredListener.onComplete();
    }

    public void prepareAsync() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                prepare();
                return null;
            }
        }.execute();
    }
    public static String properCase (String inputVal) {
        if (inputVal.length() == 0) return "";

        if (inputVal.length() == 1) return inputVal.toUpperCase();
        return inputVal.substring(0,1).toUpperCase()
                + inputVal.substring(1).toLowerCase();
    }
    private String getTmpDir() {
        PackageManager m = context.getPackageManager();
        String s = context.getPackageName();
        try {
            PackageInfo p = m.getPackageInfo(s, 0);
            return p.applicationInfo.dataDir + File.separator + AUDIO_RECORDER_FOLDER;
        } catch (PackageManager.NameNotFoundException e) {
            return Environment.getExternalStorageDirectory().getPath() + File.separator + AUDIO_RECORDER_FOLDER;
        }
    }
    public String getTmpDir(String id,String name) {
        PackageManager m = context.getPackageManager();
        String s = context.getPackageName();
        try {
            PackageInfo p = m.getPackageInfo(s, 0);
            return new File(getTmpDir(),name + id + AUDIO_RECORDER_OUTPUT_TYPE).getAbsolutePath();
        }catch (PackageManager.NameNotFoundException e){
            return Environment.getExternalStorageDirectory().getPath() + File.separator + AUDIO_RECORDER_FOLDER;
        }

    }

    private void loadTranscription(){
        File sentenceDbFolder = new File(FileHelper.getApplicationDir(context), "databases");
        if (!sentenceDbFolder.exists()) {
            sentenceDbFolder.mkdirs();
        }
        File sentenceDb = new File(sentenceDbFolder, "sentencesManager");
        if (!sentenceDb.exists()) {
            SimpleAppLog.info("Try to preload sqlite database");
            try {
                FileUtils.copyInputStreamToFile(context.getAssets().open("db/sentencesManager"), sentenceDb);
            } catch (Exception e) {
                SimpleAppLog.error("Could not save database from asset",e);
            }
        }
        DatabaseHandlerSentence dbHandleStc=new DatabaseHandlerSentence(context);
        File tmpFile = new File(FileUtils.getTempDirectory(), "transcriptions.json");
        try {
            if (tmpFile.exists()) FileUtils.forceDelete(tmpFile);
            UserProfile profile = Preferences.getCurrentProfile(context);
            String name= profile.getUsername();
            int lengh=dbHandleStc.getAll().getColumnCount();
            int version = dbHandleStc.getLastedVersion();
            String requestUrl = context.getString(R.string.transcription_url)
                    + "?action=list&data="
                    + URLEncoder.encode(profile.getUsername(),"UTF-8")
                    +"&version="+version;
            long start = System.currentTimeMillis();
            FileUtils.copyURLToFile(new URL(requestUrl), tmpFile);
            long end = System.currentTimeMillis();
            SimpleAppLog.info("Time" + (end - start));
            if (tmpFile.exists()) {
                String rawJson = FileUtils.readFileToString(tmpFile, "UTF-8");
                SimpleAppLog.info("Transcription json: " + rawJson);
                Gson gson = new Gson();
                ResponseData data = gson.fromJson(rawJson, ResponseData.class);
                if (data.transcriptions != null && data.transcriptions.size() >0 ) {

                    for (int i = 0; i < data.transcriptions.size(); i++) {
                        Transcription temp = data.transcriptions.get(i);
                        SentenceModel model = new SentenceModel();
                        model.setID(temp.getId());
                        model.setVersion(temp.getVersion());
                        model.setIsDeleted(temp.isDeteted());
                        model.setSentence(temp.getSentence());
                        dbHandleStc.deleteSentence(model);
                        dbHandleStc.addSentence(model);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String fileName(String id, String account){
        DatabaseHandlerSentence dbHandleStc=new DatabaseHandlerSentence(context);
        String fileName = "";
        try {
            RecorderSentenceModel recorderSentenceModel= dbHandleStc.getRecorderSentence(id,account);
            fileName = recorderSentenceModel.getFileName();
        }catch(Exception e){

        }
        return fileName;

    }

    private void loadRecordSentence(){
        DatabaseHandlerSentence dbHandleStc=new DatabaseHandlerSentence(context);
        File tmpFile = new File(FileUtils.getTempDirectory(), "recorder.json");
        try {
            if (tmpFile.exists()) FileUtils.forceDelete(tmpFile);
            UserProfile profile = Preferences.getCurrentProfile(context);
            String name= profile.getUsername();
            int version = dbHandleStc.getLastedVersionRecorder();
            String requestUrl = context.getString(R.string.recorder_url)
                    + "?action=listbyclient&data="
                    + URLEncoder.encode(name,"UTF-8")
                    +"&version="+version;
            long start = System.currentTimeMillis();
            FileUtils.copyURLToFile(new URL(requestUrl), tmpFile);
            long end = System.currentTimeMillis();
            SimpleAppLog.info("Time" + (end - start));
            if (tmpFile.exists()) {
                String rawJson = FileUtils.readFileToString(tmpFile, "UTF-8");
                SimpleAppLog.info("recorder json: " + rawJson);
                Gson gson = new Gson();
                ResponseDataRecorded data = gson.fromJson(rawJson, ResponseDataRecorded.class);
                if (data.RecordedSentences != null && data.RecordedSentences.size() >0 ) {
                    for (int i = 0; i < data.RecordedSentences.size(); i++) {
                        RecordedSentence temp = data.RecordedSentences.get(i);
                        String fileName=fileName(temp.getSentenceId(),name);
                        RecorderSentenceModel recorderSentence=new RecorderSentenceModel();
                        recorderSentence.setID(temp.getSentenceId());
                        recorderSentence.setIdSentence(temp.getSentenceId());
                        recorderSentence.setAccount(temp.getAccount());
                        recorderSentence.setVersion(temp.getVersion());
                        recorderSentence.setStatus(temp.getStatus());
                        recorderSentence.setIsDelete(temp.isDeteted());
                        recorderSentence.setFileName(fileName);
                        if(temp.isDeteted()==1 || temp.getStatus()==0 ){
                            File file = new File(getTmpDir(temp.getSentenceId(),name));
                            FileUtils.forceDelete(file);
                        }
                        dbHandleStc.deleteRecorderSentence(recorderSentence);
                        dbHandleStc.addRecorderSentence(recorderSentence);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void loadDatabase() {
        loadTranscription();
        loadRecordSentence();

    }

    public class ResponseData extends com.cmg.android.bbcaccentamt.http.ResponseData<Transcription> {
       public List<Transcription> transcriptions;
    }
    public class ResponseDataRecorded extends com.cmg.android.bbcaccentamt.http.ResponseData<RecordedSentence> {
        public List<RecordedSentence> RecordedSentences;
    }
    public class Transcription  {

        private String id;

        private String sentence;

        private String author;

        private Date createdDate;

        private Date modifiedDate;

        private int status;

        private int version;

        private int isDeleted;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSentence() {
            return sentence;
        }

        public void setSentence(String sentence) {
            this.sentence = sentence;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public Date getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(Date createdDate) {
            this.createdDate = createdDate;
        }

        public Date getModifiedDate() {
            return modifiedDate;
        }

        public void setModifiedDate(Date modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public int isDeteted() {
            return isDeleted;
        }

        public void setIsDeteted(int isDeleted) {
            this.isDeleted = isDeleted;
        }

    }
    public class RecordedSentence  {

        private String id;

        private String account;

        private String fileName;

        private String author;

        private Date createdDate;

        private Date modifiedDate;

        private String sentenceId;

        private int status;

        private int version;

        private int isDeleted;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }


        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public Date getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(Date createdDate) {
            this.createdDate = createdDate;
        }

        public Date getModifiedDate() {
            return modifiedDate;
        }

        public void setModifiedDate(Date modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public int isDeteted() {
            return isDeleted;
        }

        public void setIsDeteted(int isDeleted) {
            this.isDeleted = isDeleted;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getSentenceId() {
            return sentenceId;
        }

        public void setSentenceId(String sentenceId) {
            this.sentenceId = sentenceId;
        }

    }
    }

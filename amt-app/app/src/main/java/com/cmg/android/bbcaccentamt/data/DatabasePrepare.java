package com.cmg.android.bbcaccentamt.data;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.AsyncTask;

import com.cmg.android.bbcaccentamt.R;
import com.cmg.android.bbcaccentamt.activity.fragment.Preferences;
import com.cmg.android.bbcaccentamt.dsp.AndroidAudioInputStream;
import com.cmg.android.bbcaccentamt.http.ResponseData;
import com.cmg.android.bbcaccentamt.utils.SimpleAppLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.FileUtils;

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
    private AndroidAudioInputStream audioStream;
    private AudioRecord audioInputStream;
    private int chanel;
    private int sampleRate;
    private int bufferSize;


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

    private void loadDatabase() {
        DatabaseHandlerSentence databaseHandlerSentence=new DatabaseHandlerSentence(context);
        TarsosDSPAudioFormat format = new TarsosDSPAudioFormat(sampleRate, 16, (chanel == AudioFormat.CHANNEL_IN_MONO) ? 1 : 2, true, false);
        audioStream = new AndroidAudioInputStream(this.context, audioInputStream, format, bufferSize);

        File tmpFile = new File(FileUtils.getTempDirectory(), "transcriptions.json");
        try {
            if (tmpFile.exists()) FileUtils.forceDelete(tmpFile);
            UserProfile profile = Preferences.getCurrentProfile(context);
            String name=profile.getUsername();
            String requestUrl = context.getString(R.string.transcription_url)
                    + "?action=list&data="
                    + URLEncoder.encode(profile.getUsername(),"UTF-8");
            FileUtils.copyURLToFile(new URL(requestUrl), tmpFile);
            if (tmpFile.exists()) {
                String rawJson = FileUtils.readFileToString(tmpFile, "UTF-8");
                SimpleAppLog.info("Transcription json: " + rawJson);
                Gson gson = new Gson();
                ResponseData data = gson.fromJson(rawJson, ResponseData.class);
                SimpleAppLog.info("Status: " + data.isStatus() + ". Message: " + data.getMessage());
                if (data.transcriptions != null && data.transcriptions.size() >0 ) {

                    databaseHandlerSentence.deleteAllSentence();
                            for (int i = 0; i < data.transcriptions.size(); i++) {
                                int n = 0;
                                SentenceModel sentenceModel=new SentenceModel();
                                String id=data.transcriptions.get(i).getId();
                                    String output=audioStream.getTmpDir(id,name);
                                    File dstFile = new File(output);
                                    if (dstFile.exists() && data.transcriptions.get(i).getStatus()==0) {
                                        sentenceModel.setSentence(data.transcriptions.get(i).getSentence());
                                        sentenceModel.setStatus(-1);
                                        sentenceModel.setID(data.transcriptions.get(i).getId());
                                        databaseHandlerSentence.addSentence(sentenceModel);
                                        n = n + 1;
                                    }
                                if (n == 0) {
                                    databaseHandlerSentence.addSentence(new SentenceModel(data.transcriptions.get(i).getId(), data.transcriptions.get(i).getSentence(), data.transcriptions.get(i).getStatus()));
                                }
                                }

                            }

            } else {
                SimpleAppLog.info("No transcription data found");
            }

            if(databaseHandlerSentence.getSentenceCount()==0){
                String[] item={"We didn't like that.",
                        "The company declined to identify the bidders but said it received offers in the high forty dollars per share.",
                        "There were two hundred fifty six issues advancing three hundred three declining and two hundred ninety two unchanged.",
                        "However investment income which represents thirteen percent of the industry's revenues rose eleven percent in the quarter reflecting gains from the rising stock market.",
                        "No one at the state department wants to let spies in.",
                        "A lengthy fight is likely.",
                        "The jury awarded mr. scharenberg one hundred five million dollars a figure based on ten years of profits had his project been completed.",
                        "To make them directly comparable each index is based on the close of nineteen sixty nine equaling one hundred.",
                        "He declined to name specific products.",
                        "If the dollar starts to plunge the fed may step up its defense of the currency."};
                for(int i=0;i<item.length;i++){
                    //databaseHandlerSentence.addSentence(new SentenceModel(i,item[i],1));
                }

            }

        } catch (Exception e) {
            SimpleAppLog.error("Could not fetch transcription",e);
        }
    }

    public class ResponseData extends com.cmg.android.bbcaccentamt.http.ResponseData<Transcription> {
        List<Transcription> transcriptions;
    }

    public class Transcription  {

        private String id;

        private String sentence;

        private String author;

        private Date createdDate;

        private Date modifiedDate;

        private int status;

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
    }
}

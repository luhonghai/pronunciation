package com.cmg.android.bbcaccent.data.dto;

import android.content.Context;

import com.cmg.android.bbcaccent.data.sqlite.PhonemeScoreDBAdapter;
import com.cmg.android.bbcaccent.data.sqlite.ScoreDBAdapter;
import com.cmg.android.bbcaccent.utils.FileHelper;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.google.gson.Gson;
import com.luhonghai.litedb.LiteEntity;
import com.luhonghai.litedb.annotation.LiteColumn;
import com.luhonghai.litedb.annotation.LiteTable;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by luhonghai on 09/10/2015.
 */
@LiteTable(name = "pronunciation")
public class PronunciationScore extends LiteEntity {

    @LiteColumn
    private String word;

    @LiteColumn
    private float score;

    @LiteColumn(name = "data_id")
    private String dataId;

    @LiteColumn
    private Date timestamp;

    @LiteColumn
    private String username;

    @LiteColumn
    private int version;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public Date getTimestamp() {
        if (timestamp == null) return new Date(System.currentTimeMillis());
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserVoiceModel(Context context) throws Exception {
        if (dataId == null || dataId.length() == 0) return null;
        File modelSource = new File(FileHelper.getPronunciationScoreDir(context), dataId + FileHelper.JSON_EXTENSION);
        if (modelSource.exists()) {
            return FileUtils.readFileToString(modelSource, "UTF-8");
        } else {
            UserVoiceModel model = new UserVoiceModel();
            ScoreDBAdapter scoreDBAdapter = new ScoreDBAdapter();
            PronunciationScore tempScore = scoreDBAdapter.getByDataID(dataId);
            model.setAudioFile(new File(FileHelper.getPronunciationScoreDir(context), dataId + FileHelper.WAV_EXTENSION).getAbsolutePath());
            model.setScore(tempScore.getScore());
            model.setWord(tempScore.getWord());
            SphinxResult result = new SphinxResult();
            PhonemeScoreDBAdapter phonemeScoreDBAdapter = new PhonemeScoreDBAdapter();
            phonemeScoreDBAdapter.open();
            List<SphinxResult.PhonemeScore> scoreList = phonemeScoreDBAdapter.getByDataID(dataId);
            phonemeScoreDBAdapter.close();
            result.setPhonemeScores(scoreList);
            model.setResult(result);
            Gson gson = new Gson();
            String json = gson.toJson(model);
            SimpleAppLog.debug("json file get from database : " + json);
            return json;
        }
    }
}

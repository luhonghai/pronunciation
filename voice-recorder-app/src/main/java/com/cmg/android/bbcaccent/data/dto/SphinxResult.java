package com.cmg.android.bbcaccent.data.dto;

import com.luhonghai.litedb.LiteEntity;
import com.luhonghai.litedb.annotation.LiteColumn;
import com.luhonghai.litedb.annotation.LiteTable;

import java.util.Date;
import java.util.List;

/**
 * Created by luhonghai on 12/22/14.
 */
public class SphinxResult {

    public static class Phoneme {

        private String name;

        private int count;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    public static class PhonemeScoreUnit {
        public static final int BEEP_PHONEME = 3;
        public static final int NEIGHBOR = 2;
        public static final int MATCHED = 1;
        public static final int NOT_MATCH = 0;

        private int index;
        private int type;
        private String name;
        private int count;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
    @LiteTable(name = "score")
    public static class PhonemeScore extends LiteEntity {

        @LiteColumn(name = "index_phoneme")
        private int index;

        @LiteColumn(name = "phoneme")
        private String name;

        @LiteColumn(name = "ipa")
        private String ipa;

        @LiteColumn(name = "score")
        private float totalScore;

        @LiteColumn
        private String username;

        @LiteColumn
        private int version;

        @LiteColumn
        private Date timestamp;

        @LiteColumn(name = "data_id")
        private String userVoiceId;

        private long time;

        private List<PhonemeScoreUnit> phonemes;

        public String getUserVoiceId() {
            return userVoiceId;
        }

        public void setUserVoiceId(String userVoiceId) {
            this.userVoiceId = userVoiceId;
        }
        public long getTime() {
            if (timestamp != null) return timestamp.getTime();
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getName() {
            if (name == null) return "";
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public float getTotalScore() {
            return totalScore;
        }

        public void setTotalScore(float totalScore) {
            this.totalScore = totalScore;
        }

        public List<PhonemeScoreUnit> getPhonemes() {
            return phonemes;
        }

        public void setPhonemes(List<PhonemeScoreUnit> phonemes) {
            this.phonemes = phonemes;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }

        public String getIpa() {
            return ipa;
        }

        public void setIpa(String ipa) {
            this.ipa = ipa;
        }
    }

    private float score;

    private List<String> correctPhonemes;

    private List<Phoneme> bestPhonemes;

    private List<PhonemeScore> phonemeScores;

    public List<String> getCorrectPhonemes() {
        return correctPhonemes;
    }

    public void setCorrectPhonemes(List<String> correctPhonemes) {
        this.correctPhonemes = correctPhonemes;
    }

    public List<Phoneme> getBestPhonemes() {
        return bestPhonemes;
    }

    public void setBestPhonemes(List<Phoneme> bestPhonemes) {
        this.bestPhonemes = bestPhonemes;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public List<PhonemeScore> getPhonemeScores() {
        return phonemeScores;
    }

    public void setPhonemeScores(List<PhonemeScore> phonemeScores) {
        this.phonemeScores = phonemeScores;
    }

}

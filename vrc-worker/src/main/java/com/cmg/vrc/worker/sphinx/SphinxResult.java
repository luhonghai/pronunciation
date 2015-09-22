package com.cmg.vrc.worker.sphinx;

import java.io.Serializable;

import java.util.List;

public class SphinxResult implements Serializable {

    private static final long serialVersionUID = 4174818522405159165L;

    public static class Phoneme implements Serializable {

        private static final long serialVersionUID = -3166773367201536644L;

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

    public static class PhonemeScoreUnit implements Serializable {

        private static final long serialVersionUID = -1913174839753440983L;

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

    public static class PhonemeScore implements Serializable {

        private static final long serialVersionUID = 5265967234899304245L;

        private int index;
        private String name;
        private float totalScore;
        private long time;
        private String username;
        private int version;
        private String userVoiceId;
        private List<PhonemeScoreUnit> phonemes;
        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }
        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
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
        public String getUserVoiceId() {
            return userVoiceId;
        }

        public void setUserVoiceId(String userVoiceId) {
            this.userVoiceId = userVoiceId;
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

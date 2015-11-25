package com.cmg.vrc.sphinx;

import java.util.List;

public class SphinxResult {

    public List<String> getRawBestPhonemes() {
        return rawBestPhonemes;
    }

    public void setRawBestPhonemes(List<String> rawBestPhonemes) {
        this.rawBestPhonemes = rawBestPhonemes;
    }

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

    public static class PhonemeScore {
        private int index;
        private String name;
        private float totalScore;
        private long time;
        private String username;
        private int version;
        private String userVoiceId;
        private List<PhonemeScoreUnit> phonemes;

        private String ipa;

        public String getIpa() {
            return ipa;
        }

        public void setIpa(String ipa) {
            this.ipa = ipa;
        }



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

    private List<String> rawBestPhonemes;

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

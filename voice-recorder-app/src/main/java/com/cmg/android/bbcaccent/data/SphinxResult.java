package com.cmg.android.bbcaccent.data;

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
        private List<PhonemeScoreUnit> phonemes;

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

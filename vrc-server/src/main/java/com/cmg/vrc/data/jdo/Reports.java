package com.cmg.vrc.data.jdo;

import java.util.List;

/**
 * Created by CMGT400 on 9/29/2015.
 */
public class Reports {
        private int studentScoreLesson;
        private int classAvgScoreLesson;
        private List<Integer> wordStudentScore;
        private List<Integer> wordClassScore;
        private List<String> word;
        private List<Integer> phonemesStudentScore;
        private List<Integer> phonemesClassScore;
        private List<String> phonemes;
        private String sessionId;
        private String dateCreated;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getStudentScoreLesson() {
            return studentScoreLesson;
        }

        public void setStudentScoreLesson(int studentScoreLesson) {
            this.studentScoreLesson = studentScoreLesson;
        }

        public int getClassAvgScoreLesson(){
            return classAvgScoreLesson;
        }

        public void setClassAvgScoreLesson(int classAvgScoreLesson) {
            this.classAvgScoreLesson = classAvgScoreLesson;
        }


        public List<Integer> getWordStudentScore() {
            return wordStudentScore;
        }

        public void setWordStudentScore(List<Integer> wordStudentScore) {
            this.wordStudentScore = wordStudentScore;
        }

        public List<Integer> getWordClassScore() {
            return wordClassScore;
        }

        public void setWordClassScore(List<Integer> wordClassScore) {
            this.wordClassScore = wordClassScore;
        }
        public List<String> getWord() {
            return word;
        }

        public void setWord(List<String> word) {
            this.word = word;
        }


        public List<Integer> getPhonemesStudentScore() {
            return phonemesStudentScore;
        }

        public void setPhonemesStudentScore(List<Integer> phonemesStudentScore) {
            this.phonemesStudentScore = phonemesStudentScore;
        }

        public List<Integer> getPhonemesClassScore() {
            return phonemesClassScore;
        }

        public void setPhonemesClassScore(List<Integer> phonemesClassScore) {
            this.phonemesClassScore = phonemesClassScore;
        }

        public List<String> getPhonemes() {
            return phonemes;
        }

        public void setPhonemes(List<String> phonemes) {
            this.phonemes = phonemes;
        }

}

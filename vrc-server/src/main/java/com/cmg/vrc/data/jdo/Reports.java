package com.cmg.vrc.data.jdo;

import java.util.List;

/**
 * Created by CMGT400 on 9/29/2015.
 */
public class Reports {
        private int studentScoreLesson;
        private int classAvgScoreLesson;
        private List<Float> wordStudentScore;
        private List<Float> wordClassScore;
        private List<String> word;
        private List<Float> phonemesStudentScore;
        private List<Float> phonemesClassScore;
        private List<String> phonemes;

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


        public List<Float> getWordStudentScore() {
            return wordStudentScore;
        }

        public void setWordStudentScore(List<Float> wordStudentScore) {
            this.wordStudentScore = wordStudentScore;
        }

        public List<Float> getWordClassScore() {
            return wordClassScore;
        }

        public void setWordClassScore(List<Float> wordClassScore) {
            this.wordClassScore = wordClassScore;
        }
        public List<String> getWord() {
            return word;
        }

        public void setWord(List<String> word) {
            this.word = word;
        }


        public List<Float> getPhonemesStudentScore() {
            return phonemesStudentScore;
        }

        public void setPhonemesStudentScore(List<Float> phonemesStudentScore) {
            this.phonemesStudentScore = phonemesStudentScore;
        }

        public List<Float> getPhonemesClassScore() {
            return phonemesClassScore;
        }

        public void setPhonemesClassScore(List<Float> phonemesClassScore) {
            this.phonemesClassScore = phonemesClassScore;
        }

        public List<String> getPhonemes() {
            return phonemes;
        }

        public void setPhonemes(List<String> phonemes) {
            this.phonemes = phonemes;
        }

}

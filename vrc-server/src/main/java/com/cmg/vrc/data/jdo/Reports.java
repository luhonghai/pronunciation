package com.cmg.vrc.data.jdo;

import java.util.List;

/**
 * Created by CMGT400 on 9/29/2015.
 */
public class Reports {
    class Info{
        private String courseName;
        private String levelName;
        private String objectiveName;
        private String lessonName;
        private String completionDate;
        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public String getLevelName() {
            return levelName;
        }

        public void setLevelName(String levelName) {
            this.levelName = levelName;
        }

        public String getObjectiveName() {
            return objectiveName;
        }

        public void setObjectiveName(String objectiveName) {
            this.objectiveName = objectiveName;
        }

        public String getLessonName() {
            return lessonName;
        }

        public void setLessonName(String lessonName) {
            this.lessonName = lessonName;
        }

        public String getCompletionDate() {
            return completionDate;
        }

        public void setCompletionDate(String completionDate) {
            this.completionDate = completionDate;
        }

    }

    private int studentScoreLesson;
    private int classAvgScoreLesson;
    private List<Float> phonemesStudentScore;
    private List<Float> phonemesClassScore;
    private List<Float> wordStudentScore;
    private List<Float> wordClassScore;
    private List<String> phonemes;
    private List<String> word;


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

    public List<String> getPhonemes() {
        return phonemes;
    }

    public void setPhonemes(List<String> phonemes) {
        this.phonemes = phonemes;
    }

    public List<String> getWord() {
        return word;
    }

    public void setWord(List<String> word) {
        this.word = word;
    }
}

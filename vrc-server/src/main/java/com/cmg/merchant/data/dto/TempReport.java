package com.cmg.merchant.data.dto;

/**
 * Created by lantb on 2016-05-04.
 */
public class TempReport {
    private String idQuestion;
    private float score;
    private String completedDate;

    public String getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(String idQuestion) {
        this.idQuestion = idQuestion;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }
}

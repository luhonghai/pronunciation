package com.cmg.lesson.data.dto;

import com.cmg.lesson.data.jdo.Question;

import java.util.List;

/**
 * Created by lantb on 2015-10-07.
 */
public class QuestionDTO {
    public int draw;
    public Double recordsTotal;
    public Double recordsFiltered;

    private String message;

    private List<Question> listQuestion;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Question> getListQuestion() {
        return listQuestion;
    }

    public void setListQuestion(List<Question> listQuestion) {
        this.listQuestion = listQuestion;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public Double getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(Double recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public Double getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Double recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }


}

package com.cmg.lesson.data.dto.question;

import com.cmg.lesson.data.jdo.question.Question;
import com.cmg.lesson.data.jdo.question.WeightForPhoneme;

import java.util.List;

/**
 * Created by lantb on 2015-10-07.
 */
public class QuestionDTO {
    public int draw;
    public Double recordsTotal;
    public Double recordsFiltered;
    private String message;

    private List<Question> data;

    private List<WeightForPhoneme> listWeightPhoneme;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Question> getData() {
        return data;
    }

    public void setData(List<Question> data) {
        this.data = data;
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

    public List<WeightForPhoneme> getListWeightPhoneme() {
        return listWeightPhoneme;
    }

    public void setListWeightPhoneme(List<WeightForPhoneme> listWeightPhoneme) {
        this.listWeightPhoneme = listWeightPhoneme;
    }
}

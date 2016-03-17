package com.cmg.merchant.data.dto;

import com.cmg.lesson.data.dto.question.WeightDTO;

import java.util.List;

/**
 * Created by lantb on 2015-10-09.
 */
public class WeightPhonemesDTO {

    private String idQuestion;

    private String idWord;

    private List<WeightDTO> data;

    public List<WeightDTO> getData() {
        return data;
    }

    public void setData(List<WeightDTO> data) {
        this.data = data;
    }

    public String getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(String idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getIdWord() {
        return idWord;
    }

    public void setIdWord(String idWord) {
        this.idWord = idWord;
    }


}

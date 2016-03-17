package com.cmg.merchant.data.dto;

import com.cmg.lesson.data.dto.question.WeightDTO;

import java.util.List;

/**
 * Created by lantb on 2015-10-09.
 */
public class ListWordAddQuestion {

    private List<WeightPhonemesDTO> listWord;
    private String idLesson;

    public List<WeightPhonemesDTO> getListWord() {
        return listWord;
    }

    public void setListWord(List<WeightPhonemesDTO> listWord) {
        this.listWord = listWord;
    }

    public String getIdLesson() {
        return idLesson;
    }

    public void setIdLesson(String idLesson) {
        this.idLesson = idLesson;
    }
}

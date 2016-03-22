package com.cmg.merchant.data.dto;

import com.cmg.merchant.data.dto.WeightDTO;

import java.util.List;

/**
 * Created by lantb on 2015-10-09.
 */
public class WeightPhonemesDTO {

    private String nameWord;

    private String idWord;

    private List<WeightDTO> listWeightPhoneme;

    public String getIdWord() {
        return idWord;
    }

    public void setIdWord(String idWord) {
        this.idWord = idWord;
    }

    public String getNameWord() {
        return nameWord;
    }

    public void setNameWord(String nameWord) {
        this.nameWord = nameWord;
    }

    public List<WeightDTO> getListWeightPhoneme() {
        return listWeightPhoneme;
    }

    public void setListWeightPhoneme(List<WeightDTO> listWeightPhoneme) {
        this.listWeightPhoneme = listWeightPhoneme;
    }
}

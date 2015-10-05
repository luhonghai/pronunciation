package com.cmg.lesson.data.dto;

/**
 * Created by lantb on 2015-10-01.
 */
public class PhonemeDTO {
    public String getPhoneme() {
        return phoneme;
    }

    public void setPhoneme(String phoneme) {
        this.phoneme = phoneme;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private String phoneme;
    private int index;

    public PhonemeDTO(String phoneme, int index){
        this.phoneme = phoneme;
        this.index = index;
    }
}

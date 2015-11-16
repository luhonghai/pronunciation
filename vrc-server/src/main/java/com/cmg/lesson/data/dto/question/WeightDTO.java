package com.cmg.lesson.data.dto.question;

/**
 * Created by lantb on 2015-10-09.
 */
public class WeightDTO {
    private String phoneme;
    private int index;
    private float weight;

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

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


}

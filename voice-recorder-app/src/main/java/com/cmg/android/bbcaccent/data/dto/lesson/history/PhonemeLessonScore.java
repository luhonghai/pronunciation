package com.cmg.android.bbcaccent.data.dto.lesson.history;

import com.cmg.android.bbcaccent.data.dto.BaseLessonEntity;
import com.luhonghai.litedb.annotation.LiteColumn;
import com.luhonghai.litedb.annotation.LiteTable;

@LiteTable(allowedParent = BaseLessonEntity.class)
public class PhonemeLessonScore extends BaseLessonEntity {

    @LiteColumn
    private String idUserLessonHistory;

    @LiteColumn
    private String phoneme;

    @LiteColumn
    private int index;

    @LiteColumn
    private float totalScore;

    @LiteColumn
    private String idCountry;

    public String getIdUserLessonHistory() {
        return idUserLessonHistory;
    }

    public void setIdUserLessonHistory(String idUserLessonHistory) {
        this.idUserLessonHistory = idUserLessonHistory;
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

    public float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(float totalScore) {
        this.totalScore = totalScore;
    }

    public String getIdCountry() {
        return idCountry;
    }

    public void setIdCountry(String idCountry) {
        this.idCountry = idCountry;
    }
}

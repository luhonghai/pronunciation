package com.cmg.android.bbcaccent.data.dto.lesson.question;

import com.cmg.android.bbcaccent.data.dto.BaseLessonEntity;
import com.luhonghai.litedb.annotation.LiteColumn;
import com.luhonghai.litedb.annotation.LiteTable;

@LiteTable(allowedParent = BaseLessonEntity.class)
public class WordOfQuestion extends BaseLessonEntity {

    @LiteColumn
    private String idQuestion;

    @LiteColumn
    private String idWordCollection;

    public WordOfQuestion(){}

    public String getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(String idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getIdWordCollection() {
        return idWordCollection;
    }

    public void setIdWordCollection(String idWordCollection) {
        this.idWordCollection = idWordCollection;
    }
}

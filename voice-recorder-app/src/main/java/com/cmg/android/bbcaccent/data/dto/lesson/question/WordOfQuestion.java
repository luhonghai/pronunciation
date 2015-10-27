package com.cmg.android.bbcaccent.data.dto.lesson.question;

import com.cmg.android.bbcaccent.data.dto.BaseLessonEntity;
import com.luhonghai.litedb.annotation.LiteColumn;
import com.luhonghai.litedb.annotation.LiteTable;

@LiteTable
public class WordOfQuestion extends BaseLessonEntity {

    @LiteColumn
    private String idQuestion;

    @LiteColumn
    private String idWordCollection;

    public WordOfQuestion(){}

    public WordOfQuestion(String idQuestion, String idWordCollection, int version, boolean isDeleted){
        this.idQuestion = idQuestion;
        this.idWordCollection = idWordCollection;
        setVersion(version);
        setIsDeleted(isDeleted);
    }

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

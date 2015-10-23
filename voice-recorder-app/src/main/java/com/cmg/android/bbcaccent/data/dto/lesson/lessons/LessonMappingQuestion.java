package com.cmg.android.bbcaccent.data.dto.lesson.lessons;

import com.cmg.android.bbcaccent.data.dto.BaseLessonEntity;
import com.luhonghai.litedb.annotation.LiteColumn;
import com.luhonghai.litedb.annotation.LiteTable;

@LiteTable
public class LessonMappingQuestion extends BaseLessonEntity {

    @LiteColumn
    private String idLesson;

    @LiteColumn
    private String idQuestion;

    public String getIdLesson() {
        return idLesson;
    }

    public void setIdLesson(String idLesson) {
        this.idLesson = idLesson;
    }

    public String getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(String idQuestion) {
        this.idQuestion = idQuestion;
    }
}

package com.cmg.android.bbcaccent.data.dto.lesson.history;

import com.cmg.android.bbcaccent.data.dto.BaseLessonEntity;
import com.luhonghai.litedb.annotation.LiteColumn;
import com.luhonghai.litedb.annotation.LiteTable;

@LiteTable
public class SessionScore extends BaseLessonEntity {

    @LiteColumn
    private String idUserLessonHistory;

    @LiteColumn
    private String sessionID;

    @LiteColumn
    private String idCountry;

    @LiteColumn
    private String idQuestion;

    @LiteColumn
    private String idLessonCollection;

    public String getIdUserLessonHistory() {
        return idUserLessonHistory;
    }

    public void setIdUserLessonHistory(String idUserLessonHistory) {
        this.idUserLessonHistory = idUserLessonHistory;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getIdCountry() {
        return idCountry;
    }

    public void setIdCountry(String idCountry) {
        this.idCountry = idCountry;
    }

    public String getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(String idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getIdLessonCollection() {
        return idLessonCollection;
    }

    public void setIdLessonCollection(String idLessonCollection) {
        this.idLessonCollection = idLessonCollection;
    }
}

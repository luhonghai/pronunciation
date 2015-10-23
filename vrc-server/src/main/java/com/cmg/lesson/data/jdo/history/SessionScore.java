package com.cmg.lesson.data.jdo.history;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Created by lantb on 2015-10-16.
 */
@PersistenceCapable(table = "SessionScore", detachable = "true")
public class SessionScore implements Mirrorable {

    @PrimaryKey
    private String id;

    @Persistent
    private String idUserLessonHistory;

    @Persistent
    private String sessionID;

    @Persistent
    private String idCountry;

    @Persistent
    private String idQuestion;

    @Persistent
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

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}

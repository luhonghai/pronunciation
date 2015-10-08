package com.cmg.lesson.data.jdo.question;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Created by lantb on 2015-10-07.
 */
@PersistenceCapable(table = "WordOfQuestion", detachable = "true")
public class WordOfQuestion implements Mirrorable{

    @PrimaryKey
    private String id;

    @Persistent
    private String idQuestion;

    @Persistent
    private String idWordCollection;

    @Persistent
    private int version;

    @Persistent
    private boolean isDeleted;

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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
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

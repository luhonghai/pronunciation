package com.cmg.lesson.data.jdo.question;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Created by lantb on 2015-10-08.
 */
@PersistenceCapable(table = "WeightForPhoneme", detachable = "true")
public class WeightForPhoneme implements Mirrorable{

    @PrimaryKey
    private String id;

    @Persistent
    private String idWordCollection;

    @Persistent
    private String idQuestion;

    @Persistent
    private String phoneme;

    @Persistent
    private int index;

    @Persistent
    private int weight;

    @Persistent
    private int version;

    @Persistent
    private boolean isDeleted;


    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(String idQuestion) {
        this.idQuestion = idQuestion;
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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getIdWordCollection() {
        return idWordCollection;
    }

    public void setIdWordCollection(String idWordCollection) {
        this.idWordCollection = idWordCollection;
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

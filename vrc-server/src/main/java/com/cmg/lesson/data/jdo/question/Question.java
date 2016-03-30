package com.cmg.lesson.data.jdo.question;

import com.cmg.lesson.data.jdo.word.WordCollection;
import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lantb on 2015-10-07.
 */
@PersistenceCapable(table = "QUESTION", detachable = "true")
public class Question implements Mirrorable {

    @PrimaryKey
    private String id;

    @Persistent
    @Column(jdbcType="VARCHAR", length=10000)
    private String name;

    @Persistent
    @Column(jdbcType="VARCHAR", length=10000)
    private String description;

    @Persistent
    private String type;

    @Persistent
    private boolean isDeleted;

    @Persistent
    private int version;

    @NotPersistent
    private ArrayList<WordCollection> words;

    public ArrayList<WordCollection> getWords() {
        return words;
    }

    public void setWords(ArrayList<WordCollection> words) {
        this.words = words;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    @Persistent
    private Date timeCreated;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}

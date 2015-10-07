package com.cmg.lesson.data.jdo;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Date;

/**
 * Created by lantb on 2015-10-07.
 */
@PersistenceCapable(table = "Question", detachable = "true")
public class Question implements Mirrorable {

    @PrimaryKey
    private String id;

    @Persistent
    @Column(jdbcType="VARCHAR", length=10000)
    private String name;

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

    @Persistent
    private boolean isDeleted;

    @Persistent
    private int version;

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    @Persistent
    private Date timeCreated;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {

    }
}

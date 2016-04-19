package com.cmg.lesson.data.jdo.objectives;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.*;
import java.util.Date;

/**
 * Created by lantb on 2015-10-19.
 */
@PersistenceCapable(table = "OBJECTIVE", detachable = "true")
public class Objective implements Mirrorable {
    @PrimaryKey
    private String id;

    @Persistent
    private String name;

    @Persistent
    @Column(jdbcType="VARCHAR", length=10000)
    private String description;

    @Persistent
    private Date dateCreated;

    @Persistent
    private int version;

    @Persistent
    private boolean isDeleted;

    @NotPersistent
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
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

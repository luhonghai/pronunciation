package com.cmg.lesson.data.jdo.lessons;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.*;
import java.util.Date;

/**
 * Created by lantb on 2015-10-13.
 */
@PersistenceCapable(table = "LESSONCOLLECTION", detachable = "true")
public class LessonCollection implements Mirrorable {
    @PrimaryKey
    private String id;

    @Persistent
    private String nameUnique;

    @Persistent
    private String name;

    @Persistent
    @Column(jdbcType="VARCHAR", length=10000)
    private String description;

    @Persistent
    private Date dateCreated;

    @Persistent
    private boolean isDeleted;

    @Persistent
    private int version;

    @NotPersistent
    private boolean idChecked = false;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Persistent
    private String title;


    public String getNameUnique() {
        return nameUnique;
    }

    public void setNameUnique(String nameUnique) {
        this.nameUnique = nameUnique;
    }

    public boolean isIdChecked() {
        return idChecked;
    }

    public void setIdChecked(boolean idChecked) {
        this.idChecked = idChecked;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

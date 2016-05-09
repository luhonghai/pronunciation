package com.cmg.lesson.data.jdo.course;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Date;

/**
 * Created by lantb on 2015-10-19.
 */
@PersistenceCapable(table = "COURSE", detachable = "true")
public class Course implements Mirrorable {

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
    private boolean isDeleted;

    @Persistent
    private int version;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id ;
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

    public Course(String id,String name, String description, boolean isDeleted, int version, Date dateCreated){
        this.id = id;
        this.name = name;
        this.description = description;
        this.isDeleted = isDeleted;
        this.version = version;
        this.dateCreated = dateCreated;
    }
    public Course(){}
}

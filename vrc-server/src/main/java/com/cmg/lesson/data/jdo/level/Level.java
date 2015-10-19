package com.cmg.lesson.data.jdo.level;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Date;

/**
 * Created by lantb on 2015-10-19.
 */
@PersistenceCapable(table = "Level", detachable = "true")
public class Level implements Mirrorable {

    @PrimaryKey
    private String id;

    @Persistent
    private String name;

    @Persistent
    private boolean isDemo;

    @Persistent
    private String description;

    @Persistent
    private Date dateCreated;

    @Persistent
    private boolean isDeleted;

    @Persistent
    private int version;

    @Persistent
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public boolean isDemo() {
        return isDemo;
    }

    public void setIsDemo(boolean isDemo) {
        this.isDemo = isDemo;
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
}

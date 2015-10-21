package com.cmg.lesson.data.jdo.country;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Created by lantb on 2015-10-19.
 */
@PersistenceCapable(table = "Country", detachable = "true")
public class Country implements Mirrorable {

    @PrimaryKey
    private String id;

    @Persistent
    private String name;

    @Persistent
    private String description;

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
        this.id = id;
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

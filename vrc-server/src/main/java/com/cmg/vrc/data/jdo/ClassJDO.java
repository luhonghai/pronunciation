package com.cmg.vrc.data.jdo;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Date;

/**
 * Created by CMGT400 on 1/5/2016.
 */
@PersistenceCapable(table = "CLASSJDO", detachable = "true")
public class ClassJDO implements Mirrorable {
    @PrimaryKey
    private String id;

    @Persistent
    private String className;

    @Persistent
    @Column(jdbcType="VARCHAR", length=MAX_VARCHAR_LENGTH)
    private String definition;

    @Persistent
    private Date createdDate;

    @Persistent
    private int version;

    @Persistent
    private boolean isDeleted;


    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id=id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
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
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }


}

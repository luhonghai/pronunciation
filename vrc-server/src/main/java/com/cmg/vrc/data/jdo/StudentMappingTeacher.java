package com.cmg.vrc.data.jdo;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

/**
 * Created by CMGT400 on 1/5/2016.
 */
@PersistenceCapable(table = "STUDENTMAPPINGTEACHER", detachable = "true")
public class StudentMappingTeacher implements Mirrorable {
    @PrimaryKey
    private String id;

    @PrimaryKey
    private String idStudent;

    @PrimaryKey
    private String idTeacher;

    @PrimaryKey
    private boolean isDeleted;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id=id;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(String idStudent) {
        this.idStudent = idStudent;
    }

    public String getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(String idTeacher) {
        this.idTeacher = idTeacher;
    }
}

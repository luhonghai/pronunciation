package com.cmg.vrc.data.jdo;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Date;

/**
 * Created by CMGT400 on 1/5/2016.
 */
@PersistenceCapable(table = "STUDENTMAPPINGTEACHER", detachable = "true")
public class StudentMappingTeacher implements Mirrorable {
    @PrimaryKey
    private String id;

    @Persistent
    private String studentName;

    @Persistent
    private String teacherName;

    @Persistent
    private String firstTeacherName;

    @Persistent
    private String lastTeacherName;

    @Persistent
    private boolean isDeleted;

    @Persistent
    private String mappingBy;

    @Persistent
    private String status;

    @Persistent
    private boolean licence;

    @Persistent
    private boolean isView;



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

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirstTeacherName() {
        return firstTeacherName;
    }

    public void setFirstTeacherName(String firstTeacherName) {
        this.firstTeacherName = firstTeacherName;
    }

    public String getLastTeacherName() {
        return lastTeacherName;
    }

    public void setLastTeacherName(String lastTeacherName) {
        this.lastTeacherName = lastTeacherName;
    }

    public boolean isLicence() {
        return licence;
    }

    public void setLicence(boolean licence) {
        this.licence = licence;
    }

    public String getMappingBy() {
        return mappingBy;
    }

    public void setMappingBy(String mappingBy) {
        this.mappingBy = mappingBy;
    }

    public boolean isView() {
        return isView;
    }

    public void setIsView(boolean isView) {
        this.isView = isView;
    }
}

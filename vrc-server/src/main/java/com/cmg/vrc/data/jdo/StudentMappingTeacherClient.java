package com.cmg.vrc.data.jdo;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Created by CMGT400 on 1/5/2016.
 */
public class StudentMappingTeacherClient implements Mirrorable {
    private String id;

    private String studentName;

    private String teacherName;

    private String firstNameTeacher;

    private String lastNameTeacher;

    private String status;

    private String company;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id=id;
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

    public String getFirstNameTeacher() {
        return firstNameTeacher;
    }

    public void setFirstNameTeacher(String firstNameTeacher) {
        this.firstNameTeacher = firstNameTeacher;
    }

    public String getLastNameTeacher() {
        return lastNameTeacher;
    }

    public void setLastNameTeacher(String lastNameTeacher) {
        this.lastNameTeacher = lastNameTeacher;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}

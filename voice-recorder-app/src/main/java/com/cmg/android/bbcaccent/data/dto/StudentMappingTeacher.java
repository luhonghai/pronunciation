package com.cmg.android.bbcaccent.data.dto;

/**
 * Created by CMGT400 on 1/25/2016.
 */
public class StudentMappingTeacher {
    private String id;

    private String studentName;

    private String teacherName;

    private boolean isDeleted;

    private String status;

    public StudentMappingTeacher(String id,String studentName,String teacherName,boolean isDeleted,String status){
        this.id=id;
        this.studentName=studentName;
        this.teacherName=teacherName;
        this.isDeleted=isDeleted;
        this.status=status;
    }
    public String getId() {
        return id;
    }

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
}

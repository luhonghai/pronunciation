package com.cmg.vrc.data.jdo;

import com.cmg.lesson.data.dto.objectives.IndexLesson;
import com.cmg.lesson.data.jdo.lessons.LessonCollection;

import java.util.List;

/**
 * Created by CMG Dev156 on 10/18/2015.
 */
public class TeacherOrStaffMappingCompany {
    private String fullName;
    private String firstName;
    private String lastName;
    private String password;

    private String role;
    private String[] idObjects;

    public String[] getIdObjects() {
        return idObjects;
    }

    public void setIdObjects(String[] idObjects) {
        this.idObjects = idObjects;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}

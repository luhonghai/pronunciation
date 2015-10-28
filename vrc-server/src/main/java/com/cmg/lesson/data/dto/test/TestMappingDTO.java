package com.cmg.lesson.data.dto.test;

import com.cmg.lesson.data.jdo.lessons.LessonCollection;

import java.util.List;

/**
 * Created by CMG Dev156 on 10/18/2015.
 */
public class TestMappingDTO {
    private String message;
    private String idTest;
    private String idLevel;
    private String idCourse;
    private String nameTest;
    private String descriptionTest;
    private Double percentPass;
    private List<String> idLessons;
    private List<LessonCollection> data;

    public String getIdTest() {
        return idTest;
    }

    public void setIdTest(String idTest) {
        this.idTest = idTest;
    }

    public String getNameTest() {
        return nameTest;
    }

    public void setNameTest(String nameTest) {
        this.nameTest = nameTest;
    }

    public String getDescriptionTest() {
        return descriptionTest;
    }

    public void setDescriptionTest(String descriptionTest) {
        this.descriptionTest = descriptionTest;
    }

    public Double getPercentPass() {
        return percentPass;
    }

    public void setPercentPass(Double percentPass) {
        this.percentPass = percentPass;
    }

    public List<String> getIdLessons() {
        return idLessons;
    }

    public void setIdLessons(List<String> idLessons) {
        this.idLessons = idLessons;
    }

    public List<LessonCollection> getData() {
        return data;
    }

    public void setData(List<LessonCollection> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIdLevel() {
        return idLevel;
    }

    public void setIdLevel(String idLevel) {
        this.idLevel = idLevel;
    }

    public String getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(String idCourse) {
        this.idCourse = idCourse;
    }
}

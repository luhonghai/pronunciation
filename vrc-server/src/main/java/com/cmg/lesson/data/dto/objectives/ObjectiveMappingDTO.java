package com.cmg.lesson.data.dto.objectives;

import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.lessons.LessonMappingQuestion;

import java.util.List;

/**
 * Created by CMG Dev156 on 10/18/2015.
 */
public class ObjectiveMappingDTO {
    private String message;
    private String idObjective;
    private String idLevel;
    private String idCourse;
    private String nameObj;
    private int index;
    private String descriptionObj;
    private List<IndexLesson> idLessons;
    private List<LessonCollection> data;

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

    public String getIdObjective() {
        return idObjective;
    }

    public void setIdObjective(String idObjective) {
        this.idObjective = idObjective;
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

    public String getNameObj() {
        return nameObj;
    }

    public void setNameObj(String nameObj) {
        this.nameObj = nameObj;
    }

    public String getDescriptionObj() {
        return descriptionObj;
    }

    public void setDescriptionObj(String descriptionObj) {
        this.descriptionObj = descriptionObj;
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<IndexLesson> getIdLessons() {
        return idLessons;
    }

    public void setIdLessons(List<IndexLesson> idLessons) {
        this.idLessons = idLessons;
    }

}

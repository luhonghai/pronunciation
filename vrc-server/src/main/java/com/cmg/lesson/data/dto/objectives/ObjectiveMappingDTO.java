package com.cmg.lesson.data.dto.objectives;

import com.cmg.lesson.data.jdo.lessons.LessonMappingQuestion;

import java.util.List;

/**
 * Created by CMG Dev156 on 10/18/2015.
 */
public class ObjectiveMappingDTO {
    private String idObjective;

    private String idLesson;

    private String message;

    private List<LessonMappingQuestion> data;

    public String getIdQuestion() {
        return idObjective;
    }

    public void setIdQuestion(String idQuestion) {
        this.idObjective = idQuestion;
    }

    public String getIdLesson() {
        return idLesson;
    }

    public void setIdLesson(String idLesson) {
        this.idLesson = idLesson;
    }

    public List<LessonMappingQuestion> getData() {
        return data;
    }

    public void setData(List<LessonMappingQuestion> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

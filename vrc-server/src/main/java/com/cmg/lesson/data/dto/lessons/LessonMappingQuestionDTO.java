package com.cmg.lesson.data.dto.lessons;

import com.cmg.lesson.data.jdo.lessons.LessonMappingQuestion;

import java.util.List;

/**
 * Created by CMG Dev156 on 10/18/2015.
 */
public class LessonMappingQuestionDTO {
    private String idQuestion;

    private String idLesson;

    private String message;

    private List<LessonMappingQuestion> data;

    public String getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(String idQuestion) {
        this.idQuestion = idQuestion;
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

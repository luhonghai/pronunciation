package com.cmg.lesson.data.dto.objectives;

import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.objectives.Objective;

import java.util.List;

/**
 * Created by CMG Dev156 on 10/14/2015.
 */
public class ObjectiveDTO {

    private String message;
    private List<Objective> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Objective> getData() {
        return data;
    }

    public void setData(List<Objective> data) {
        this.data = data;
    }


}

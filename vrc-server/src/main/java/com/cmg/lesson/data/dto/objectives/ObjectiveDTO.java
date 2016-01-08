package com.cmg.lesson.data.dto.objectives;

import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.objectives.Objective;

import java.util.List;

/**
 * Created by CMG Dev156 on 10/14/2015.
 */
public class ObjectiveDTO {
    public int draw;
    public Double recordsTotal;
    public Double recordsFiltered;
    private String message;
    private List<Objective> data;

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public Double getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(Double recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public Double getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Double recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

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

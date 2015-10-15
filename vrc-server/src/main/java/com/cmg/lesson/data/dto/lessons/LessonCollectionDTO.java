package com.cmg.lesson.data.dto.lessons;

import com.cmg.lesson.data.jdo.lessons.LessonCollection;

import java.util.List;

/**
 * Created by CMG Dev156 on 10/14/2015.
 */
public class LessonCollectionDTO {
    public int draw;
    public Double recordsTotal;
    public Double recordsFiltered;
    private String message;
    private List<LessonCollection> data;

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

    public List<LessonCollection> getData() {
        return data;
    }

    public void setData(List<LessonCollection> data) {
        this.data = data;
    }
}

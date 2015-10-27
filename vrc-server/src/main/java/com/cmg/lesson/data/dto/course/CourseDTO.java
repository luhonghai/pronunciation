package com.cmg.lesson.data.dto.course;

import com.cmg.lesson.data.dto.objectives.ObjectiveMappingDTO;
import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.lesson.data.jdo.test.Test;

import java.util.List;

/**
 * Created by lantb on 2015-10-21.
 */
public class CourseDTO {
    public int draw;
    public Double recordsTotal;
    public Double recordsFiltered;
    private String message;

    private List<Course> data;

    private List<Objective> listObj;

    private Test test;

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

    public List<Course> getData() {
        return data;
    }

    public void setData(List<Course> data) {
        this.data = data;
    }

    public List<Objective> getListObj() {
        return listObj;
    }

    public void setListObj(List<Objective> listObj) {
        this.listObj = listObj;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }
}

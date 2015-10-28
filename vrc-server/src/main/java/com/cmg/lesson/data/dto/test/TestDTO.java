package com.cmg.lesson.data.dto.test;

import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.lesson.data.jdo.test.Test;

import java.util.List;

/**
 * Created by CMG Dev156 on 10/14/2015.
 */
public class TestDTO {

    private String message;
    private List<Test> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Test> getData() {
        return data;
    }

    public void setData(List<Test> data) {
        this.data = data;
    }


}

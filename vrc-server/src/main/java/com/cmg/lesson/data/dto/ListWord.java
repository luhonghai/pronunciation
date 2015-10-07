package com.cmg.lesson.data.dto;

import com.cmg.lesson.data.jdo.WordCollection;

import java.util.List;

/**
 * Created by lantb on 2015-10-06.
 */
public class ListWord {
    public int draw;
    public Double recordsTotal;
    public Double recordsFiltered;
    public String message;
    public String error;
    List<WordCollection> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

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

    public List<WordCollection> getData() {
        return data;
    }

    public void setData(List<WordCollection> data) {
        this.data = data;
    }
}

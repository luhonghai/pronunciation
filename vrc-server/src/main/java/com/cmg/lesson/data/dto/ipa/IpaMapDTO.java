package com.cmg.lesson.data.dto.ipa;

import com.cmg.lesson.data.jdo.ipa.IpaMapArpabet;
import com.cmg.lesson.data.jdo.level.Level;

import java.util.List;

/**
 * Created by lantb on 2015-10-27.
 */
public class IpaMapDTO {

    public int draw;
    public Double recordsTotal;
    public Double recordsFiltered;
    private String message;

    private List<IpaMapArpabet> data;

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

    public List<IpaMapArpabet> getData() {
        return data;
    }

    public void setData(List<IpaMapArpabet> data) {
        this.data = data;
    }
}

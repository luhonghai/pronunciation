package com.cmg.lesson.data.dto;

import java.util.List;

/**
 * Created by lantb on 2015-10-06.
 */
public class ListWord {
    public int draw;
    public Double recordsTotal;
    public Double recordsFiltered;
    List<WordDTO> data;

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

    public List<WordDTO> getData() {
        return data;
    }

    public void setData(List<WordDTO> data) {
        this.data = data;
    }
}

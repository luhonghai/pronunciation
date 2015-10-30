package com.cmg.lesson.data.dto.level;

import com.cmg.lesson.data.dto.objectives.ObjectiveMappingDTO;
import com.cmg.lesson.data.dto.test.TestMappingDTO;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.lesson.data.jdo.question.Question;

import java.util.List;

/**
 * Created by lantb on 2015-10-20.
 */
public class LevelDTO {
    public int draw;
    public Double recordsTotal;
    public Double recordsFiltered;
    private String message;

    private List<Level> dataforDropdown;

    private List<Level> data;

    private List<ObjectiveMappingDTO> listObjMap;

    private List<TestMappingDTO> listTestMap;

    public List<TestMappingDTO> getListTestMap() {
        return listTestMap;
    }

    public void setListTestMap(List<TestMappingDTO> listTestMap) {
        this.listTestMap = listTestMap;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Level> getData() {
        return data;
    }

    public void setData(List<Level> data) {
        this.data = data;
    }


    public List<Level> getDataforDropdown() {
        return dataforDropdown;
    }

    public void setDataforDropdown(List<Level> dataforDropdown) {
        this.dataforDropdown = dataforDropdown;
    }

    public List<ObjectiveMappingDTO> getListObjMap() {
        return listObjMap;
    }

    public void setListObjMap(List<ObjectiveMappingDTO> listObjMap) {
        this.listObjMap = listObjMap;
    }
}

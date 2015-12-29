package com.cmg.lesson.data.dto.level;

import com.cmg.lesson.data.dto.objectives.IndexOBJ;

import java.util.List;

/**
 * Created by CMG Dev156 on 12/8/2015.
 */
public class LevelMappingObjDTO {
    private String message;
    private String idLevel;
    private int index;

    private List<IndexOBJ> obj;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIdLevel() {
        return idLevel;
    }

    public void setIdLevel(String idLevel) {
        this.idLevel = idLevel;
    }
    public List<IndexOBJ> getObj() {
        return obj;
    }

    public void setObj(List<IndexOBJ> obj) {
        this.obj = obj;
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

}

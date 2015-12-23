package com.cmg.lesson.data.dto.objectives;

import com.cmg.lesson.data.jdo.objectives.Objective;

import java.util.List;

/**
 * Created by CMG Dev156 on 10/14/2015.
 */
public class IndexOBJ {



    private String idObjects;
    private int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getIdObjects() {
        return idObjects;
    }

    public void setIdObjects(String idObjects) {
        this.idObjects = idObjects;
    }

}

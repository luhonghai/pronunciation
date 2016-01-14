package com.cmg.vrc.data.jdo;

import java.util.Date;

/**
 * Created by cmg on 01/09/15.
 */
public class Student {
    private String idClass;
    private String[] idObjects;


    public String getIdClass() {
        return idClass;
    }

    public void setIdClass(String idClass) {
        this.idClass = idClass;
    }

    public String[] getIdObjects() {
        return idObjects;
    }

    public void setIdObjects(String[] idObjects) {
        this.idObjects = idObjects;
    }
}

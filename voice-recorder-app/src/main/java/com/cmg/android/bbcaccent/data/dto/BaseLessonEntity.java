package com.cmg.android.bbcaccent.data.dto;

import com.luhonghai.litedb.annotation.LiteColumn;

public abstract class BaseLessonEntity {

    @LiteColumn(isPrimaryKey = true)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

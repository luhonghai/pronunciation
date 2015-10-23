package com.cmg.android.bbcaccent.data.dto;

import com.luhonghai.litedb.LiteEntity;
import com.luhonghai.litedb.annotation.LiteColumn;

public abstract class BaseLessonEntity {

    @LiteColumn(alias = LiteEntity._ID, isPrimaryKey = true)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

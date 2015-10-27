package com.cmg.android.bbcaccent.data.dto.lesson.lessons;

import com.cmg.android.bbcaccent.data.dto.BaseLessonEntity;
import com.luhonghai.litedb.annotation.LiteColumn;
import com.luhonghai.litedb.annotation.LiteTable;

import java.util.Date;

@LiteTable(allowedParent = BaseLessonEntity.class)
public class LessonCollection extends BaseLessonEntity {

    @LiteColumn
    private String name;

    @LiteColumn
    private String description;

    @LiteColumn
    private Date dateCreated;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

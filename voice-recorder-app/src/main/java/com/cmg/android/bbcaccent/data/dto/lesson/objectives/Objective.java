package com.cmg.android.bbcaccent.data.dto.lesson.objectives;

import com.cmg.android.bbcaccent.data.dto.BaseLessonEntity;
import com.luhonghai.litedb.annotation.LiteColumn;
import com.luhonghai.litedb.annotation.LiteTable;

import java.util.Date;

@LiteTable(allowedParent = BaseLessonEntity.class)
public class Objective extends BaseLessonEntity{

    @LiteColumn
    private String name;

    @LiteColumn
    private String description;

    @LiteColumn
    private Date dateCreated;

    private int score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        if (description == null) return "";
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

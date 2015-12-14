package com.cmg.android.bbcaccent.data.dto.lesson.level;

import com.cmg.android.bbcaccent.data.dto.BaseLessonEntity;
import com.luhonghai.litedb.annotation.LiteColumn;
import com.luhonghai.litedb.annotation.LiteTable;

import java.util.Date;

@LiteTable(allowedParent = BaseLessonEntity.class, name = "Level")
public class LessonLevel extends BaseLessonEntity {

    @LiteColumn
    private String name;

    @LiteColumn
    private boolean isDemo;

    @LiteColumn
    private boolean isDefaultActivated;

    @LiteColumn
    private String description;

    @LiteColumn
    private Date dateCreated;

    @LiteColumn
    private String color;

    private boolean active;

    private int score;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDemo() {
        return isDemo;
    }

    public void setIsDemo(boolean isDemo) {
        this.isDemo = isDemo;
    }

    public String getDescription() {
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isDefaultActivated() {
        return isDefaultActivated;
    }

    public void setIsDefaultActivated(boolean isDefaultActivated) {
        this.isDefaultActivated = isDefaultActivated;
    }
}

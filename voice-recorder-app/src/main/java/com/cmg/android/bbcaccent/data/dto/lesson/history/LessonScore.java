package com.cmg.android.bbcaccent.data.dto.lesson.history;

import com.luhonghai.litedb.LiteEntity;
import com.luhonghai.litedb.annotation.LiteColumn;
import com.luhonghai.litedb.annotation.LiteTable;

/**
 * Created by luhonghai on 29/10/2015.
 */
@LiteTable
public class LessonScore extends LiteEntity {

    @LiteColumn
    private String username;

    @LiteColumn
    private String countryId;

    @LiteColumn
    private String lessonCollectionId;

    @LiteColumn
    private String objectiveId;

    @LiteColumn
    private String levelId;

    @LiteColumn
    private int score;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getLessonCollectionId() {
        return lessonCollectionId;
    }

    public void setLessonCollectionId(String lessonCollectionId) {
        this.lessonCollectionId = lessonCollectionId;
    }

    public String getObjectiveId() {
        return objectiveId;
    }

    public void setObjectiveId(String objectiveId) {
        this.objectiveId = objectiveId;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

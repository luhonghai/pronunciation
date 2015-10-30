package com.cmg.android.bbcaccent.data.dto.lesson.history;

import com.luhonghai.litedb.LiteEntity;
import com.luhonghai.litedb.annotation.LiteColumn;
import com.luhonghai.litedb.annotation.LiteTable;

/**
 * Created by luhonghai on 29/10/2015.
 */
@LiteTable
public class LevelScore extends LiteEntity {

    @LiteColumn
    private String username;

    @LiteColumn
    private String countryId;

    @LiteColumn
    private String levelId;

    @LiteColumn
    private int score;

    @LiteColumn
    private boolean enable;

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

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}

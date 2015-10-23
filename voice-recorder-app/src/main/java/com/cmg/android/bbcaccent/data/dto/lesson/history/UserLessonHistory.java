package com.cmg.android.bbcaccent.data.dto.lesson.history;

import com.cmg.android.bbcaccent.data.dto.BaseLessonEntity;
import com.luhonghai.litedb.annotation.LiteColumn;
import com.luhonghai.litedb.annotation.LiteTable;

@LiteTable
public class UserLessonHistory extends BaseLessonEntity {

    @LiteColumn
    private String username;

    @LiteColumn
    private String word;

    @LiteColumn
    private double score;

    @LiteColumn
    private String recordedFile;

    @LiteColumn
    private String type;

    @LiteColumn
    private long serverTime;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getRecordedFile() {
        return recordedFile;
    }

    public void setRecordedFile(String recordedFile) {
        this.recordedFile = recordedFile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }
}

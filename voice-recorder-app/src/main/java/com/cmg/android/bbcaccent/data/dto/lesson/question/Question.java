package com.cmg.android.bbcaccent.data.dto.lesson.question;

import com.cmg.android.bbcaccent.data.dto.BaseLessonEntity;
import com.luhonghai.litedb.annotation.LiteColumn;
import com.luhonghai.litedb.annotation.LiteTable;

import java.util.Date;

@LiteTable
public class Question extends BaseLessonEntity {

    @LiteColumn
    private String name;

    @LiteColumn
    private Date timeCreated;

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }
}

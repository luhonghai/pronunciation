package com.cmg.android.bbcaccent.data.dto.lesson.course;

import com.cmg.android.bbcaccent.data.dto.BaseLessonEntity;
import com.luhonghai.litedb.annotation.LiteColumn;
import com.luhonghai.litedb.annotation.LiteTable;

@LiteTable(allowedParent = BaseLessonEntity.class)
public class LevelMappingObjectiveTest extends BaseLessonEntity {

    @LiteColumn
    private String idCourse;

    @LiteColumn
    private String idLevel;

    @LiteColumn
    private String idChild;

    @LiteColumn
    private boolean isTest;

    public String getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(String idCourse) {
        this.idCourse = idCourse;
    }

    public String getIdLevel() {
        return idLevel;
    }

    public void setIdLevel(String idLevel) {
        this.idLevel = idLevel;
    }

    public String getIdChild() {
        return idChild;
    }

    public void setIdChild(String idChild) {
        this.idChild = idChild;
    }

    public boolean isTest() {
        return isTest;
    }

    public void setIsTest(boolean isTest) {
        this.isTest = isTest;
    }
}

package com.cmg.android.bbcaccent.data.dto.lesson.test;

import com.cmg.android.bbcaccent.data.dto.BaseLessonEntity;
import com.luhonghai.litedb.annotation.LiteColumn;
import com.luhonghai.litedb.annotation.LiteTable;

@LiteTable
public class TestMapping extends BaseLessonEntity {

    @LiteColumn
    private String idTest;

    @LiteColumn
    private String idLessonCollection;

    public String getIdTest() {
        return idTest;
    }

    public void setIdTest(String idTest) {
        this.idTest = idTest;
    }

    public String getIdLessonCollection() {
        return idLessonCollection;
    }

    public void setIdLessonCollection(String idLessonCollection) {
        this.idLessonCollection = idLessonCollection;
    }
}

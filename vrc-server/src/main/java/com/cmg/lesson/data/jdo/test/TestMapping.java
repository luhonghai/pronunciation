package com.cmg.lesson.data.jdo.test;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Created by lantb on 2015-10-19.
 */
@PersistenceCapable(table = "TestMapping", detachable = "true")
public class TestMapping implements Mirrorable {

    @PrimaryKey
    private String id;

    @Persistent
    private String idTest;

    @Persistent
    private String idLessonCollection;

    @Persistent
    private int version;

    @Persistent
    private boolean isDeleted;


    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }


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

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}

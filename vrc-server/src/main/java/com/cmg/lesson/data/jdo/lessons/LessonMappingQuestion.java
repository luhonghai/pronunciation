package com.cmg.lesson.data.jdo.lessons;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Created by lantb on 2015-10-13.
 */
@PersistenceCapable(table = "LessonMappingQuestion", detachable = "true")
public class LessonMappingQuestion implements Mirrorable {

    @PrimaryKey
    private String id;

    @Persistent
    private String idLesson;

    @Persistent
    private String idQuestion;

    @Persistent
    private boolean isDeleted;

    @Persistent
    private int version;


    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}

package com.cmg.lesson.data.jdo.objectives;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Created by lantb on 2015-10-19.
 */
@PersistenceCapable(table = "OBJECTIVEMAPPING", detachable = "true")
public class ObjectiveMapping implements Mirrorable {

    @PrimaryKey
    private String id;

    @Persistent
    private String idObjective;

    @Persistent
    private String idLessonCollection;

    @Persistent
    private boolean isDeleted;

    @Persistent
    private int version;

    //add column index by nam.bui
    @Persistent
    private int index;

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getIdObjective() {
        return idObjective;
    }

    public void setIdObjective(String idObjective) {
        this.idObjective = idObjective;
    }

    public String getIdLessonCollection() {
        return idLessonCollection;
    }

    public void setIdLessonCollection(String idLessonCollection) {
        this.idLessonCollection = idLessonCollection;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

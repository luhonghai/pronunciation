package com.cmg.merchant.data.jdo;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Created by lantb on 2016-04-20.
 */
@PersistenceCapable(table = "TEACHERCOURSEHISTORY", detachable = "true")
public class TeacherCourseHistory implements Mirrorable {
    @PrimaryKey
    private String id;

    @Persistent
    private String idCourse;

    @Persistent
    private String pathAws;

    @Persistent
    private int version;

    @NotPersistent
    private String name;

    @NotPersistent
    private String urlDownload;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlDownload() {
        return urlDownload;
    }

    public void setUrlDownload(String urlDownload) {
        this.urlDownload = urlDownload;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(String idCourse) {
        this.idCourse = idCourse;
    }

    public String getPathAws() {
        return pathAws;
    }

    public void setPathAws(String pathAws) {
        this.pathAws = pathAws;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int index) {
        this.version = index;
    }
}

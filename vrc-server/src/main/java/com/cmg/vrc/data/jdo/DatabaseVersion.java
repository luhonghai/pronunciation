package com.cmg.vrc.data.jdo;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Date;

/**
 * Created by cmg on 06/07/15.
 */
@PersistenceCapable(table = "DATABASE_VERSION", detachable = "true")
public class DatabaseVersion implements Mirrorable {

    @PrimaryKey
    private String id;

    @Persistent
    private int version;

    @Persistent
    private boolean selected;

    @Persistent
    private String fileName;

    @Persistent
    private String admin;

    @Persistent
    @Column(jdbcType="VARCHAR", length=10000)
    private String lessonChange;

    @Persistent
    @Column(jdbcType="VARCHAR", length=10000)
    private String titleNotification;

    @Persistent
    private Date createdDate;

    @Persistent
    private Date selectedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getLessonChange() {
        return lessonChange;
    }

    public void setLessonChange(String lessonChange) {
        this.lessonChange = lessonChange;
    }

    public String getTitleNotification() {
        return titleNotification;
    }

    public void setTitleNotification(String titleNotification) {
        this.titleNotification = titleNotification;
    }
}

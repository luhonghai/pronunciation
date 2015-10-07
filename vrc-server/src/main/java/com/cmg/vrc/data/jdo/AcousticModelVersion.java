package com.cmg.vrc.data.jdo;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Date;

/**
 * Created by cmg on 05/10/2015.
 */
@PersistenceCapable(table = "ACOUSTIC_MODEL_VERSION", detachable = "true")
public class AcousticModelVersion implements Mirrorable {

    @PrimaryKey
    private String id;

    @Persistent
    private int version;

    @Persistent
    private String projectName;

    @Persistent
    private boolean selected;

    @Persistent
    private String fileName;

    @Persistent
    private String dictionaryVersionId;

    @Persistent
    private String languageModelVersionId;

    @Persistent
    private String logFileName;

    @Persistent
    private String admin;

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

    public String getDictionaryVersionId() {
        return dictionaryVersionId;
    }

    public void setDictionaryVersionId(String dictionaryVersionId) {
        this.dictionaryVersionId = dictionaryVersionId;
    }

    public String getLanguageModelVersionId() {
        return languageModelVersionId;
    }

    public void setLanguageModelVersionId(String languageModelVersionId) {
        this.languageModelVersionId = languageModelVersionId;
    }

    public String getLogFileName() {
        return logFileName;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}

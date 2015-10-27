package com.cmg.lesson.data.jdo.ipa;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Date;

/**
 * Created by lantb on 2015-10-26.
 */
@PersistenceCapable(table = "IpaMapArpabet", detachable = "true")
public class IpaMapArpabet implements Mirrorable{

    @PrimaryKey
    private String id;

    @Persistent
    private String arpabet;

    @Persistent
    private String ipa;

    @Persistent
    private String color;

    @Persistent
    private String type;

    @Persistent
    private String description;

    @Persistent
    private String tip;

    @Persistent
    private String words;

    @Persistent
    private String mp3Url;

    @Persistent
    private int indexingType;

    @Persistent
    private boolean isDeleted;

    @Persistent
    private Date dateCreated;

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getArpabet() {
        return arpabet;
    }

    public void setArpabet(String arpabet) {
        this.arpabet = arpabet;
    }

    public String getIpa() {
        return ipa;
    }

    public void setIpa(String ipa) {
        this.ipa = ipa;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getMp3Url() {
        return mp3Url;
    }

    public void setMp3Url(String mp3Url) {
        this.mp3Url = mp3Url;
    }

    public int getIndexingType() {
        return indexingType;
    }

    public void setIndexingType(int indexingType) {
        this.indexingType = indexingType;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}

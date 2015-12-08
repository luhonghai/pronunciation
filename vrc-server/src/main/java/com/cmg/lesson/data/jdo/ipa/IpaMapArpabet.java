package com.cmg.lesson.data.jdo.ipa;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Date;

/**
 * Created by lantb on 2015-10-26.
 */
@PersistenceCapable(table = "IPAMAPARPABET", detachable = "true")
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
    @Column(jdbcType="VARCHAR", length=10000)
    private String tip;

    @Persistent
    private String words;

    @Persistent
    private String mp3Url;

    //column mp3UrlShort add by nam.bui
    @Persistent
    private String mp3UrlShort;

    @Persistent
    private int indexingType;

    @Persistent
    private boolean isDeleted;

    @Persistent
    private Date dateCreated;

    @Persistent
    @Column(jdbcType="VARCHAR", length=10000)
    private String imgTongue;

    @Persistent
    private String textTongue;

    @Persistent
    @Column(jdbcType="VARCHAR", length=10000)
    private String imgLip;

    @Persistent
    private String textLip;

    @Persistent
    @Column(jdbcType="VARCHAR", length=10000)
    private String imgJaw;

    @Persistent
    private String textJaw;


    public String getImgTongue() {
        return imgTongue;
    }

    public void setImgTongue(String imgTongue) {
        this.imgTongue = imgTongue;
    }

    public String getTextTongue() {
        return textTongue;
    }

    public void setTextTongue(String textTongue) {
        this.textTongue = textTongue;
    }

    public String getImgLip() {
        return imgLip;
    }

    public void setImgLip(String imgLip) {
        this.imgLip = imgLip;
    }

    public String getTextLip() {
        return textLip;
    }

    public void setTextLip(String textLip) {
        this.textLip = textLip;
    }

    public String getImgJaw() {
        return imgJaw;
    }

    public void setImgJaw(String imgJaw) {
        this.imgJaw = imgJaw;
    }

    public String getTextJaw() {
        return textJaw;
    }

    public void setTextJaw(String textJaw) {
        this.textJaw = textJaw;
    }

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
    public String getMp3UrlShort() {
        return mp3UrlShort;
    }

    public void setMp3UrlShort(String mp3UrlShort) {
        this.mp3UrlShort = mp3UrlShort;
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

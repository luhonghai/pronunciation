package com.cmg.vrc.data.jdo.amt;

import com.cmg.vrc.data.Mirrorable;

import java.util.Date;

/**
 * Created by cmg on 03/07/15.
 */
public class Transcription implements Mirrorable {

    private String id;

    private String sentence;

    private String author;

    private Date createdDate;

    private Date modifiedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}

package com.cmg.vrc.data.jdo.amt;

import com.cmg.vrc.data.Mirrorable;

import java.util.Date;

/**
 * Created by cmg on 03/07/15.
 */
public class RecordedSentence implements Mirrorable {

    public static final int AWAITING = 0;

    public static final int PENDING = 1;

    public static final int REJECT = 2;

    public static final int APPROVED = 3;

    public static final int LOCKED = 4;

    public static final int TESTING = 5;

    private String id;

    private String account;

    private String admin;

    private int status;

    private String sentenceId;
    
    private String fileName;

    private Date createdDate;

    private Date modifiedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSentenceId() {
        return sentenceId;
    }

    public void setSentenceId(String sentenceId) {
        this.sentenceId = sentenceId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

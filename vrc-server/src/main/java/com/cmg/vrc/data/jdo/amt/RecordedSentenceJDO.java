package com.cmg.vrc.data.jdo.amt;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Date;

/**
 * Created by cmg on 03/07/15.
 */
@PersistenceCapable
public class RecordedSentenceJDO implements Mirrorable {

    public static final int DELETED = -1;

    public static final int AWAITING = 0;

    public static final int PENDING = 1;

    public static final int REJECT = 2;

    public static final int APPROVED = 3;

    public static final int LOCKED = 4;

    @PrimaryKey
    private String id;

    @Persistent
    private String account;

    @Persistent
    private String admin;

    @Persistent
    private int status;

    @Persistent
    private String sentenceId;

    @Persistent
    private String fileName;

    @Persistent
    private Date createdDate;

    @Persistent
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

package com.cmg.vrc.data.jdo.amt;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Date;

/**
 * Created by cmg on 06/07/15.
 */
@PersistenceCapable
public class RecordedSentenceHistoryJDO implements Mirrorable {

    @PrimaryKey
    private String id;

    @Persistent
    private String recordedSentenceId;

    @Persistent
    private int previousStatus;

    @Persistent
    private int newStatus;

    @Persistent
    private String actor;

    @Persistent
    private int actorType;

    @Persistent
    private String message;

    @Persistent
    private Date timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRecordedSentenceId() {
        return recordedSentenceId;
    }

    public void setRecordedSentenceId(String recordedSentenceId) {
        this.recordedSentenceId = recordedSentenceId;
    }

    public int getPreviousStatus() {
        return previousStatus;
    }

    public void setPreviousStatus(int previousStatus) {
        this.previousStatus = previousStatus;
    }

    public int getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(int newStatus) {
        this.newStatus = newStatus;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public int getActorType() {
        return actorType;
    }

    public void setActorType(int actorType) {
        this.actorType = actorType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

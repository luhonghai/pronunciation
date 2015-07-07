package com.cmg.vrc.data.jdo.amt;

import com.cmg.vrc.data.Mirrorable;

import java.util.Date;

/**
 * Created by cmg on 06/07/15.
 */
public class RecordedSentenceHistory implements Mirrorable {

    public static final int ACTOR_TYPE_ADMIN = 0;

    public static final int ACTOR_TYPE_USER = 1;

    private String id;

    private String recordedSentenceId;

    private int previousStatus;

    private int newStatus;

    private String actor;

    private int actorType;

    private String message;

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

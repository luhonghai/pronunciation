package com.cmg.merchant.data.jdo;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Date;

/**
 * Created by lantb on 2016-01-26.
 */
@PersistenceCapable(table = "COURSEMAPPINGTEACHER", detachable = "true")
public class CourseMappingTeacher implements Mirrorable{
    @PrimaryKey
    private String id;

    @Persistent
    private String cpID;//company id

    @Persistent
    private String tID;//teacher id

    @Persistent
    private String cID;//course id

    @Persistent
    private String sr;//sharing rule

    @Persistent
    private String status;//status is publish or not

    @Persistent
    private String state;//created, edited, duplicated

    @Persistent
    private Date dateCreated;//date time created,edited,duplicated

    @Persistent
    private String cpIdClone;//use for store company id in case merchant copy the course from this company

    @Persistent
    private String cIdClone;//use for store course id in case merchant copy the course.

    @Persistent
    private boolean isDeleted;

    public CourseMappingTeacher(){}

    public CourseMappingTeacher(String tID, String cpID, String cID,
                                String sr, String status, String state, Date dateCreated,
                                String cpIdClone, String cIdClone, boolean isDeleted){
        this.tID = tID;
        this.cpID = cpID;
        this.cID = cID;
        this.sr = sr;
        this.status = status;
        this.state = state;
        this.dateCreated = dateCreated;
        this.cpIdClone = cpIdClone;
        this.cIdClone = cIdClone;
        this.isDeleted = isDeleted;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getCpID() {
        return cpID;
    }

    public void setCpID(String cpID) {
        this.cpID = cpID;
    }

    public String gettID() {
        return tID;
    }

    public void settID(String tID) {
        this.tID = tID;
    }

    public String getcID() {
        return cID;
    }

    public void setcID(String cID) {
        this.cID = cID;
    }

    public String getSr() {
        return sr;
    }

    public void setSr(String sr) {
        this.sr = sr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCpIdClone() {
        return cpIdClone;
    }

    public void setCpIdClone(String cpIdClone) {
        this.cpIdClone = cpIdClone;
    }

    public String getcIdClone() {
        return cIdClone;
    }

    public void setcIdClone(String cIdClone) {
        this.cIdClone = cIdClone;
    }
}

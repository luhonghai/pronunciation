package com.cmg.vrc.data.jdo;

import java.util.Date;

/**
 * Created by CMGT400 on 11/13/2015.
 */
public class Setting {


    private String idAppDetail;

    private String noAccessMessage;

    private boolean registration = false;

    private String idNumberDate;

    private String userName;

    private Date createdDate;

    private int  numberDate;

    private String subject;

    private String message;


    public int getNumberDate() {
        return numberDate;
    }

    public void setNumberDate(int numberDate) {
        this.numberDate = numberDate;
    }

    public String getIdAppDetail() {
        return idAppDetail;
    }

    public void setIdAppDetail(String idAppDetail) {
        this.idAppDetail = idAppDetail;
    }

    public String getIdNumberDate() {
        return idNumberDate;
    }

    public void setIdNumberDate(String idNumberDate) {
        this.idNumberDate = idNumberDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getNoAccessMessage() {
        return noAccessMessage;
    }

    public void setNoAccessMessage(String noAccessMessage) {
        this.noAccessMessage = noAccessMessage;
    }

    public boolean isRegistration() {
        return registration;
    }

    public void setRegistration(boolean registration) {
        this.registration = registration;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

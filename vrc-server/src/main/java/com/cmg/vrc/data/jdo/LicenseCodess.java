package com.cmg.vrc.data.jdo;

import java.util.Date;

/**
 * Created by CMGT400 on 9/29/2015.
 */
public class LicenseCodess {

    private String id;


    private String account;

    private String code;


    private Date activatedDate;


    private boolean isActivated;


    private String imei;


    private boolean isDeleted;


    private Date createdDate;

    private String company;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        if (account != null) account = account.toLowerCase();
        return account;
    }

    public void setAccount(String account) {
        if (account != null) account = account.toLowerCase();
        this.account = account;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getActivatedDate() {
        return activatedDate;
    }

    public void setActivatedDate(Date activatedDate) {
        this.activatedDate = activatedDate;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean isActivated) {
        this.isActivated = isActivated;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
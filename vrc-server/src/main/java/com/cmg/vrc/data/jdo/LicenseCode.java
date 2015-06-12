package com.cmg.vrc.data.jdo;

import com.cmg.vrc.data.Mirrorable;

import java.util.Date;

/**
 * Created by luhonghai on 5/18/15.
 */
public class LicenseCode implements Mirrorable {

    private String id;

    private String account;

    private String code;

    private Date activatedDate;

    private boolean isActivated;

    private String imei;

    @Override
    public String getId() {
        return id;
    }

    @Override
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
}
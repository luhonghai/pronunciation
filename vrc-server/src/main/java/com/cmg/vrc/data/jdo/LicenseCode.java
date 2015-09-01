package com.cmg.vrc.data.jdo;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Date;

/**
 * Created by luhonghai on 5/18/15.
 */
@PersistenceCapable(table = "LICENSE_CODE", detachable = "true")
public class LicenseCode implements Mirrorable {

    @PrimaryKey
    private String id;

    @Persistent
    private String account;

    @Persistent
    private String code;

    @Persistent
    private Date activatedDate;

    @Persistent
    private boolean isActivated;

    @Persistent
    private String imei;

    @Persistent
    private boolean isDeleted;

    @Persistent
    private Date createdDate;

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
}

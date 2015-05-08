package com.cmg.vrc.data.jdo;

import com.cmg.vrc.data.Mirrorable;

import java.util.Date;

/**
 * Created by luhonghai on 5/8/15.
 */
public class Feedback implements Mirrorable {

    private String id;

    private String account;

    private String screenshoot;

    private String description;

    private String imei;

    private String appVersion;

    private String osVersion;

    private String osApiLevel;

    private String deviceName;

    private String stackTrace;

    private String rawUserProfile;

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
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getScreenshoot() {
        return screenshoot;
    }

    public void setScreenshoot(String screenshoot) {
        this.screenshoot = screenshoot;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getOsApiLevel() {
        return osApiLevel;
    }

    public void setOsApiLevel(String osApiLevel) {
        this.osApiLevel = osApiLevel;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getRawUserProfile() {
        return rawUserProfile;
    }

    public void setRawUserProfile(String rawUserProfile) {
        this.rawUserProfile = rawUserProfile;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}

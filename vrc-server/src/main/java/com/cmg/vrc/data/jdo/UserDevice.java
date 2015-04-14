package com.cmg.vrc.data.jdo;

import com.cmg.vrc.data.Mirrorable;

import java.util.Date;

/**
 * Created by luhonghai on 4/13/15.
 */
public class UserDevice implements Mirrorable {

    private String id;

    private String model;
    private String osVersion;
    private String osApiLevel;
    private String deviceName;
    private String emei;
    private Date attachedDate;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
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

    public String getEmei() {
        return emei;
    }

    public void setEmei(String emei) {
        this.emei = emei;
    }

    public Date getAttachedDate() {
        return attachedDate;
    }

    public void setAttachedDate(Date attachedDate) {
        this.attachedDate = attachedDate;
    }
}

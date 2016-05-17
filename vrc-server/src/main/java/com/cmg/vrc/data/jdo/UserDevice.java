package com.cmg.vrc.data.jdo;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Date;

/**
 * Created by luhonghai on 4/13/15.
 */
@PersistenceCapable(table = "USER_DEVICE", detachable = "true")
public class UserDevice implements Mirrorable {

    @PrimaryKey
    private String id;

    @Persistent
    private String model;

    @Persistent
    private String osVersion;

    @Persistent
    private String osApiLevel;

    @Persistent
    private String deviceName;

    @Persistent
    @Column(name = "emei")
    private String imei;

    @Persistent
    private String gcmId;

    @Persistent
    private Date attachedDate;

    @Persistent
    private String userLastLogin;

    public String getUserLastLogin() {
        return userLastLogin;
    }

    public void setUserLastLogin(String userLastLogin) {
        this.userLastLogin = userLastLogin;
    }

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

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Date getAttachedDate() {
        return attachedDate;
    }

    public void setAttachedDate(Date attachedDate) {
        this.attachedDate = attachedDate;
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }
}

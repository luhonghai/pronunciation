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
@PersistenceCapable(table = "`usage`")
public class UsageJDO implements Mirrorable {

    @PrimaryKey
    private String id;

    @Persistent
    private String username;

    @Persistent
    @Column(name = "emei")
    private String imei;

    @Persistent
    private double latitude;

    @Persistent
    private double longitude;

    @Persistent
    private Date time;

    @Persistent
    private String appVersion;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getUsername() {
        if (username != null) username = username.toLowerCase();
        return username;
    }

    public void setUsername(String username) {
        if (username != null) username = username.toLowerCase();
        this.username = username;
    }
}

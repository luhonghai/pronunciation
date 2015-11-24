package com.cmg.vrc.data.jdo;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.util.Date;

/**
 * Created by CMGT400 on 6/8/2015.
 */
@PersistenceCapable(table = "LOGINTOKEN", detachable = "true")
public class LoginToken implements Mirrorable {
    @PrimaryKey
    private String id;

    @Persistent
    private String userName;

    @Persistent
    private String deviceName;

    @Persistent
    private String token;

    @Persistent
    private int appVersion;

    @Persistent
    private String appName;

    @Persistent
    private Date createdDate;

    @Persistent
    private Date accessDate;



    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id=id;
    }
    public String getUserName(){
        if (userName != null) userName = userName.toLowerCase();
        return userName;
    }
    public  void setUserName(String userName){
        if (userName != null) userName = userName.toLowerCase();
        this.userName=userName;
    }
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getToken() {
        if (token == null) return "";
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(int appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }


    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getAccessDate() {
        return accessDate;
    }

    public void setAccessDate(Date accessDate) {
        this.accessDate = accessDate;
    }
}

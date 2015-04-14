package com.cmg.vrc.data.jdo;

import com.cmg.vrc.data.Mirrorable;

import java.util.Date;

/**
 * Created by luhonghai on 4/13/15.
 */
public class Security implements Mirrorable {

    private String id;

    private String username;

    private String password;

    private String loginType;

    private Date firstAccess;

    private String appVersion;

    private String noAccessMessage;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public Date getFirstAccess() {
        return firstAccess;
    }

    public void setFirstAccess(Date firstAccess) {
        this.firstAccess = firstAccess;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getNoAccessMessage() {
        return noAccessMessage;
    }

    public void setNoAccessMessage(String noAccessMessage) {
        this.noAccessMessage = noAccessMessage;
    }
}

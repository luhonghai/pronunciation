package com.cmg.vrc.data.jdo;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Created by luhonghai on 4/13/15.
 */
@PersistenceCapable(table = "user")
public class UserJDO implements Mirrorable {

    @PrimaryKey
    private String id;

    @Persistent
    private String username;

    @Persistent
    private String name;

    @Persistent
    private String loginType;

    @Persistent
    private String profileImage;

    @Persistent
    private String password;

    @Persistent
    private boolean nativeEnglish = true;

    @Persistent
    private boolean gender = true;

    @Persistent
    private String dob = "01/01/1900";

    @Persistent
    private String country = "GB";

    @Persistent
    private int englishProficiency = 5;

    @Persistent
    private String rawJsonData;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isNativeEnglish() {
        return nativeEnglish;
    }

    public void setNativeEnglish(boolean nativeEnglish) {
        this.nativeEnglish = nativeEnglish;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getEnglishProficiency() {
        return englishProficiency;
    }

    public void setEnglishProficiency(int englishProficiency) {
        this.englishProficiency = englishProficiency;
    }

    public String getRawJsonData() {
        return rawJsonData;
    }

    public void setRawJsonData(String rawJsonData) {
        this.rawJsonData = rawJsonData;
    }
}
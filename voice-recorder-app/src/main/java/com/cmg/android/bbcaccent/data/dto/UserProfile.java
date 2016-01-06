package com.cmg.android.bbcaccent.data.dto;

import com.cmg.android.bbcaccent.data.dto.lesson.country.Country;

import java.util.List;

/**
 * Created by luhonghai on 9/29/14.
 */
public class UserProfile {

    public static final String TYPE_EASYACCENT = "easyaccent";
    public static final String TYPE_FACEBOOK = "facebook";
    public static final String TYPE_GOOGLE_PLUS = "googleplus";

    public static final int HELP_INIT = 0;
    public static final int HELP_SKIP = 1;
    public static final int HELP_NEVER = 2;

    public static UserProfile getAnonymouse() {
        UserProfile anonymous = new UserProfile();
        anonymous.username = "anonymous";
        return anonymous;
    }

    public List<LicenseData> getLicenseData() {
        return licenseData;
    }

    public void setLicenseData(List<LicenseData> licenseData) {
        this.licenseData = licenseData;
    }

    public String getLastSelectedMenuItem() {
        return lastSelectedMenuItem;
    }

    public void setLastSelectedMenuItem(String lastSelectedMenuItem) {
        this.lastSelectedMenuItem = lastSelectedMenuItem;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public class LicenseData {
        private String code;
        private UserProfile.DeviceInfo deviceInfo;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public DeviceInfo getDeviceInfo() {
            return deviceInfo;
        }

        public void setDeviceInfo(DeviceInfo deviceInfo) {
            this.deviceInfo = deviceInfo;
        }

        @Override
        public String toString() {
            return code
                    + (deviceInfo != null ? (" (" + deviceInfo.getModel() + " " + deviceInfo.getDeviceName() + ")") : "");
        }
    }

    public boolean isSetup() {
        return isSetup;
    }

    public void setIsSetup(boolean isSetup) {
        this.isSetup = isSetup;
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
        if (profileImage == null) return "";
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

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public int getHelpStatus() {
        return helpStatus;
    }

    public void setHelpStatus(int helpStatus) {
        this.helpStatus = helpStatus;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public String getLicenseCode() {
        if (licenseCode == null) return "";
        return licenseCode;
    }

    public void setLicenseCode(String licenseCode) {
        this.licenseCode = licenseCode;
    }

    public Country getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(Country selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isActivatedLicence() {
        return isActivatedLicence;
    }

    public void setIsActivatedLicence(boolean isActivatedLicence) {
        this.isActivatedLicence = isActivatedLicence;
    }

    public boolean isSubscription() {
        return isSubscription;
    }

    public void setIsSubscription(boolean isSubscription) {
        this.isSubscription = isSubscription;
    }

    public String getAdditionalToken() {
        return additionalToken;
    }

    public void setAdditionalToken(String additionalToken) {
        this.additionalToken = additionalToken;
    }

    public static class UserLocation {
        private double latitude;
        private double longitude;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }

    public static class DeviceInfo {
        private String appVersion;
        private String appName;
        private String model;
        private String osVersion;
        private String osApiLevel;
        private String deviceName;
        private String emei;
        private String gcmId;

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
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

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getGcmId() {
            return gcmId;
        }

        public void setGcmId(String gcmId) {
            this.gcmId = gcmId;
        }
    }

    private String username;
    private String firstName;
    private String lastName;
    private String name;
    private String loginType;
    private String profileImage;
    private String password;
    private boolean isSetup = false;
    private boolean nativeEnglish = true;
    private boolean gender = true;
    private String dob = "01/01/1900";
    private String country = "GB";
    private int englishProficiency = 5;
    private long time;
    private long duration;
    private UserLocation location;
    private DeviceInfo deviceInfo;
    private String uuid;
    private int helpStatus = HELP_INIT;
    private boolean isLogin = false;
    private String lastSelectedMenuItem;

    private String licenseCode;

    private Country selectedCountry;

    private String token;

    private String additionalToken;

    private boolean isActivatedLicence;

    private boolean isSubscription;

    private boolean isExpired;

    private List<LicenseData> licenseData;

    public boolean isPro() {
        return isSubscription || isActivatedLicence;
    }

    public int getEnglishProficiency() {
        return englishProficiency;
    }

    public void setEnglishProficiency(int englishProficiency) {
        this.englishProficiency = englishProficiency;
    }


    public String getUsername() {
        return (username ==null ?  "" : username.toLowerCase());
    }

    public void setUsername(String username) {
        if (username != null) {
            this.username = username.toLowerCase();
        }
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public UserLocation getLocation() {
        return location;
    }

    public void setLocation(UserLocation location) {
        this.location = location;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

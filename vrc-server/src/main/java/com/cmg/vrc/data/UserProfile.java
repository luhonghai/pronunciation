package com.cmg.vrc.data;

/**
 * Created by luhonghai on 9/29/14.
 */
public class UserProfile {

    public static final String TYPE_EASYACCENT = "easyaccent";
    public static final String TYPE_FACEBOOK = "facebook";
    public static final String TYPE_GOOGLE_PLUS = "googleplus";

    public static UserProfile getAnonymouse() {
        UserProfile anonymous = new UserProfile();
        anonymous.username = "anonymous";
        return anonymous;
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
        private String model;
        private String osVersion;
        private String osApiLevel;
        private String deviceName;
        private String emei;

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
    }

    private String username;
    private String name;
    private String firstName;
    private String lastName;

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

    public int getEnglishProficiency() {
        return englishProficiency;
    }

    public void setEnglishProficiency(int englishProficiency) {
        this.englishProficiency = englishProficiency;
    }


    public String getUsername() {
        if (username != null) return username.toLowerCase();
        return username;
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

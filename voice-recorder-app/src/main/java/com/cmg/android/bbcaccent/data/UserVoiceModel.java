package com.cmg.android.bbcaccent.data;

/**
 * Created by luhonghai on 9/30/14.
 */
public class UserVoiceModel {

    private String id;
    private String username;
    private boolean nativeEnglish = true;
    private boolean gender = true;
    private String dob;
    private String country;
    private int englishProficiency = 5;
    private long time;
    private long serverTime;
    private long duration;
    private float score;
    private double latitude;
    private double longitude;
    private String uuid;
    private String word;
    private String recordFile;
    private String cleanRecordFile;
    private String phonemes;
    private String hypothesis;

    private int version;



    private int versionPhoneme;

    private String audioFile;

    private SphinxResult result;


    public int getVersionPhoneme() {
        return versionPhoneme;
    }

    public void setVersionPhoneme(int versionPhoneme) {
        this.versionPhoneme = versionPhoneme;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getRecordFile() {
        return recordFile;
    }

    public void setRecordFile(String recordFile) {
        this.recordFile = recordFile;
    }

    public String getCleanRecordFile() {
        return cleanRecordFile;
    }

    public void setCleanRecordFile(String cleanRecordFile) {
        this.cleanRecordFile = cleanRecordFile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhonemes() {
        return phonemes;
    }

    public void setPhonemes(String phonemes) {
        this.phonemes = phonemes;
    }

    public String getHypothesis() {
        return hypothesis;
    }

    public void setHypothesis(String hypothesis) {
        this.hypothesis = hypothesis;
    }


    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public SphinxResult getResult() {
        return result;
    }

    public void setResult(SphinxResult result) {
        this.result = result;
    }

    public String getAudioFile() {
        if (audioFile == null) return "";
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public int getVersion() {return version;}
    public void setVersion(int version) {this.version = version;}
}

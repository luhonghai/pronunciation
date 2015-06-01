package com.cmg.vrc.data.jdo;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Created by luhonghai on 9/30/14.
 */
@PersistenceCapable
public class UserVoiceModelJDO implements Mirrorable {
    @PrimaryKey
    private String id;

    @Persistent
    private String username;
    @Persistent
    private boolean nativeEnglish;
    @Persistent
    private boolean gender;
    @Persistent
    private String dob;
    @Persistent
    private String country;
    @Persistent
    private int englishProficiency;
    @Persistent
    private long time;
    @Persistent
    private long serverTime;
    @Persistent
    private long duration;
    @Persistent
    private float score;
    @Persistent
    private double latitude;
    @Persistent
    private double longitude;
    @Persistent
    private String uuid;
    @Persistent
    private String word;
    @Persistent
    private String recordFile;
    @Persistent
    private String cleanRecordFile;
    @Persistent
    private String phonemes;
    @Persistent
    private String hypothesis;

    @Persistent
    private String rawSphinxResult;

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

    @Override
    public String getId() {
        return id;
    }

    @Override
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

    public String getRawSphinxResult() {
        return rawSphinxResult;
    }

    public void setRawSphinxResult(String rawSphinxResult) {
        this.rawSphinxResult = rawSphinxResult;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}

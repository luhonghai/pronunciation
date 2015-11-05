package com.cmg.vrc.data.jdo;

import com.cmg.vrc.sphinx.SphinxResult;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.Persistent;
import java.util.Date;

/**
 * Created by CMGT400 on 9/29/2015.
 */
public class Score {

    private String id;

    private String username;

    private long serverTime;

    private double score;

    private double latitude;

    private double longitude;

    private String word;

    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
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

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

}

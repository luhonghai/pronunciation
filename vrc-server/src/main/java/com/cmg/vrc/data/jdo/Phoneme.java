package com.cmg.vrc.data.jdo;

/**
 * Created by CMGT400 on 9/29/2015.
 */
public class Phoneme {

    private String id;

    private String username;

    private long serverTime;

    private double score;

    private String phoneme;

    private String country;

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

    public String getPhoneme() {
        return phoneme;
    }

    public void setPhoneme(String phoneme) {
        this.phoneme = phoneme;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

}

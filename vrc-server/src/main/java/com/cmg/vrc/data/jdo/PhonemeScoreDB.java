package com.cmg.vrc.data.jdo;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Created by lantb on 2015-09-01.
 */
@PersistenceCapable(table = "PHONEMESCORE", detachable = "true")
public class PhonemeScoreDB implements Mirrorable{
    @PrimaryKey
    private String id;

    @Persistent
    private String username;

    @Persistent
    private String phonemeWord;

    @Persistent
    private float totalScore;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Persistent
    private long time;

    @Persistent
    @Column(defaultValue="1")
    private int version;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public float getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(float totalScore) {
        this.totalScore = totalScore;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhonemeWord() {
        return phonemeWord;
    }

    public void setPhonemeWord(String phonemeWord) {
        this.phonemeWord = phonemeWord;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

}





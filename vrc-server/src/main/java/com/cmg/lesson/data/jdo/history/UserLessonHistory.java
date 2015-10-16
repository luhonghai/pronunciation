package com.cmg.lesson.data.jdo.history;

import com.cmg.vrc.data.Mirrorable;
import com.cmg.vrc.sphinx.SphinxResult;

import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Created by lantb on 2015-10-16.
 */
@PersistenceCapable(table = "UserLessonHistory", detachable = "true")
public class UserLessonHistory implements Mirrorable{

    @PrimaryKey
    private String id;

    @NotPersistent
    private String idCountry;

    @Persistent
    private String username;

    @Persistent
    private String word;

    @Persistent
    private double score;

    @Persistent
    private String recordedFile;

    @Persistent
    private String type;

    @Persistent
    private boolean isDeleted;

    @Persistent
    private int version;

    @Persistent
    private long serverTime;

    @NotPersistent
    private SphinxResult result;

    @NotPersistent
    private String idQuestion;

    @NotPersistent
    private String idWord;



    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getIdCountry() {
        return idCountry;
    }

    public void setIdCountry(String idCountry) {
        this.idCountry = idCountry;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getRecordedFile() {
        return recordedFile;
    }

    public void setRecordedFile(String recordedFile) {
        this.recordedFile = recordedFile;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getServerTime() {
        return serverTime;
    }

    public void setServerTime(long serverTime) {
        this.serverTime = serverTime;
    }

    public SphinxResult getResult() {
        return result;
    }

    public void setResult(SphinxResult result) {
        this.result = result;
    }

    public String getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(String idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getIdWord() {
        return idWord;
    }

    public void setIdWord(String idWord) {
        this.idWord = idWord;
    }
}

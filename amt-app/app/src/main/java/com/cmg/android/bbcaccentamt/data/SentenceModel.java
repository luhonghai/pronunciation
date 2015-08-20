package com.cmg.android.bbcaccentamt.data;

/**
 * Created by CMGT400 on 7/10/2015.
 */
public class SentenceModel {
    String _id;
    String sentence;
    int version;
    int status;
    int isDeleted;

    public SentenceModel(){
    }

    public SentenceModel(String id, String sentence, int status, int version, int isDeleted){
        this._id = id;
        this.sentence = sentence;
        this.version=version;
        this.status=status;
        this.isDeleted=isDeleted;
    }
    public String getID(){
        return this._id;
    }
    public void setID(String id){
        this._id = id;
    }
    public String getSentence(){
        return this.sentence;
    }
    public void setSentence(String sentence){
        this.sentence = sentence;
    }
    public int getStatus(){
        return this.status;
    }
    public void setStatus(int status){
        this.status =status;
    }
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
    public int isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }




}

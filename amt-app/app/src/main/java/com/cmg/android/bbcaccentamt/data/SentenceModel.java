package com.cmg.android.bbcaccentamt.data;

/**
 * Created by CMGT400 on 7/10/2015.
 */
public class SentenceModel {
    int _id;
    String sentence;
    int status;

    public SentenceModel(){
    }
    public SentenceModel(int id, String sentence, int status){
        this._id = id;
        this.sentence = sentence;
        this.status=status;
    }
    public int getID(){
        return this._id;
    }
    public void setID(int id){
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
}

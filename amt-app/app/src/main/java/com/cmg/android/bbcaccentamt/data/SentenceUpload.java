package com.cmg.android.bbcaccentamt.data;

/**
 * Created by CMGT400 on 8/12/2015.
 */
public class SentenceUpload {
    int _id;
    String sentence;

    public SentenceUpload(){
    }
    public SentenceUpload(int id, String sentence){
        this._id = id;
        this.sentence = sentence;
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

}

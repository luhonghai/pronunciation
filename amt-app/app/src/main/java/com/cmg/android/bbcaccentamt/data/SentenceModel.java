package com.cmg.android.bbcaccentamt.data;

/**
 * Created by CMGT400 on 7/10/2015.
 */
public class SentenceModel {
    String _id;
    String sentence;
    int status;
    int index;

    public SentenceModel(){
    }
    public SentenceModel(String id, String sentence, int status, int index){
        this._id = id;
        this.sentence = sentence;
        this.status=status;
        this.index=index;

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
    public int getIndex(){
        return this.index;
    }
    public void setIndex(int index){
        this.index =index;
    }

}

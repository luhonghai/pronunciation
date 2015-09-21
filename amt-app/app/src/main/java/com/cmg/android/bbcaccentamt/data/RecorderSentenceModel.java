package com.cmg.android.bbcaccentamt.data;

/**
 * Created by CMGT400 on 8/12/2015.
 */
public class RecorderSentenceModel {
    String _id;
    String idSentence;
    String account;
    String fileName;
    int status;
    int version;
    int isDelete;

    public RecorderSentenceModel(String _id, String idSentence, String account,String fileName,int status,int version,int isDeleted ){
        this._id=_id;
        this.idSentence = idSentence;
        this.account=account;
        this.fileName=fileName;
        this.status=status;
        this.version=version;
        this.isDelete=isDeleted;
    }
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getVersion() {
        return version;
    }

    public String getIdSentence() {
        return idSentence;
    }

    public void setIdSentence(String idSentence) {
        this.idSentence = idSentence;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int isDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }
    public RecorderSentenceModel(){
    }
    public String getID(){
        return this._id;
    }
    public void setID(String id){
        this._id = id;
    }


}

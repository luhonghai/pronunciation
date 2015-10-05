package com.cmg.vrc.servlet;

import com.cmg.vrc.data.Mirrorable;
import com.cmg.vrc.data.dao.impl.TranscriptionDAO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by CMGT400 on 10/1/2015.
 */
public class Test1 {

    public class Transcription implements Mirrorable {

        private String id;

        private String sentence;

        private int status;

        private String author;

        private Date createdDate;

        private Date modifiedDate;

        private int version;
        private int isDeleted;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSentence() {
            return sentence;
        }

        public void setSentence(String sentence) {
            this.sentence = sentence;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public Date getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(Date createdDate) {
            this.createdDate = createdDate;
        }

        public Date getModifiedDate() {
            return modifiedDate;
        }

        public void setModifiedDate(Date modifiedDate) {
            this.modifiedDate = modifiedDate;
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


    public static void main(String []args){
        TranscriptionDAO transcriptionDAO=new TranscriptionDAO();
        Gson gson = new Gson();
        File target = new File("D:\\Sentence\\sentence.json");
        try {
        List<Transcription> transcriptionList = gson.fromJson(
                FileUtils.readFileToString(target, "UTF-8"),
                new TypeToken<List<Transcription>>(){}.getType());
            transcriptionDAO.deleteAll();
            List<com.cmg.vrc.data.jdo.Transcription> transcriptions = new ArrayList<com.cmg.vrc.data.jdo.Transcription>();
            for (Transcription transcription : transcriptionList) {
                com.cmg.vrc.data.jdo.Transcription t = new com.cmg.vrc.data.jdo.Transcription();
                t.setAuthor(transcription.getAuthor());
                t.setStatus(transcription.getStatus());
                t.setCreatedDate(transcription.getCreatedDate());
                t.setIsDeleted(transcription.isDeleted());
                t.setVersion(transcription.getVersion());
                t.setId(transcription.getId());
                t.setModifiedDate(transcription.getModifiedDate());
                t.setSentence(transcription.getSentence());
                transcriptions.add(t);
            }
            transcriptionDAO.create(transcriptions);
           // FileUtils.write(new File("D:\\Sentence\\sentence.json"), gson.toJson(transcriptionList), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

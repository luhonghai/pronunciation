package com.cmg.vrc.servlet;

import com.cmg.vrc.data.Mirrorable;
import com.cmg.vrc.data.dao.impl.TranscriptionDAO;
import com.cmg.vrc.sphinx.DictionaryHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import java.io.File;
import java.io.IOException;
import java.util.*;

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

    private static final int EXPECTED_SENTENCES_LENGTH = 700;

    private static final int ACCEPTED_RANGE = 100;

    public static void main(String []args){
        TranscriptionDAO transcriptionDAO=new TranscriptionDAO();
        Gson gson = new Gson();
        File target = new File("D:\\Sentence\\sentence.json");
        DictionaryHelper dictionaryHelper = new DictionaryHelper(DictionaryHelper.Type.BEEP);
        try {
            List<Transcription> transcriptionList = gson.fromJson(
                FileUtils.readFileToString(target, "UTF-8"),
                new TypeToken<List<Transcription>>(){}.getType());
                while(!pickTranscription(transcriptionList, dictionaryHelper));
            //transcriptionDAO.create(transcriptions);
           // FileUtils.write(new File("D:\\Sentence\\sentence.json"), gson.toJson(transcriptionList), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean pickTranscription(List<Transcription> transcriptionList, DictionaryHelper dictionaryHelper) throws Exception {
        System.out.println("===========================================");
        System.out.println("Try to pick " + EXPECTED_SENTENCES_LENGTH + " sentences from transcriptions list size " + transcriptionList.size());
        int size = transcriptionList.size();
        Map<String, Transcription> data = new HashMap<String, Transcription>();
        while (data.size() < EXPECTED_SENTENCES_LENGTH) {
            Random r = new Random();
            int index = -1;
            do {
                index = r.nextInt(size + 1);
                if (index < 0) index = 0;
                if (index > size - 1) index = size - 1;
                System.out.println("Random index: " + index);
            } while (data.containsKey(Integer.toString(index)));
            Transcription transcription = transcriptionList.get(index);
            String sentence = transcription.getSentence();
            while (sentence.contains("  ")) sentence = sentence.replace("  ", " ");
            sentence = sentence.trim().toUpperCase();
            String[] words = transcription.getSentence().split(" ");
            boolean isValid = true;
            for (String word : words) {
                List<String> mPhonemes = dictionaryHelper.getCorrectPhonemes(word);
                if (mPhonemes == null) {
                    System.out.println("Detect invalid word " + word + ". Skip by default");
                    isValid = false;
                    break;
                }
            }
            if (isValid) {
                System.out.println("Pick sentence index " + index + ":" + sentence);
                transcription.setSentence(sentence);
                data.put(Integer.toString(index), transcription);
            }
        }
        Map<String, Integer> phonemes = new HashMap<String, Integer>();
        List<String> phones = IOUtils.readLines(Test1.class.getClassLoader().getResourceAsStream("amt/phones"));
        for (String phone : phones) {
            if (!phone.equalsIgnoreCase("sil") && !phonemes.containsKey(phone.toLowerCase())) {
                phonemes.put(phone.toLowerCase(), 0);
            }
        }
        int total = 0;
        for (Transcription transcription : data.values()) {
            String[] words = transcription.getSentence().split(" ");
            for (String word : words) {
                List<String> mPhonemes = dictionaryHelper.getCorrectPhonemes(word);
                for (String mPhoneme : mPhonemes) {
                    int count = phonemes.get(mPhoneme.toLowerCase());
                    phonemes.put(mPhoneme.toLowerCase(), ++count);
                    total++;
                }
            }
        }
        int avg = total / phonemes.size();
        Iterator<String> keys = phonemes.keySet().iterator();
        boolean isValid = true;
        while (keys.hasNext()) {
            String phone = keys.next();
            int count = phonemes.get(phone);
            System.out.println("Phone: " + phone + ". Count: " + count + ". Avg: " + avg);
            if (avg - count > ACCEPTED_RANGE) {
                System.out.println("Out of accepted range " + (avg - count));
                isValid = false;
            }
        }
        if (isValid) {
            System.out.println("Found good list. Save to file");
            Gson gson = new Gson();
            FileUtils.write(new File("D:\\Sentence\\sentence-700.json"), gson.toJson(data.values()), "UTF-8");
        }
        return isValid;
    }

    private static void saveData() {
        Map<Integer, Transcription> data = new HashMap<Integer, Transcription>();
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

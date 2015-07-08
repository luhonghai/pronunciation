package com.cmg.vrc.service.amt;

import com.cmg.vrc.data.dao.impl.amt.TranscriptionDAO;
import com.cmg.vrc.data.jdo.amt.Transcription;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * Created by cmg on 08/07/15.
 */
public class TranscriptionService {
    private static final Logger logger = Logger.getLogger(TranscriptionService.class.getName());

    private TranscriptionDAO transcriptionDAO;

    private String author = "system";

    public TranscriptionService() {
        transcriptionDAO = new TranscriptionDAO();
    }

    public TranscriptionService(String author) {
        this();
        this.author = author;
    }

    public void loadTranscription(InputStream is) throws Exception {
        try {
            transcriptionDAO.deleteAll();
            List<String> transcriptions = IOUtils.readLines(is);
            if (transcriptions != null && transcriptions.size() > 0) {
                for (String t : transcriptions) {
                    t = t.trim();
                    if (t.length() > 0) {
                        String sentence = t.substring(0, t.lastIndexOf("(")).trim();
                        if (sentence.length() > 0) {
                            Transcription transcription = new Transcription();
                            transcription.setAuthor(author);
                            transcription.setCreatedDate(new Date(System.currentTimeMillis()));
                            transcription.setModifiedDate(new Date(System.currentTimeMillis()));
                            transcription.setSentence(sentence);
                            transcriptionDAO.put(transcription);
                        }
                    }
                }
            } else {
                logger.error("No default transcription found");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public void loadTranscription(File f) throws Exception {
        loadTranscription(new FileInputStream(f));
    }

    public void loadTranscription() throws Exception {
        loadTranscription(this.getClass().getClassLoader().getResourceAsStream("amt/default_transcription.txt"));
    }
}

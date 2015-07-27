package com.cmg.vrc.service.amt;

import com.cmg.vrc.data.UserProfile;
import com.cmg.vrc.data.dao.impl.amt.RecordedSentenceDAO;
import com.cmg.vrc.data.dao.impl.amt.RecordedSentenceHistoryDAO;
import com.cmg.vrc.data.dao.impl.amt.TranscriptionDAO;
import com.cmg.vrc.data.jdo.amt.RecordedSentence;
import com.cmg.vrc.data.jdo.amt.RecordedSentenceHistory;
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
    public static final String DEFAULT_TRANSCRIPTION_RESOURCE = "amt/default_transcription.txt";

    public static final String VOXFORGE_TRANSCRIPTION_RESOURCE = "amt/voxforge_en_sphinx.transcription";

    public static final String ADDITIONAL_TRANSCRIPTION_RESOURCE = "amt/additional.transcription";

    private static final Logger logger = Logger.getLogger(TranscriptionService.class.getName());

    private TranscriptionDAO transcriptionDAO;

    private RecordedSentenceDAO recordedSentenceDAO;

    private RecordedSentenceHistoryDAO recordedSentenceHistoryDAO;

    private String author = "system";

    public TranscriptionService() {
        transcriptionDAO = new TranscriptionDAO();
        recordedSentenceDAO = new RecordedSentenceDAO();
        recordedSentenceHistoryDAO = new RecordedSentenceHistoryDAO();
    }

    public TranscriptionService(String author) {
        this();
        this.author = author;
    }

    public List<Transcription> listTranscriptionByAccount(String account) throws Exception {
        List<Transcription> transcriptions = transcriptionDAO.listAll();
        if (transcriptions != null && transcriptions.size() > 0) {
            for (final Transcription transcription : transcriptions) {
                RecordedSentence recordedSentence = recordedSentenceDAO.getBySentenceIdAndAccount(transcription.getId(), account);
                if (recordedSentence != null) {
                    transcription.setStatus(recordedSentence.getStatus());
                }
            }
        }
        return transcriptions;
    }

    public RecordedSentenceHistory handleUploadedSentence(UserProfile user, String sentenceId, File recordedVoice) throws Exception {
        logger.info("Save recorded sentence. ID: " + sentenceId + ". Account: " + user.getUsername());
        Transcription transcription = transcriptionDAO.getById(sentenceId);
        if (transcription != null) {
            logger.info("Found sentence: " + transcription.getSentence() + ". ID: " + sentenceId);
            RecordedSentence recordedSentence = recordedSentenceDAO.getBySentenceIdAndAccount(sentenceId, user.getUsername());
            if (recordedSentence == null) {
                recordedSentence = new RecordedSentence();
            }
            Date now = new Date(System.currentTimeMillis());
            int lastStatus = recordedSentence.getStatus();
            recordedSentence.setStatus(RecordedSentence.PENDING);
            recordedSentence.setAccount(user.getUsername());
            recordedSentence.setAdmin("System");
            if (recordedSentence.getCreatedDate() == null)
                recordedSentence.setCreatedDate(now);
            recordedSentence.setModifiedDate(now);
            recordedSentence.setFileName(recordedVoice.getName());
            recordedSentence.setSentenceId(sentenceId);
            logger.info("Try to save recorded sentence info to database");
            if (recordedSentenceDAO.put(recordedSentence)) {
                logger.info("completed!");
                RecordedSentenceHistory recordedSentenceHistory = new RecordedSentenceHistory();
                recordedSentenceHistory.setActor(user.getUsername());
                recordedSentenceHistory.setActorType(RecordedSentenceHistory.ACTOR_TYPE_USER);
                recordedSentenceHistory.setPreviousStatus(lastStatus);
                recordedSentenceHistory.setNewStatus(recordedSentence.getStatus());
                recordedSentenceHistory.setMessage("Uploaded by user " + user.getUsername());
                recordedSentenceHistory.setTimestamp(now);
                recordedSentenceHistory.setRecordedSentenceId(recordedSentence.getId());
                if (recordedSentenceHistoryDAO.put(recordedSentenceHistory)) {
                    return recordedSentenceHistory;
                }
            }
        } else {
            logger.error("No sentence with ID " + sentenceId + " found.");
        }
        return null;
    }

    public void loadTranscription(InputStream is) throws Exception {
        try {
            transcriptionDAO.deleteAll();
            List<String> transcriptions = IOUtils.readLines(is);
            if (transcriptions != null && transcriptions.size() > 0) {
                int count = 0;
                for (String t : transcriptions) {
                    t = t.trim();
                    if (t.length() > 0) {
                        String sentence;
                        if (t.contains("(")) {
                            sentence = t.substring(0, t.lastIndexOf("(")).trim();
                        } else {
                            sentence = t.trim();
                        }
                        if (sentence.startsWith("<s>")) sentence = sentence.substring("<s>".length(), sentence.length());
                        if (sentence.endsWith("</s>")) sentence = sentence.substring(0, sentence.length() - "</s>".length());
                        sentence = sentence.trim();
                        if (sentence.length() > 0
                                && transcriptionDAO.getBySentence(sentence) == null
                                ) {
                            Transcription transcription = new Transcription();
                            transcription.setAuthor(author);
                            transcription.setCreatedDate(new Date(System.currentTimeMillis()));
                            transcription.setModifiedDate(new Date(System.currentTimeMillis()));
                            transcription.setSentence(sentence);
                            transcriptionDAO.put(transcription);
                            logger.info("Save transcription: " + sentence);
                            count++;
                        }
                    }
                }
                logger.info("Saved " + count + " transcriptions");
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

    public void loadTranscription(String resource) throws Exception {
        loadTranscription(this.getClass().getClassLoader().getResourceAsStream(resource));
    }

    public void loadTranscription() throws Exception {
        loadTranscription(VOXFORGE_TRANSCRIPTION_RESOURCE);
    }
}

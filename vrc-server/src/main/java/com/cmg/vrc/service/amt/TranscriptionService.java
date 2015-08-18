package com.cmg.vrc.service.amt;

import com.cmg.vrc.data.UserProfile;
import com.cmg.vrc.data.dao.impl.amt.RecordedSentenceDAO;
import com.cmg.vrc.data.dao.impl.amt.RecordedSentenceHistoryDAO;
import com.cmg.vrc.data.dao.impl.amt.TranscriptionDAO;
import com.cmg.vrc.data.jdo.RecordedSentence;
import com.cmg.vrc.data.jdo.amt.RecordedSentenceHistory;
import com.cmg.vrc.data.jdo.Transcription;
import com.cmg.vrc.sphinx.DictionaryHelper;
import com.cmg.vrc.util.StringUtil;
import com.cmg.vrc.util.UUIDGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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

    private boolean useJDO = true;

    private static final List<String> SENTENCES = new ArrayList<String>();

    private static Object lock = new Object();

    private static File resultHtmlFile;

    private DictionaryHelper dictionaryHelper;

    private Gson gson;

    public class LoadTranscriptionResult {
        int loadedCount = 0;
        int correctedSentenceCount = 0;
        Map<String, List<Integer>> replacedUKWord = new HashMap<String, List<Integer>>();
        Map<String, List<Integer>> notFoundInBeep = new HashMap<String, List<Integer>>();
        Map<String, List<Integer>> containSpecialChar = new HashMap<String, List<Integer>>();
        Map<String, Integer> phones = new HashMap<String, Integer>();
        int totalPhones = 0;
        List<String> words = new ArrayList<String>();
    }

    private LoadTranscriptionResult transcriptionResult;

    public TranscriptionService() {
        transcriptionDAO = new TranscriptionDAO();
        recordedSentenceDAO = new RecordedSentenceDAO();
        recordedSentenceHistoryDAO = new RecordedSentenceHistoryDAO();
        dictionaryHelper = new DictionaryHelper(DictionaryHelper.Type.BEEP);
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public TranscriptionService(String author) {
        this();
        this.author = author;
    }

    public void setUseJDO(boolean useJDO) {
        this.useJDO = useJDO;
    }

    public List<Transcription> listTranscriptionByAccount(String account) throws Exception {
        List<Transcription> transcriptions = transcriptionDAO.listAll(100);
        Map<String, RecordedSentence> recordedSentenceMap = recordedSentenceDAO.getByAccount(account);
        if (transcriptions != null && transcriptions.size() > 0 && recordedSentenceMap != null && recordedSentenceMap.size() > 0) {
            for (final Transcription transcription : transcriptions) {
                if (recordedSentenceMap.containsKey(transcription.getId())) {
                    transcription.setStatus(recordedSentenceMap.get(transcription.getId()).getStatus());
                }
            }
        }
        return transcriptions;
    }


    public RecordedSentenceHistory handleUploadedSentence(UserProfile user, String sentenceId, File recordedVoice) throws Exception {
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

        if (recordedSentenceDAO.put(recordedSentence)) {
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
        return null;
    }

    private String verifySentence(String sentence) throws Exception {
        while (sentence.contains("  ") || sentence.contains("\t")) {
            sentence = sentence.replace("\t", " ");
            sentence = sentence.replace("  ", " ");
        }
        sentence = sentence.trim().toUpperCase();
        final String[] words = sentence.split(" ");

        final Map<String, List<Integer>> replacedUKWord = new HashMap<String, List<Integer>>();
        final Map<String, List<Integer>> notFoundInBeep = new HashMap<String, List<Integer>>();
        final Map<String, List<Integer>> containSpecialChar = new HashMap<String, List<Integer>>();
        boolean isCorrected = true;
        StringBuffer sb = new StringBuffer();
        sb.append("<tr>");
        sb.append("<td><a id=\""
                + (transcriptionResult.loadedCount+1)
                + "\" href=\"#"
                + (transcriptionResult.loadedCount+1) + "\">"
                + (transcriptionResult.loadedCount+1) +
                "</a></td>");
        sb.append("<td>");
        Map<String, Integer> phones = new HashMap<String, Integer>();
        for (int i = 0; i < words.length; i++) {
            String ukW = dictionaryHelper.getUKWord(words[i]);
            sb.append("<span class=\"");
            String title = "";
            if (ukW.length() > 0) {
                addCountItem(replacedUKWord, words[i], transcriptionResult.loadedCount);
                title = words[i];
                words[i] = ukW;
                sb.append("rp ");
            }
            List<String> correctPhonemes = dictionaryHelper.getCorrectPhonemes(words[i]);
            if (correctPhonemes == null  || correctPhonemes.size() == 0) {
                isCorrected = false;
                addCountItem(notFoundInBeep, words[i], transcriptionResult.loadedCount);
                sb.append("nb ");
            } else {
                for (String p : correctPhonemes) {
                    p = p.toUpperCase();
                    if (phones.containsKey(p)) {
                        int j = phones.get(p);
                        phones.put(p, j + 1);
                    } else {
                        phones.put(p, 1);
                    }
                }
            }
            if (!StringUtils.isAlpha(words[i])) {
                isCorrected = false;
                addCountItem(containSpecialChar, words[i], transcriptionResult.loadedCount);
                sb.append("na ");
            }
            sb.append("\"");
            if (title.length() > 0) {
                sb.append(" title=\"" + title + "\"");
            }
            sb.append(">");
            sb.append(words[i]);
            sb.append("</span>&nbsp;");
            if (!transcriptionResult.words.contains(words[i])) {
                transcriptionResult.words.add(words[i]);
            }
        }
        sb.append("</td>");
        sb.append("</tr>");
        String output = StringUtils.join(words, " ");
        if (!isExistSentence(output)) {
            writeHtmlToResult(sb.toString());
            mergeMapCountItems(transcriptionResult.replacedUKWord, replacedUKWord);
            mergeMapCountItems(transcriptionResult.containSpecialChar, containSpecialChar);
            mergeMapCountItems(transcriptionResult.notFoundInBeep, notFoundInBeep);
            if (phones.size() > 0) {
                Iterator<String> keys = phones.keySet().iterator();
                while (keys.hasNext()) {
                    String p = keys.next();
                    int count = phones.get(p);
                    transcriptionResult.phones.put(p, transcriptionResult.phones.get(p) + count);
                }
            }
            if (isCorrected) {
                transcriptionResult.correctedSentenceCount++;
            }
            transcriptionResult.loadedCount++;
        } else {
            output = "";
        }
        return output;
    }

    private void mergeMapCountItems(final Map<String, List<Integer>> map, final Map<String, List<Integer>> fromMap) {
        if (fromMap.size() > 0) {
            Iterator<String> keys = fromMap.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                List<Integer> list = fromMap.get(key);
                if (list.size() > 0) {
                    if (map.containsKey(key)) {
                        map.get(key).addAll(list);
                    } else {
                        map.put(key, list);
                    }
                }
            }
        }
    }

    private void addCountItem(final Map<String, List<Integer>> map, String word, int index) {
        if (map.containsKey(word)) {
            map.get(word).add(index);
        } else {
            final List<Integer> list = new ArrayList<Integer>();
            list.add(index);
            map.put(word, list);
        }
    }


    private boolean isExistSentence(String sentence) throws Exception {
        if (useJDO) {
            return transcriptionDAO.getBySentence(sentence) != null;
        } else {
            return SENTENCES.contains(sentence);
        }
    }

    private boolean saveSentence(String sentence) throws Exception {
        try {
            if (useJDO) {
                Transcription transcription = new Transcription();
                transcription.setAuthor(author);
                transcription.setCreatedDate(new Date(System.currentTimeMillis()));
                transcription.setModifiedDate(new Date(System.currentTimeMillis()));
                transcription.setSentence(sentence);
                return transcriptionDAO.put(transcription);
            } else {
                SENTENCES.add(sentence);
                return true;
            }
        } finally {
            logger.info("Save transcription: " + sentence);
        }
    }

    private void recycleSentences() throws Exception {
        if (resultHtmlFile != null && resultHtmlFile.exists()) {
            FileUtils.forceDelete(resultHtmlFile);
        }
        resultHtmlFile = new File(FileUtils.getTempDirectory(), "transcription-result-" + UUIDGenerator.generateUUID() + ".html");
        transcriptionResult = new LoadTranscriptionResult();
        if (useJDO) {
            transcriptionDAO.deleteAll();
        } else {
            SENTENCES.clear();
        }
        List<String> phones = IOUtils.readLines(this.getClass().getClassLoader().getResourceAsStream("amt/phones"));
        for (String phone : phones) {
            String p = phone.toUpperCase();
            if (!p.equalsIgnoreCase("sil") && !transcriptionResult.phones.containsKey(p)) {
                transcriptionResult.phones.put(p, 0);
            }
        }
        writeResourceToResult("amt/template/result-header.html");
    }

    private void writePhonesScanResult() throws Exception {
        if (transcriptionResult.phones.size()  == 0) return;
        writeHtmlToResult("<table id=\"TB-PHONES\" cellspacing=\"3\" cellpadding=\"3\">");
        writeHtmlToResult("<tr><th class=\"tl\" colspan=\"2\">PHONEMES ANALYZING RESULT</th></tr>");
        Iterator<String> phones = transcriptionResult.phones.keySet().iterator();
        while (phones.hasNext()) {
            String phone = phones.next();
            int count = transcriptionResult.phones.get(phone);
            transcriptionResult.totalPhones += count;
            writeHtmlToResult("<tr>");
            writeHtmlToResult("<td>"  + phone + "</td>");
            writeHtmlToResult("<td>"  + count + "</td>");
            writeHtmlToResult("</tr>");
        }
        writeHtmlToResult("<tr><td class=\"tl\" colspan=\"2\">Total phonemes count: "  + transcriptionResult.totalPhones + "</td></tr>");
        writeHtmlToResult("<tr><td class=\"tl\" colspan=\"2\">AVG phonemes count: "  + (transcriptionResult.totalPhones / transcriptionResult.phones.size()) + "</td></tr>");
        writeHtmlToResult("</table>");
    }

    private void completeSentences() throws Exception {
       // logger.info("Result: " + gson.toJson(transcriptionResult));
        writeHtmlToResult("<hr/>");
        drawTableResult(transcriptionResult.notFoundInBeep, "TB-NB", "NOT FOUND IN BEEP DICTIONARY");
        writeHtmlToResult("<hr/>");
        drawTableResult(transcriptionResult.replacedUKWord, "TB-RP", "REPLACED BY UK WORDS");
        writeHtmlToResult("<hr/>");
        drawTableResult(transcriptionResult.containSpecialChar, "TB-NA", "CONTAINS SPECIAL CHARACTERS");
        writeHtmlToResult("<hr/>");
        writePhonesScanResult();
        writeResourceToResult("amt/template/result-output.html");
        writeHtmlToResult("<hr/>");
        writeHtmlToResult("<table cellspacing=\"3\" cellpadding=\"3\">");
        writeHtmlToResult("<tr><td>Total sentences</td><td>" + transcriptionResult.loadedCount + "</td></tr>");
        writeHtmlToResult("<tr><td>Corrected sentences</td><td>" +
                transcriptionResult.correctedSentenceCount
                + " ("
                + getPercentText(transcriptionResult.correctedSentenceCount, transcriptionResult.loadedCount)
                + ")</td></tr>");
        writeHtmlToResult("<tr><td>BEEP dictionary words</td><td>" + dictionaryHelper.getBeepSize() + "</td></tr>");
        writeHtmlToResult("<tr><td>Total words</td><td>" + transcriptionResult.words.size() + " ("
                + getPercentText(transcriptionResult.words.size() , dictionaryHelper.getBeepSize())
                + " of BEEP)</td></tr>");
        writeHtmlToResult("<tr><td>Replaced by UK</td><td>" + transcriptionResult.replacedUKWord.size() + " ("
                + getPercentText(transcriptionResult.replacedUKWord.size(), transcriptionResult.words.size())
                + " of total words found)</td></tr>");
        writeHtmlToResult("<tr><td>Not found in BEEP</td><td>" + transcriptionResult.notFoundInBeep.size() + " ("
                + getPercentText(transcriptionResult.notFoundInBeep.size(), transcriptionResult.words.size())
                + " of total words found)</td></tr>");
        writeHtmlToResult("<tr><td>Contains special char</td><td>" + transcriptionResult.containSpecialChar.size() + " ("
                + getPercentText(transcriptionResult.containSpecialChar.size(), transcriptionResult.words.size())
                + " of total words found)</td></tr>");
        writeHtmlToResult("<tr><td><a href=\"#TB-PHONES\">Total phonemes count</a></td><td>"  + transcriptionResult.totalPhones + "</td></tr>");
        if (transcriptionResult.phones.size() > 0)
            writeHtmlToResult("<tr><td><a href=\"#TB-PHONES\">AVG phonemes count</a></td><td>"  + (transcriptionResult.totalPhones / transcriptionResult.phones.size()) + "</td></tr>");
        writeHtmlToResult("</table>");
        writeHtmlToResult("</div></div>");

        writeResourceToResult("amt/template/result-footer.html");
        logger.info("Result file path: " + resultHtmlFile.toURI());
    }

    private String getPercentText(int a, int b) {
        if (b == 0) return "100%";
        return Math.round(100 * (float) a / b) + "%";
    }

    private void drawTableResult(Map<String, List<Integer>> map, String tableId, String tableName) throws Exception {
        writeHtmlToResult("<table id=\"" + tableId + "\" cellspacing=\"3\" cellpadding=\"3\">");
        writeHtmlToResult("<tr><th class=\"tl\" colspan=\"3\">" + tableName + "</th></tr>");
        writeHtmlToResult("<tr><th class=\"tl\">WORD</th><th class=\"tl\">COUNT</th><th class=\"tl\">INDEXES</th></tr>");
        if (map.size() > 0) {
            Iterator<String> words = map.keySet().iterator();
            while (words.hasNext()) {
                String word = words.next();
                List<Integer> indexes = map.get(word);
                StringBuffer sb = new StringBuffer();
                sb.append("<tr><td>");
                String title = "";
                String ukW = dictionaryHelper.getUKWord(word);
                sb.append("<span class=\"");
                if (ukW.length() > 0) {
                    title = ukW;
                    sb.append("rp ");
                }
                List<String> correctPhonemes = dictionaryHelper.getCorrectPhonemes(word);
                if (correctPhonemes == null  || correctPhonemes.size() == 0) {
                    sb.append("nb ");
                }
                if (!StringUtils.isAlpha(word)) {
                    sb.append("na ");
                }
                sb.append("\"");
                if (title.length() > 0) {
                    sb.append(" title=\"" + title + "\"");
                }
                sb.append(">");
                sb.append(word);
                sb.append("</span>");
                sb.append("</td>");
                sb.append("<td>" + (indexes == null ? 0 : indexes.size()) + "</td>");
                sb.append("<td>");
                if (indexes != null && indexes.size() > 0) {
                    for (Integer index : indexes) {
                        sb.append("<a href=\"");
                        sb.append("#" + (index + 1));
                        sb.append("\">");
                        sb.append("#" + (index + 1));
                        sb.append("</a>&nbsp;");
                    }
                }
                sb.append("</td></tr>");
                writeHtmlToResult(sb.toString());
            }
        }
        writeHtmlToResult("</table>");
    }

    public void loadTranscription(InputStream is) throws Exception {
        synchronized (lock) {
            try {
                transcriptionDAO.deleteAll();
                recycleSentences();
                List<String> transcriptions = IOUtils.readLines(is);
                if (transcriptions != null && transcriptions.size() > 0) {
                    writeHtmlToResult("<table cellspacing=\"3\" cellpadding=\"3\">");
                    for (String t : transcriptions) {
                        t = t.trim();
                        if (t.length() > 0) {
                            String sentence;
                            if (t.contains("(")) {
                                sentence = t.substring(0, t.lastIndexOf("(")).trim();
                            } else {
                                sentence = t.trim();
                            }
                            if (sentence.startsWith("<s>"))
                                sentence = sentence.substring("<s>".length(), sentence.length());
                            if (sentence.endsWith("</s>"))
                                sentence = sentence.substring(0, sentence.length() - "</s>".length());
                            sentence = verifySentence(sentence.trim());
                            if (sentence.length() > 0)
                                saveSentence(sentence);
                        }
                    }writeHtmlToResult("</table>");
                    logger.info("Saved " + transcriptionResult.loadedCount + " transcriptions");
                } else {
                    logger.error("No default transcription found");
                }
            } catch (Exception e) {
                throw e;
            } finally {
                if (is != null) {
                    is.close();
                }

                completeSentences();
            }
        }
    }

    public void loadTranscription(File f) throws Exception {
        loadTranscription(new FileInputStream(f));
    }

    public void loadTranscription() throws Exception {
        loadTranscription(this.getClass().getClassLoader().getResourceAsStream("amt/voxforge_en_sphinx.transcription"));
    }

    private void writeHtmlToResult(String html) throws IOException {
        FileUtils.write(resultHtmlFile,
                html
                , "UTF-8",
                true);
    }

    private void writeResourceToResult(String resource) throws IOException {
        FileUtils.write(resultHtmlFile,
                IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream(resource), "UTF-8")
                , "UTF-8",
                true);
    }

    public File getResultHtmlFile() {
        return resultHtmlFile;
    }

    public static void main(String[] args) {
        TranscriptionService service = new TranscriptionService();
        service.setUseJDO(true);
        try {
            service.loadTranscription();
            FileUtils.copyFile(service.getResultHtmlFile(), new File("/Users/cmg/Desktop/transcription-analyzing-result.html"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

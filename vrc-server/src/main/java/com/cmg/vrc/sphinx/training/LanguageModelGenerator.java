package com.cmg.vrc.sphinx.training;

import com.cmg.vrc.data.dao.impl.TranscriptionDAO;
import com.cmg.vrc.data.jdo.Transcription;
import com.cmg.vrc.processor.CommandExecutor;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by cmg on 10/09/15.
 */
public class LanguageModelGenerator {

    public File getExtraDir() {
        return extraDir;
    }

    public void setExtraDir(File extraDir) {
        this.extraDir = extraDir;
    }

    public interface TrainingListener {
        void onMessage(String message);

        void onError(String message, Throwable e);

        void onSuccess(File languageModel) throws Exception;
    }

    private final TrainingListener listener;

    private final TranscriptionDAO transcriptionDAO;

    private final File tmpDir;

    private File extraDir;

    public LanguageModelGenerator(TrainingListener listener, File targetDir) {
        this.listener = listener;
        this.transcriptionDAO = new TranscriptionDAO();
        this.tmpDir = targetDir;
        if (!tmpDir.exists())
            tmpDir.mkdirs();
    }

    private void addExtraSentences(File fileSentences) throws IOException {
        if (extraDir != null && extraDir.exists() && extraDir.isDirectory()) {
            listener.onMessage("Found extra directory " + extraDir);
            List<String> transcriptions = FileUtils.readLines(new File(extraDir, "ext.transcription"), "UTF-8");
            for (String transcription : transcriptions) {
                String sentence = transcription.substring(0, transcription.lastIndexOf("(") - 1);
                sentence = sentence.trim().toUpperCase();
                FileUtils.writeStringToFile(fileSentences, "<s> " + sentence.toLowerCase().trim() + " </s>\n", "UTF-8", true);
            }
        }
    }

    public void training() {
        try {
            listener.onMessage("Fetch all sentences from database");
            List<Transcription> transcriptions =  transcriptionDAO.listAll();
            if (transcriptions != null && transcriptions.size() > 0) {
                String script = "script.sh";
                File fileScript = new File(tmpDir, "script.sh");
                if (fileScript.exists())
                    FileUtils.forceDelete(fileScript);
                String sentencesTxt = "sentences.txt";
                String sentencesVocab = "sentences.vocab";
                String languageModel = "output.lm";
                String idngram = "sentences.idngram";
                File fileSentences = new File(tmpDir, sentencesTxt);
                File fileVocab = new File(tmpDir, sentencesVocab);
                File fileIdNgram = new File(tmpDir, idngram);
                File fileLanguageModel = new File(tmpDir, languageModel);
                if (fileSentences.exists())
                    FileUtils.forceDelete(fileSentences);
                listener.onMessage("Found " + transcriptions.size() + " transcription");
                listener.onMessage("Save to tmp file " + fileSentences);
                for (Transcription transcription : transcriptions) {
                    String sentence = transcription.getSentence();
                    if (sentence.length() > 0) {
                        FileUtils.writeStringToFile(fileSentences, "<s> " + sentence.toLowerCase().trim() + " </s>\n", "UTF-8", true);
                    }
                }
                addExtraSentences(fileSentences);
                listener.onMessage("Sentences saved successfully!");
                FileUtils.write(fileScript, "LD_LIBRARY_PATH=/usr/local/lib\nexport LD_LIBRARY_PATH\n", "UTF-8", true);
                String command = "text2wfreq < " + fileSentences.getAbsolutePath() + " | wfreq2vocab > " + fileVocab.getAbsolutePath();
                listener.onMessage("Append command: " + command);
                FileUtils.write(fileScript, command + "\n", "UTF-8", true);
                command = "text2idngram -vocab " + fileVocab.getAbsolutePath()
                        + " -idngram " + fileIdNgram.getAbsolutePath()
                        + " < " + fileSentences.getAbsolutePath();
                listener.onMessage("Append command: " + command);
                FileUtils.write(fileScript, command + "\n", "UTF-8", true);

                command = "idngram2lm -vocab_type 0 -idngram " + fileIdNgram.getAbsolutePath()
                        + " -vocab " + fileVocab.getAbsolutePath()
                        + " -arpa " + fileLanguageModel.getAbsolutePath();
                listener.onMessage("Append command: " + command);
                FileUtils.write(fileScript, command + "\n", "UTF-8", true);
                listener.onMessage("Run generate the ARPA format language model script " + script);
                executeCommand(null,"sh", fileScript.getAbsolutePath());
                try {
                    if (fileLanguageModel.exists()) {
                        listener.onMessage("Language model generated successfully!");
                        listener.onSuccess(fileLanguageModel);
                    } else {
                        listener.onError("Could not generate language model", new FileNotFoundException(
                                "Could not found file " + fileLanguageModel
                        ));
                    }
                } finally {
                    if (fileVocab.exists())
                        FileUtils.forceDelete(fileVocab);
                    if (fileIdNgram.exists())
                        FileUtils.forceDelete(fileIdNgram);
                }
            } else {
                listener.onError("No transcription found", null);
            }
        } catch (Exception e) {
            listener.onError("Could not training language model", e);
        }
    }

    private void executeCommand(File targetDir, String... commands) throws IOException, InterruptedException {
        CommandExecutor.execute(targetDir, new CommandExecutor.CommandListener() {
            @Override
            public void onMessage(String message) {
                listener.onMessage(message);
            }

            @Override
            public void onError(String message, Throwable e) {
                listener.onError(message, e);
            }
        }, commands);
    }
}

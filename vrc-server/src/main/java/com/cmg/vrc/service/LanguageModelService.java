package com.cmg.vrc.service;

import com.cmg.vrc.data.dao.impl.TranscriptionDAO;
import com.cmg.vrc.data.jdo.Transcription;
import com.cmg.vrc.util.UUIDGenerator;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.List;

/**
 * Created by cmg on 10/09/15.
 */
public class LanguageModelService {

    public interface TrainingListener {
        public void onMessage(String message);

        public void onError(String message, Throwable e);

        public void onSuccess(File languageModel);
    }

    private final TrainingListener listener;

    private final TranscriptionDAO transcriptionDAO;

    private final File tmpDir;

    public LanguageModelService(TrainingListener listener) {
        this.listener = listener;
        this.transcriptionDAO = new TranscriptionDAO();
        this.tmpDir = new File(FileUtils.getTempDirectory(), UUIDGenerator.generateUUID() + "_lm");
        if (!tmpDir.exists())
            tmpDir.mkdirs();
    }

    public synchronized void training() {
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
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        if (targetDir != null)
            processBuilder.directory(targetDir);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String readLine;
        while ((readLine = processOutputReader.readLine()) != null) {
            listener.onMessage(readLine);
        }
        process.waitFor();
    }

    public static void main(String[] args) {
        LanguageModelService languageModelService = new LanguageModelService(new TrainingListener() {
            @Override
            public void onMessage(String message) {
                System.out.println(message);
            }

            @Override
            public void onError(String message, Throwable e) {
                System.out.println(message);
                e.printStackTrace();
            }

            @Override
            public void onSuccess(File languageModel) {
                try {
                    FileUtils.moveFile(languageModel, new File("/Users/cmg/Desktop/languagemodel.lm"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        languageModelService.training();
    }
}

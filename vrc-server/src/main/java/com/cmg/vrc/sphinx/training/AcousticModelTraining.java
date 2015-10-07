package com.cmg.vrc.sphinx.training;

import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.dao.impl.DictionaryVersionDAO;
import com.cmg.vrc.data.dao.impl.LanguageModelVersionDAO;
import com.cmg.vrc.data.dao.impl.RecorderDAO;
import com.cmg.vrc.data.jdo.AcousticModelVersion;
import com.cmg.vrc.data.jdo.DictionaryVersion;
import com.cmg.vrc.data.jdo.LanguageModelVersion;
import com.cmg.vrc.sphinx.DictionaryHelper;
import com.cmg.vrc.util.AWSHelper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cmg on 05/10/2015.
 */
public class AcousticModelTraining {
    /**
     * Number of tied states (senones) to create in decision-tree clustering
     */
    private static final int DEFAULT_SENONES = 200;
    /**
     *
     */
    private static final int DEFAULT_DENSITIES = 8;

    public boolean isTrainCiModelEnabled() {
        return isTrainCiModelEnabled;
    }

    public void setIsTrainCiModelEnabled(boolean isTrainCiModelEnabled) {
        this.isTrainCiModelEnabled = isTrainCiModelEnabled;
    }

    public File getExtraDir() {
        return extraDir;
    }

    public void setExtraDir(File extraDir) {
        this.extraDir = extraDir;
    }

    public interface TrainingListener {

        void onMessage(String message);

        void onError(String message, Throwable e);

        void onSuccess(AcousticModelVersion amv);
    }

    public interface FetchSentencesListener {
        void onDetectSentence(String id, String account, String sentence, String fileName) throws Exception;
    }

    private final File rootDir;

    private final String projectName;

    private final TrainingListener listener;

    private File etcDir;

    private File wavDir;

    private AWSHelper awsHelper;

    private DictionaryVersion dictionaryVersion;

    private LanguageModelVersion languageModelVersion;

    private long count;

    private boolean isTrainCiModelEnabled = false;

    private File extraDir;

    private List<String> allWords;

    private DictionaryHelper dictionaryHelper;

    private File dictionaryFile;

    private File testFileIds;

    private File trainFileIds;

    private File testTranscription;

    private File trainTranscription;

    public AcousticModelTraining(File rootDir, String projectName, TrainingListener listener) {
        this.rootDir = rootDir;
        this.projectName = projectName;
        this.listener = listener;
        this.awsHelper = new AWSHelper();

    }

    public void train() throws Exception {
        count = 0;
        prepare();
        doTraining();
    }

    private void prepare() throws Exception {
        prepareDir();
        prepareAdditionalData();
        dictionaryFile = new File(etcDir, projectName + ".dic");
        testFileIds = new File(etcDir, projectName + "_test.fileids");
        trainFileIds = new File(etcDir, projectName + "_train.fileids");
        testTranscription = new File(etcDir, projectName + "_test.transcription");
        trainTranscription = new File(etcDir, projectName + "_train.transcription");
        dictionaryHelper = new DictionaryHelper(DictionaryHelper.Type.BEEP,
                Constant.FOLDER_DICTIONARY
                + "/"
                + dictionaryVersion.getFileName());

        if (dictionaryFile.exists()) {
            FileUtils.forceDelete(dictionaryFile);
        }
        allWords = new ArrayList<String>();
        listener.onMessage("Start fetch all recorded sentences for training");
        new RecorderDAO().listTrainingSentences(new FetchSentencesListener() {
            @Override
            public void onDetectSentence(String id, String account, String sentence, String fileName) throws Exception {
                listener.onMessage("Detect recorded sentence id " + id + ". Account: " + account + ". File name: " + fileName
                        + ". Sentence: " + sentence);
                File targetWavDir = new File(wavDir, account);
                checkDir(targetWavDir);
                String fileId = id + "_" + count++;
                String audioKey = Constant.FOLDER_REOCORDED_VOICES_AMT + "/" + account + "/" + fileName;
                File audioFile = new File(targetWavDir, fileName);
                if (!audioFile.exists()) {
                    listener.onMessage("Download WAV file " + audioKey + " from S3 to " + audioFile);
                    awsHelper.download(audioKey, audioFile);
                }
                File verifiedAudioFile = new File(targetWavDir, fileId + ".wav");
                listener.onMessage("Verify WAV file " + audioFile + " to " + verifiedAudioFile);
                verifyAudioFile(audioFile, verifiedAudioFile);
                listener.onMessage("Write data to fileids and transcription files");
                while (sentence.contains("  ")) {
                    sentence = sentence.replace("  ", " ");
                }
                sentence = sentence.trim().toUpperCase();
                String[] words = sentence.split(" ");
                for (String word : words) {
                    if (!allWords.contains(word)) {
                        List<String> phones = dictionaryHelper.getCorrectPhonemes(word);
                        if (phones != null) {
                            FileUtils.write(dictionaryFile, word.toLowerCase() + " " + StringUtils.join(phones, " ") + "\n",  "UTF-8", true);
                        } else {
                            throw new TrainingException("Could not found phones for word " + word + " in sentence " + sentence);
                        }
                        allWords.add(word);
                    }
                }
                String fullSentence = "<s> " + sentence.toLowerCase() + " </s>";
                FileUtils.writeStringToFile(testFileIds, account + "/" + fileId + "\n", "UTF-8", true);
                FileUtils.writeStringToFile(trainFileIds, account + "/" + fileId + "\n", "UTF-8", true);
                FileUtils.writeStringToFile(testTranscription, fullSentence + " (" + fileId + ")" + "\n", "UTF-8", true);
                FileUtils.writeStringToFile(trainTranscription, fullSentence + " (" + fileId + ")" + "\n", "UTF-8", true);
            }
        });
        addExtraRecordedSentences();
        runConfigure();
    }

    private void addExtraRecordedSentences() throws Exception {
        if (extraDir != null && extraDir.exists() && extraDir.isDirectory()) {
            listener.onMessage("Found extra directory " + extraDir);
            List<String> fileids = FileUtils.readLines(new File(extraDir, "ext.fileids"), "UTF-8");
            List<String> transcriptions = FileUtils.readLines(new File(extraDir, "ext.transcription"), "UTF-8");
            int size = fileids.size();
            if (fileids.size() == transcriptions.size()) {
                for (int i = 0; i < size; i++) {
                    String fileId = fileids.get(i);
                    String transcription = transcriptions.get(i);
                    String sId = transcription.substring(transcription.lastIndexOf("(") + 1, transcription.length() - 1).trim();
                    String sentence = transcription.substring(0, transcription.lastIndexOf("(") - 1);
                    while (sentence.contains("  "))
                        sentence = sentence.replace("  ", " ");
                    sentence = sentence.trim();
                    String[] words = sentence.split(" ");
                    List<String> sentenceWords = new ArrayList<String>();
                    boolean isValid = true;
                    for (String word : words) {
                        if (!allWords.contains(word) && !sentenceWords.contains(word)) {
                            List<String> phones = dictionaryHelper.getCorrectPhonemes(word);
                            if (phones == null) {
                                listener.onMessage("No phones found for word " + word + ". Skip by default");
                                isValid = false;
                                break;
                            }
                            sentenceWords.add(word);
                        }
                    }
                    if (isValid) {
                        File wav = new File(extraDir, "wav" + File.separator + fileId + ".wav");
                        if (!wav.exists()) {
                            listener.onMessage("No wav file " + wav + " found for sentence " + sentence + ". Skip by default");
                        } else {
                            File targetWav = new File(wavDir, fileId + ".wav");
                            File parent = targetWav.getParentFile();
                            if (!parent.exists() || !parent.isDirectory())
                                parent.mkdirs();
                            FileUtils.copyFile(wav, targetWav);
                            for (String word : sentenceWords) {
                                List<String> phones = dictionaryHelper.getCorrectPhonemes(word);
                                FileUtils.write(dictionaryFile, word.toLowerCase() + " " + StringUtils.join(phones, " ") + "\n", "UTF-8", true);
                                if (!allWords.contains(word))
                                    allWords.add(word);
                            }
                            String fullSentence = "<s> " + sentence.toLowerCase() + " </s>";
                            FileUtils.writeStringToFile(testFileIds, fileId + "\n", "UTF-8", true);
                            FileUtils.writeStringToFile(trainFileIds, fileId + "\n", "UTF-8", true);
                            FileUtils.writeStringToFile(testTranscription, fullSentence + " (" + sId + ")" + "\n", "UTF-8", true);
                            FileUtils.writeStringToFile(trainTranscription, fullSentence + " (" + sId + ")" + "\n", "UTF-8", true);
                            listener.onMessage("Add extra sentence to training list " + sentence + ". WAV file " + targetWav);
                        }
                    } else {
                        listener.onMessage("Skip invalid sentence " + sentence);
                    }
                }
            } else {
                throw new TrainingException("Sentences size not matched");
            }
        }
    }

    private void prepareAdditionalData() throws Exception {
        listener.onMessage("Get selected dictionary");
        dictionaryVersion = new DictionaryVersionDAO().getSelectedVersion();
        if (dictionaryVersion != null) {
            listener.onMessage("Found selected dictionary " + dictionaryVersion.getFileName());
        } else {
            throw new TrainingException("No selected dictionary found");
        }
        languageModelVersion = new LanguageModelVersionDAO().getSelectedVersion();
        if (languageModelVersion != null) {
            listener.onMessage("Found selected language model " + languageModelVersion.getFileName());
            File target = new File(etcDir, projectName + ".lm");
            if (!target.exists())
                awsHelper.download(Constant.FOLDER_LANGUAGE_MODEL
                        + "/"
                        + languageModelVersion.getFileName(), target);
            listener.onMessage("Save language model to " + target);
        } else {
            throw new TrainingException("No selected language model found");
        }
        listener.onMessage("Download phone template");
        File target = new File(etcDir, projectName + ".phone");
        if (!target.exists())
            awsHelper.download("training/template.phone",target);
        listener.onMessage("========== " + projectName + ".phone" + " =========");
        listener.onMessage(FileUtils.readFileToString(target, "UTF-8"));
        listener.onMessage("===================================================");
        listener.onMessage("Download filler template");
        target = new File(etcDir, projectName + ".filler");
        if (!target.exists())
            awsHelper.download("training/template.filler",target);
        listener.onMessage("========== " + projectName + ".filler" + " =========");
        listener.onMessage(FileUtils.readFileToString(target, "UTF-8"));
        listener.onMessage("===================================================");
    }

    private void runConfigure() throws IOException, InterruptedException, TrainingException {
        File cfg = new File(etcDir, "sphinx_train.cfg");
        listener.onMessage("Generate configuration file " + cfg);
        StringBuilder command = new StringBuilder("");
        command.append("cd ").append("\"").append(rootDir.getAbsolutePath()).append("\"").append("\n")
            .append("sphinxtrain -t ").append(projectName).append(" setup");
        File shScript = new File(rootDir, "configure.sh");
        FileUtils.writeStringToFile(shScript, command.toString(), "UTF-8");
        executeCommand(null, "sh", shScript.getAbsolutePath());
        if (cfg.exists()) {
            listener.onMessage("Detect configuration file " + cfg);
            List<String> cfgContent = FileUtils.readLines(cfg, "UTF-8");
            List<String> newCfgContent = new ArrayList<String>();
            for (String line : cfgContent) {
                if (line.startsWith("$CFG_N_TIED_STATES")) {
                    line = "$CFG_N_TIED_STATES = " + DEFAULT_SENONES + ";";
                } else if (line.startsWith("$DEC_CFG_LANGUAGEMODEL")) {
                    line = "$DEC_CFG_LANGUAGEMODEL  = \"$CFG_BASE_DIR/etc/${CFG_DB_NAME}.lm\";";
                } else if (line.startsWith("$CFG_CD_TRAIN")) {
                    line = "$CFG_CD_TRAIN = '" + (isTrainCiModelEnabled ? "no" : "yes") + "';";
                }
                newCfgContent.add(line);
            }
            listener.onMessage("Save re-configuration file to " + cfg);
            FileUtils.writeLines(cfg, "UTF-8", newCfgContent, "\n");
        } else {
            throw new TrainingException("Could not create configuration file " + cfg);
        }
    }

    private void prepareDir() throws IOException {
        listener.onMessage("Prepare root directory " + rootDir);
        checkDir(rootDir);
        etcDir = new File(rootDir, "etc");
        listener.onMessage("Prepare directory ETC " + etcDir);
        if (etcDir.exists())
            FileUtils.forceDelete(etcDir);
        checkDir(etcDir);
        wavDir = new File(rootDir, "wav");
        listener.onMessage("Prepare directory WAV " + wavDir);
        checkDir(wavDir);
    }

    private void checkDir(File dir) {
        if (!dir.exists() || !dir.isDirectory())
            dir.mkdirs();
    }

    private void verifyAudioFile(File src, File dest) throws IOException {
        if (dest.exists())
            FileUtils.forceDelete(dest);
        //TODO If audio file is not valid format, try to use FFMPEG to verify that
        FileUtils.moveFile(src, dest);
    }

    private void doTraining() throws IOException, InterruptedException {
        listener.onMessage("Start training acoustic model");
        StringBuilder command = new StringBuilder("");
        command.append("cd ").append("\"").append(rootDir.getAbsolutePath()).append("\"").append("\n")
                .append("sphinxtrain run");
        File shScript = new File(rootDir, "train.sh");
        FileUtils.writeStringToFile(shScript, command.toString(), "UTF-8");
        executeCommand(null, "sh", shScript.getAbsolutePath());
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
        AcousticModelTraining training = new AcousticModelTraining(new File("/Users/cmg/Documents/training/amt_test"), "amt_test", new TrainingListener() {
            @Override
            public void onMessage(String message) {
                System.out.println(message);
            }

            @Override
            public void onError(String message, Throwable e) {
                System.out.println("Error: " + message);
                if (e != null)
                    e.printStackTrace();
            }

            @Override
            public void onSuccess(AcousticModelVersion amv) {

            }
        });
        try {
            training.setExtraDir(new File("/Users/cmg/Documents/training/ext-training"));
            training.train();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

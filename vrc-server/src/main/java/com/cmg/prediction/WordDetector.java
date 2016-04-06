package com.cmg.prediction;

import com.cmg.vrc.util.AWSHelper;
import com.cmg.vrc.util.FileHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.result.WordResult;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by luhonghai on 10/7/14.
 */

public class WordDetector {

    private static final Logger logger = Logger.getLogger(WordDetector.class.getName());

    private final File target;

    private StreamSpeechRecognizer recognizer;

    private final File sphinx4DataTmpDir = FileHelper.getTmpSphinx4DataDir();

    private List<WordFrame> wordFrames;

    public WordDetector(File target) {
        this.target = target;
    }

    public void init() {
        if (recognizer == null) {
            AWSHelper awsHelper = new AWSHelper();

            Configuration conf = new Configuration();

            File tmpAcousticModelDir = new File(sphinx4DataTmpDir, "cmusphinx-en-us-5.2");
            if (!tmpAcousticModelDir.exists()) {
                awsHelper.downloadAndUnzip("sphinx-data/cmusphinx-en-us-5.2.zip", sphinx4DataTmpDir);
            }
            if (tmpAcousticModelDir.exists()) {
                conf.setAcousticModelPath(tmpAcousticModelDir.toURI().toString());
            } else {
                conf.setAcousticModelPath(com.cmg.vrc.properties.Configuration.getValue(com.cmg.vrc.properties.Configuration.ACOUSTIC_MODEL_PATH));
            }
            File tmpPhonemesDictFile = new File(sphinx4DataTmpDir, "cmudict-en-us.dict");
            if (!tmpPhonemesDictFile.exists()) {
                awsHelper.download("sphinx-data/dict/cmudict-en-us.dict", tmpPhonemesDictFile);
            }
            if (tmpPhonemesDictFile.exists()) {
                conf.setDictionaryPath(tmpPhonemesDictFile.toURI().toString());
            } else {
                conf.setDictionaryPath(com.cmg.vrc.properties.Configuration.getValue(com.cmg.vrc.properties.Configuration.DICTIONARY_PATH));
            }

            File tmpLanguageModel = new File(sphinx4DataTmpDir, "cmusphinx-5.0-en-us.lm.dmp");
            if (!tmpLanguageModel.exists()) {
                awsHelper.download("sphinx-data/lm/cmusphinx-5.0-en-us.lm.dmp", tmpLanguageModel);
            }
            if (tmpLanguageModel.exists()) {
                conf.setLanguageModelPath(tmpLanguageModel.getAbsolutePath());
            }
            try {
                recognizer =
                        new StreamSpeechRecognizer(conf);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Could not init recognizer", e);
            }
        }
    }

    public void close() {
        if (recognizer != null) {
            recognizer.stopRecognition();
        }
    }


    public List<WordFrame> analyze() throws IOException {
        wordFrames = Collections.synchronizedList(new ArrayList<WordFrame>());
        init();
        InputStream stream = null;
        if (target.exists() && !target.isDirectory()) {
            logger.info("Start analyze WAV record: " + target);
            try {
                stream = new FileInputStream(target);
                recognizer.startRecognition(stream);
                SpeechResult result;
                while ((result = recognizer.getResult()) != null) {
                    logger.info(result.getHypothesis());
                    List<WordResult> resultList = result.getWords();
                    if (resultList != null && resultList.size() > 0) {
                        for (WordResult wordResult : resultList) {
                            WordFrame wordFrame = new WordFrame();
                            wordFrame.setWord(wordResult.getWord().toString());
                            wordFrame.setStart(wordResult.getTimeFrame().getStart());
                            wordFrame.setEnd(wordResult.getTimeFrame().getEnd());
                            wordFrame.setDuration(wordResult.getTimeFrame().length());
                            wordFrames.add(wordFrame);
                        }
                    }
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Could not calculation pronunciation" ,e);
            } finally {
                try {
                    close();
                } catch (Exception ex) {

                }
                try {
                    if (stream != null)
                        stream.close();
                } catch (IOException iox) {

                }
            }
        }
        return wordFrames;
    }

    public List<WordFrame> getWordFrames() {
        return wordFrames;
    }

    public static class WordFrame {
        private String word;
        private long start;
        private long end;
        private long duration;

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public long getStart() {
            return start;
        }

        public void setStart(long start) {
            this.start = start;
        }

        public long getEnd() {
            return end;
        }

        public void setEnd(long end) {
            this.end = end;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }
    }

    public static void main(String[] args) throws IOException {
        WordDetector wordDetector = new WordDetector(new File("/Users/luhonghai/Desktop/audio.wav"));
        List<WordFrame> wordFrames = wordDetector.analyze();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(wordFrames);
        System.out.println(json);
        FileUtils.writeStringToFile(new File("/Users/luhonghai/Desktop/word-frame.json"), json, "UTF-8");
    }
}

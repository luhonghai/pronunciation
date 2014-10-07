package com.cmg.vrc.sphinx;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.linguist.acoustic.Unit;
import edu.cmu.sphinx.linguist.dictionary.Pronunciation;
import edu.cmu.sphinx.result.WordResult;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by luhonghai on 10/7/14.
 */
public class PhonemesDetector {
    private static final String[] NOISE_LIST = new String[]{
            "SIL",
            "++AH++", "+AH+",
            "++BEEP++", "+TONE+",
            "++DOOR_SLAM++", "+SLAM+",
            "++BREATH++", "+BREATH+",
            "++GRUNT++", "+GRUNT+",
            "++LIP_SMACK++", "+SMACK+",
            "++PHONE_RING++", "+RING+",
            "++CLICK++", "+CLICK+",
            "++NOISE++", "+NOISE+"
    };

    public static class Result {

        private String phonemes;
        private String hypothesis;

        public String getPhonemes() {
            return phonemes;
        }

        public void setPhonemes(String phonemes) {
            this.phonemes = phonemes;
        }

        public String getHypothesis() {
            return hypothesis;
        }

        public void setHypothesis(String hypothesis) {
            this.hypothesis = hypothesis;
        }
    }

    private static final Logger logger = Logger.getLogger(PhonemesDetector.class.getName());
    private final File target;

    private StreamSpeechRecognizer recognizer;
    private Configuration conf;

    public PhonemesDetector(File target) {
        this.target = target;
    }

    public Result analyze() throws IOException {
        conf = new Configuration();
        conf.setAcousticModelPath(com.cmg.vrc.properties.Configuration.getValue(com.cmg.vrc.properties.Configuration.ACOUSTIC_MODEL_PATH));
        conf.setDictionaryPath(com.cmg.vrc.properties.Configuration.getValue(com.cmg.vrc.properties.Configuration.DICTIONARY_PATH));
        conf.setLanguageModelPath(com.cmg.vrc.properties.Configuration.getValue(com.cmg.vrc.properties.Configuration.LANGUAGE_MODEL_PATH));
        recognizer =
                new StreamSpeechRecognizer(conf);

        InputStream stream = null;

        if (target.exists() && !target.isDirectory()) {
            logger.info("Start analyze WAV record: " + target);
            try {
                stream = new FileInputStream(target);
                recognizer.startRecognition(stream);
                SpeechResult result;
                String phonemes = "";
                String hypothesis = "";
                while ((result = recognizer.getResult()) != null) {
                    hypothesis += result.getHypothesis().trim() + " ";


                    boolean flag = false;
                    boolean s;
                    for (WordResult r : result.getWords()) {
                        // Only get phonemes of first word
                        Pronunciation pron = r.getPronunciation();
                        Unit[] units = pron.getUnits();
                        for (Unit u : units) {
                            final String p = u.getName();
                            s = false;
                            for (String noise : NOISE_LIST) {
                                if (p.equalsIgnoreCase(noise)) {
                                    s = true;
                                    break;
                                }
                            }
                            if (!s) {
                                phonemes += (u.getName() + " ");
                            }
                        }
                    }
                }
                phonemes = phonemes.trim();
                hypothesis = hypothesis.trim();
                logger.info("Hypothesis: " + hypothesis);
                logger.info("Phonemes: " + phonemes);
                recognizer.stopRecognition();
                Result r = new Result();
                r.setHypothesis(hypothesis);
                r.setPhonemes(phonemes);
                return r;
            } finally {
                try {
                    stream.close();
                } catch (IOException iox) {
                    iox.printStackTrace();
                }
            }
        }
        return null;
    }
}

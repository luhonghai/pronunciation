package com.cmg.vrc.sphinx;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.CustomSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.linguist.acoustic.Unit;
import edu.cmu.sphinx.linguist.dictionary.Pronunciation;
import edu.cmu.sphinx.result.WordResult;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
            "++NOISE++", "+NOISE+",
            "+COUGH+", "+SMACK+",
            "+UH+", "+UM+"
    };

    public static class Result {

        private String phonemes;
        private String hypothesis;
        private List<String> NBests;

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

        public List<String> getNBests() {
            return NBests;
        }

        public void setNBests(List<String> nBests) {
            this.NBests = nBests;
        }
    }

    private static final Logger logger = Logger.getLogger(PhonemesDetector.class.getName());

    private final File target;

    private static CustomSpeechRecognizer recognizer;

    public static void init() {
        if (recognizer == null) {
            Configuration conf = new Configuration();
            conf.setAcousticModelPath(com.cmg.vrc.properties.Configuration.getValue(com.cmg.vrc.properties.Configuration.ACOUSTIC_MODEL_PATH));
            conf.setDictionaryPath(com.cmg.vrc.properties.Configuration.getValue(com.cmg.vrc.properties.Configuration.DICTIONARY_PATH));
            conf.setLanguageModelPath(com.cmg.vrc.properties.Configuration.getValue(com.cmg.vrc.properties.Configuration.LANGUAGE_MODEL_PATH));
            try {
                recognizer =
                        new CustomSpeechRecognizer(conf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close() {
        if (recognizer == null) {
            recognizer.stopRecognition();
        }
    }

    public PhonemesDetector(File target) {
        this.target = target;
    }

    public Result analyze() throws IOException {
        init();
        synchronized (recognizer) {
            InputStream stream = null;
            if (target.exists() && !target.isDirectory()) {
                logger.info("Start analyze WAV record: " + target);
                try {
                    stream = new FileInputStream(target);
                    recognizer.startRecognition(stream);
                    SpeechResult result;
                    String phonemes = "";
                    String hypothesis = "";
                    List<String> NBests = new ArrayList<String>();
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
                        Collection<String> tmpNBest = result.getNbest(4);
                        if (tmpNBest != null && tmpNBest.size() >0) {
                            NBests.addAll(tmpNBest);
                        }
                    }
                    phonemes = phonemes.trim();
                    hypothesis = hypothesis.trim();
                    logger.info("Hypothesis: " + hypothesis);
                    logger.info("Phonemes: " + phonemes);
                    //recognizer.stopRecognition();
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
}

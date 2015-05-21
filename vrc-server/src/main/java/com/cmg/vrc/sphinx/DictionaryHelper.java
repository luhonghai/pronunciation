package com.cmg.vrc.sphinx;

import com.cmg.vrc.util.AWSHelper;
import com.cmg.vrc.util.FileHelper;
import edu.cmu.sphinx.linguist.g2p.G2PConverter;
import edu.cmu.sphinx.linguist.g2p.Path;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by luhonghai on 5/21/15.
 */
public class DictionaryHelper {

    private static final Logger logger = Logger.getLogger(DictionaryHelper.class.getName());

    enum Type {
        G2P_CONVERTER,
        BEEP
    }

    public static final Map<String, String> BEEP_TO_CMU_PHONEMES;


    static {
        BEEP_TO_CMU_PHONEMES = new HashMap<String, String>();
        BEEP_TO_CMU_PHONEMES.put("OH", "AA");
        BEEP_TO_CMU_PHONEMES.put("EA", "EH");
        BEEP_TO_CMU_PHONEMES.put("IA", "IY");
        BEEP_TO_CMU_PHONEMES.put("AX", "AH");
        BEEP_TO_CMU_PHONEMES.put("UA", "UH");

    }

    private static HashMap<String, List<String>> BEEP_CACHE;

    private static final Object lock = new Object();

    private final Type type;

    public DictionaryHelper(Type type) {
        this.type = type;
    }

    public List<String> getCorrectPhonemes(String word) throws Exception {
        try {
            switch (type) {
                case G2P_CONVERTER:
                    List<String> correctPhonemes = new ArrayList<>();
                    G2PConverter converter = new G2PConverter(com.cmg.vrc.properties.Configuration.getValue(com.cmg.vrc.properties.Configuration.MODEL_FST_SER));
                    ArrayList<Path> list = converter.phoneticize(word, 1);
                    for (Path p : list) {
                        for (String _p : p.getPath()) {
                            correctPhonemes.add(_p);
                        }
                    }
                    return correctPhonemes;
                case BEEP:
                default:
                    if (BEEP_CACHE == null || BEEP_CACHE.size() == 0) {
                        synchronized (lock) {
                            logger.info("Start fetch BEEP dictionary");
                            File tmp = new File(FileHelper.getTmpSphinx4DataDir(), "sphinx-dict-beep-1.0.tmp");
                            if (!tmp.exists()) {
                                logger.info("Fetch BEEP dictionary from AWS S3");
                                AWSHelper awsHelper = new AWSHelper();
                                awsHelper.download("sphinx-data/dict/beep-1.0", tmp);
                            }
                            if (!tmp.exists()) {
                                tmp = new File("/Volumes/DATA/OSX/luhonghai/Desktop/beep/beep-1.0");
                            }
                            if (tmp.exists()) {
                                logger.info("Start analyze BEEP dictionary");
                                BEEP_CACHE = new HashMap<String, List<String>>();
                                BufferedReader br = null;
                                try {
                                    br = new BufferedReader(new FileReader(tmp));
                                    String line;
                                    while ((line = br.readLine()) != null) {
                                        if (!line.startsWith("#")) {
                                            while (line.contains("\t")) {
                                                line = line.replace("\t", " ");
                                            }
                                            line = line.toUpperCase();
                                            String[] lineData = line.split(" ");
                                            if (lineData.length >= 2) {
                                                List<String> phonemes = new ArrayList<String>();
                                                for (int i = 1; i < lineData.length; i++) {
                                                    String p =lineData[i].trim();
                                                    if (p.length() > 0)
                                                        phonemes.add(p);
                                                }
                                                BEEP_CACHE.put(lineData[0].trim(), phonemes);
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    try {
                                        if (br != null)
                                            br.close();
                                    } catch (Exception e) {

                                    }
                                }
                            } else {
                                logger.info("BEEP dictionary cache not found!");
                            }
                        }
                        logger.info("Found " + BEEP_CACHE.size() + " words from BEEP dictionary");
                    }
                    if (BEEP_CACHE != null && BEEP_CACHE.size() > 0 && BEEP_CACHE.containsKey(word.toUpperCase())) {
                        return BEEP_CACHE.get(word.toUpperCase());
                    }
                    break;
            }
            return null;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Could not generate phonemes from word " + word, ex);
            throw ex;
        }
    }

    public static void main(String[] args) throws Exception {
        DictionaryHelper helper = new DictionaryHelper(Type.BEEP);
        List<String> phonemes = helper.getCorrectPhonemes("bottom");
        if (phonemes != null && phonemes.size() > 0) {
            System.out.println("Found phonemes:");
            for (String s : phonemes) {
                System.out.println(s);
            }
        } else {
            System.out.println("No phonemes found");
        }

        phonemes = helper.getCorrectPhonemes("necessarily");
        if (phonemes != null && phonemes.size() > 0) {
            System.out.println("Found phonemes:");
            for (String s : phonemes) {
                System.out.println(s);
            }
        } else {
            System.out.println("No phonemes found");
        }
    }
}

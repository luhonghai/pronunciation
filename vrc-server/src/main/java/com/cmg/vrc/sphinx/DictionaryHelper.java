package com.cmg.vrc.sphinx;

import com.cmg.vrc.util.AWSHelper;
import com.cmg.vrc.util.FileHelper;
import com.cmg.vrc.util.StringUtil;
import edu.cmu.sphinx.linguist.g2p.G2PConverter;
import edu.cmu.sphinx.linguist.g2p.Path;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by luhonghai on 5/21/15.
 */
public class DictionaryHelper {

    private static final Logger logger = Logger.getLogger(DictionaryHelper.class.getName());

    public enum Type {
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

    private static final String DEFAULT_BEEP_S3_PATH = "sphinx-data/dict/beep-1.0";

    private static Map<String, Map<String, List<String>>> BEEP_CACHE = new ConcurrentHashMap<String, Map<String, List<String>>>();

    private static final Map<String, String> UK_VS_US_SPELLING = new HashMap<String, String>();

    private static final Map<String, String> US_VS_UK_SPELLING = new HashMap<String, String>();

    private static final Object lock = new Object();

    private final Type type;

    private String s3Path = DEFAULT_BEEP_S3_PATH;

    public DictionaryHelper(Type type) {
        this.type = type;
    }

    public DictionaryHelper(Type type, String s3Path) {
        this(type);
        this.s3Path = s3Path;
    }

    public String getUKWord(String usWord) {
        initSpellingDictionary();
        if (US_VS_UK_SPELLING.containsKey(usWord)) {
            return US_VS_UK_SPELLING.get(usWord);
        } else {
            return "";
        }
    }

    public String getUSWord(String ukWord) {
        initSpellingDictionary();
        if (UK_VS_US_SPELLING.containsKey(ukWord)) {
            return UK_VS_US_SPELLING.get(ukWord);
        } else {
            return "";
        }
    }

    public int getBeepSize() {
        checkBEEP();
        if (BEEP_CACHE.containsKey(s3Path))
            return BEEP_CACHE.size();
        return 0;
    }

    private static void initSpellingDictionary() {
        if (UK_VS_US_SPELLING.isEmpty() || US_VS_UK_SPELLING.isEmpty()) {
            synchronized (lock) {
                try {
                    List<String> rows = IOUtils.readLines(DictionaryHelper.class.getClassLoader().getResourceAsStream("amt/uk_vs_us.spelling"));
                    if (rows != null && rows.size() > 0) {
                        for (String row : rows) {
                            if (row.contains("|")) {
                                String[] raw = row.split("\\|");
                                if (raw.length == 2) {
                                    String uk = raw[0].trim().toUpperCase();
                                    String us = raw[1].trim().toUpperCase();
                                    UK_VS_US_SPELLING.put(uk,us);
                                    US_VS_UK_SPELLING.put(us, uk);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Could not load spelling dictionary",e);
                }
            }
        }
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
                    checkBEEP();
                    if (BEEP_CACHE.containsKey(s3Path)) {
                        return BEEP_CACHE.get(s3Path).get(word.toUpperCase());
                    }
                    break;
            }
            return null;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Could not generate phonemes from word " + word, ex);
            throw ex;
        }
    }

    private void checkBEEP() {
        if (!BEEP_CACHE.containsKey(s3Path)) {
            synchronized (lock) {
                logger.info("Start fetch BEEP dictionary for s3 path " + s3Path);
                File tmp = new File(FileHelper.getTmpSphinx4DataDir(), StringUtil.md5(s3Path) + ".dic");
                if (!tmp.exists()) {
                    logger.info("Fetch BEEP dictionary from AWS S3");
                    AWSHelper awsHelper = new AWSHelper();
                    awsHelper.download(s3Path, tmp);
                }
                if (tmp.exists()) {
                    logger.info("Start analyze BEEP dictionary");
                    Map<String, List<String>> cache = new HashMap<String, List<String>>();
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
                                    cache.put(lineData[0].trim(), phonemes);
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
                    BEEP_CACHE.put(s3Path, cache);
                    logger.info("Found " + BEEP_CACHE.get(s3Path).size() + " words from BEEP dictionary");
                } else {
                    logger.info("BEEP dictionary cache not found for path " + s3Path);
                }
            }

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

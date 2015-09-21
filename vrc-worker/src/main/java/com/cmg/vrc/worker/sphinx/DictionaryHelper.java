package com.cmg.vrc.worker.sphinx;

import com.cmg.vrc.worker.AnalyzingRequest;
import com.cmg.vrc.worker.cache.DictionaryCache;
import com.cmg.vrc.worker.common.Constant;
import com.cmg.vrc.worker.util.AWSHelper;
import com.cmg.vrc.worker.util.FileHelper;
import com.cmg.vrc.worker.util.Hash;
import edu.cmu.sphinx.linguist.g2p.G2PConverter;
import edu.cmu.sphinx.linguist.g2p.Path;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.BinaryConnectionFactory;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.MemcachedClientIF;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
        @Deprecated
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

    private static final ConcurrentHashMap<String, DictionaryCache> CACHE = new ConcurrentHashMap<String, DictionaryCache>();

    private static final Object lock = new Object();

    private final Type type;

    private final MemcachedClient memcachedClient;

    private final AWSHelper awsHelper;

    private final AnalyzingRequest.S3File dictionaryFile;

    public DictionaryHelper(Type type,
                            MemcachedClient memcachedClient,
                            AnalyzingRequest.S3File dictionaryFile,
                            AWSHelper awsHelper) {
        this.type = type;
        this.memcachedClient = memcachedClient;
        this.dictionaryFile = dictionaryFile;
        this.awsHelper = awsHelper;
    }

    public int getBeepSize() {
        checkBEEP();
        if (CACHE == null || !CACHE.containsKey(dictionaryFile.getKey())) return 0;
        return CACHE.get(dictionaryFile.getKey()).cache.size();
    }

    public List<String> getCorrectPhonemes(String word) throws Exception {
        try {
            switch (type) {
                case G2P_CONVERTER:
                    @Deprecated
                    List<String> correctPhonemes = new ArrayList<>();
                    G2PConverter converter = new G2PConverter("");
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
                    if (CACHE.containsKey(dictionaryFile.getKey())) {
                        final DictionaryCache cache = CACHE.get(dictionaryFile.getKey());
                        if (cache.cache.containsKey(word.toLowerCase())) {
                            return cache.cache.get(word.toLowerCase());
                        }
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
        synchronized (lock) {
            if (!CACHE.containsKey(dictionaryFile.getKey())) {
                DictionaryCache dictionaryCache = null;
//                try {
//                    dictionaryCache = (DictionaryCache) memcachedClient.get(Constant.CACHE_PREFIX_DICTIONARY
//                            + Hash.md5(dictionaryFile.getKey()));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                //if (dictionaryCache == null) {
                logger.info("Start fetch dictionary from " + dictionaryFile.getKey() + " to " + dictionaryFile.getLocal());
                File tmp = new File(FileHelper.getTmpDir(), dictionaryFile.getLocal());
                if (!tmp.exists()) {
                    logger.info("Fetch dictionary from AWS S3. Key: " + dictionaryFile.getKey());
                    awsHelper.download(dictionaryFile.getKey(), tmp);
                }
                if (tmp.exists()) {
                    dictionaryCache = new DictionaryCache();
                    logger.info("Start analyze dictionary");
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
                                        String p = lineData[i].trim();
                                        if (p.length() > 0)
                                            phonemes.add(p);
                                    }
                                    String word = lineData[0].trim().toLowerCase();
                                    if (!dictionaryCache.cache.containsKey(word)) {
                                        dictionaryCache.cache.put(word, phonemes);
                                    }
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

                    logger.info("Found " + dictionaryCache.cache.size() + " words from dictionary");
//                        memcachedClient.add(Constant.CACHE_PREFIX_DICTIONARY
//                                + Hash.md5(dictionaryFile.getKey()),
//                                MemcachedClientIF.MAX_KEY_LENGTH,
//                                dictionaryCache);
                } else {
                    logger.info("BEEP dictionary cache not found!");
                }
                // }
                if (dictionaryCache != null) {
                    logger.info("Add " + dictionaryCache.cache.size() + " words to cache");
                    CACHE.put(dictionaryFile.getKey(), dictionaryCache);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        DictionaryHelper helper = new DictionaryHelper(Type.BEEP,
                new MemcachedClient(new BinaryConnectionFactory(), AddrUtil.getAddresses("vrc-worker-cache.qvhoby.cfg.apse1.cache.amazonaws.com:11211")),
                new AnalyzingRequest.S3File("dictionary-v1.dict", "sphinx-data/dict/beep-1.0"),
                new AWSHelper("ap-southeast-1", "com-accenteasy-bbc-accent-dev")
        );
        List<String> phonemes = helper.getCorrectPhonemes("repaying");
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

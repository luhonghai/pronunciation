package com.cmg.vrc.worker.sphinx;

import com.cmg.vrc.worker.AnalyzingRequest;
import com.cmg.vrc.worker.common.Constant;
import com.cmg.vrc.worker.memcache.MemcacheFactory;
import com.cmg.vrc.worker.util.AWSHelper;
import com.cmg.vrc.worker.util.FileHelper;
import com.cmg.vrc.worker.util.UUIDGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.decoder.search.Token;
import edu.cmu.sphinx.linguist.HMMSearchState;
import edu.cmu.sphinx.linguist.SearchState;
import net.spy.memcached.MemcachedClient;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by luhonghai on 10/7/14.
 */

public class PhonemesDetector {

    private static final Logger logger = Logger.getLogger(PhonemesDetector.class.getName());

    private StreamSpeechRecognizer recognizer;

    private List<String> correctPhonemes;

    private static Map<String, List<String>> neighbourPhones;

    private final File sphinx4DataTmpDir;

    private final AWSHelper awsHelper;

    private final AnalyzingRequest analyzingRequest;

    private MemcachedClient memcachedClient;

    private static final Object lock = new Object();

    public PhonemesDetector(AnalyzingRequest analyzingRequest) {
        this.sphinx4DataTmpDir = FileHelper.getTmpDir();
        this.awsHelper = new AWSHelper(analyzingRequest.getRegionName(), analyzingRequest.getBucketName());
        this.analyzingRequest = analyzingRequest;
        this.memcachedClient = MemcacheFactory.getMemcachedClient(analyzingRequest.getElasticCacheAddress());
    }


    public void init() throws IOException {
        generatePhonemes();
        initNeighbourPhonemes();
        if (recognizer == null) {
            Configuration conf = new Configuration();
            File tmpAcousticModelDir = new File(sphinx4DataTmpDir, analyzingRequest.getAcousticModel().getLocal());
            synchronized (lock) {
                if (!tmpAcousticModelDir.exists()) {
                    awsHelper.downloadAndUnzip(analyzingRequest.getAcousticModel().getKey(), sphinx4DataTmpDir);
                }
            }
            if (tmpAcousticModelDir.exists()) {
                conf.setAcousticModelPath(tmpAcousticModelDir.toURI().toString());
            }
            File tmpPhonemesDictFile = new File(sphinx4DataTmpDir, analyzingRequest.getPhoneDictionary().getLocal());
            synchronized (lock) {
                if (!tmpPhonemesDictFile.exists()) {
                    awsHelper.download(analyzingRequest.getPhoneDictionary().getKey(), tmpPhonemesDictFile);
                }
            }
            if (tmpPhonemesDictFile.exists()) {
                conf.setDictionaryPath(tmpPhonemesDictFile.toURI().toString());
            }
            conf.setGrammarPath(getGrammarPath());
            conf.setUseGrammar(true);
            conf.setGrammarName(analyzingRequest.getWord());
            recognizer =
                        new StreamSpeechRecognizer(conf);
        }
    }

    private String getGrammarPath() throws IOException {
        File grammarDir = new File(sphinx4DataTmpDir, "grammar");
        if (!grammarDir.exists()) {
            grammarDir.mkdirs();
        }
        StringBuffer sb = new StringBuffer();
        sb.append("#JSGF V1.0;\n\n").append("grammar phonelist;\n\n").append("public <phonelist> = (SIL ");
        for (String p : correctPhonemes) {
            String phoneme;
            if (DictionaryHelper.BEEP_TO_CMU_PHONEMES.containsKey(p.toUpperCase())) {
                phoneme = DictionaryHelper.BEEP_TO_CMU_PHONEMES.get(p.toUpperCase());
            } else {
                phoneme = p;
            }
            List<String> neighbours = neighbourPhones.get(phoneme.toLowerCase());
            sb.append("(");
            if (neighbours!=null && neighbours.size() > 0) {
                neighbours = new ArrayList<String>(neighbours);
                neighbours.add("SIL");
                neighbours.add(phoneme);
                sb.append(StringUtils.join(neighbours, "|"));
            } else {
                sb.append(phoneme);
            }
            sb.append(") ");
        }
        sb.append("SIL);");

        File grammarFile = new File(grammarDir,  analyzingRequest.getWord() + ".gram");
        if (!grammarFile.exists() || grammarFile.isDirectory()) {
            try {
                FileUtils.write(grammarFile, sb.toString(), "UTF-8");
            } catch (IOException e) {
                throw e;
            }
        }
        return grammarDir.toURI().toString();
    }

    private void initNeighbourPhonemes() {
        if (neighbourPhones == null || neighbourPhones.size() == 0) {
            neighbourPhones = new HashMap<String, List<String>>();
            synchronized (neighbourPhones) {
                File tmpFile = new File(FileHelper.getTmpDir(Constant.FOLDER_EXTRA), analyzingRequest.getNeighbourPhones().getLocal());
                if (!tmpFile.exists()) {
                    awsHelper.download(analyzingRequest.getNeighbourPhones().getKey(), tmpFile);
                }
                if (!tmpFile.exists()) return;
                try {
                    List<String> rows = FileUtils.readLines(tmpFile, "UTF-8");
                    for (String line : rows) {
                        String[] raw = line.split("neighbors:");
                        String phones = raw[0].trim();
                        System.out.println("=======");
                        System.out.println("Found phones " + phones);
                        List<String> neighbours = null;
                        if (neighbourPhones.containsKey(phones.toLowerCase())) {
                            neighbours = neighbourPhones.get(phones.toLowerCase());
                            neighbourPhones.remove(phones.toLowerCase());
                        }
                        if (neighbours == null)
                            neighbours = new ArrayList<String>();

                        String[] rawNeighbours = raw[1].trim().split("\\|");
                        if (rawNeighbours.length > 0) {
                            for (String nb : rawNeighbours) {
                                nb = nb.trim();
                                if (!neighbours.contains(nb.toLowerCase()) && !nb.equalsIgnoreCase(phones) && !nb.equalsIgnoreCase("sil")) {
                                    System.out.println("Found neighbour " + nb);
                                    neighbours.add(nb);
                                }
                            }
                        }
                        neighbourPhones.put(phones.toLowerCase(), neighbours);
                    }
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, "Could not get neighbour phonemes list", ex);
                }
            }
        }
    }

    private void generatePhonemes() {
        try {
            DictionaryHelper helper = new DictionaryHelper(DictionaryHelper.Type.BEEP,
                    memcachedClient,
                    analyzingRequest.getDictionary(),
                    awsHelper);
            correctPhonemes = helper.getCorrectPhonemes(analyzingRequest.getWord());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Could not generate phonemes from word " + analyzingRequest.getWord(), ex);
        }
    }

    public void close() {
        if (recognizer != null) {
            recognizer.stopRecognition();
        }
    }


    public SphinxResult analyze() throws IOException {
        init();
        InputStream stream = null;
        SphinxResult r = new SphinxResult();
        r.setCorrectPhonemes(correctPhonemes);
            logger.info("Start analyze WAV record: " + analyzingRequest.getKeyName());
            try {
                stream = awsHelper.openInputStream(analyzingRequest.getKeyName());
                recognizer.startRecognition(stream);
                SpeechResult result;
                List<SphinxResult.Phoneme> bestTokenPhonemes = new ArrayList<SphinxResult.Phoneme>();
                if ((result = recognizer.getResult()) != null) {
                    edu.cmu.sphinx.result.Result rs = result.getResult();
                    List<Token> bestTokens = rs.getBestTokens();
                    if (bestTokens != null && bestTokens.size() > 0) {
                        SphinxResult.Phoneme phoneme = null;
                        for (Token token : bestTokens) {
                            SearchState searchState = token.getSearchState();
                            if (searchState != null && searchState instanceof HMMSearchState) {
                                HMMSearchState hmmSearchState = (HMMSearchState) searchState;
                                if (hmmSearchState.isEmitting()) {
                                    String baseUnit = hmmSearchState.getHMMState().getHMM().getBaseUnit().getName();
                                    if (baseUnit != null && baseUnit.length() > 0 && !baseUnit.toLowerCase().contains("sil")) {
                                        if (phoneme == null) {
                                            phoneme = new SphinxResult.Phoneme();
                                            phoneme.setName(baseUnit);
                                            phoneme.setCount(1);
                                        } else if (phoneme.getName().equalsIgnoreCase(baseUnit)) {
                                            phoneme.setCount(phoneme.getCount() + 1);
                                        } else {
                                            bestTokenPhonemes.add(phoneme);
                                            phoneme = new SphinxResult.Phoneme();
                                            phoneme.setName(baseUnit);
                                            phoneme.setCount(1);
                                        }
                                    }
                                }
                            }
                        }
                        bestTokenPhonemes.add(phoneme);
                    }
                }
                //recognizer.stopRecognition();

                r.setBestPhonemes(bestTokenPhonemes);
                calculateScore(r);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Could not calculate pronunciation" ,e);
            } finally {
                try {
                    close();
                } catch (Exception ex) {

                }
                try {
                    stream.close();
                } catch (IOException iox) {

                }
            }
        return r;
    }

    private int getValidatePhonemeType(String selectedPhoneme, String targetPhoneme) {
        try {
            if (selectedPhoneme.equalsIgnoreCase(targetPhoneme)) {
                return SphinxResult.PhonemeScoreUnit.MATCHED;
            } else if (DictionaryHelper.BEEP_TO_CMU_PHONEMES.containsKey(selectedPhoneme.toUpperCase())
                    && DictionaryHelper.BEEP_TO_CMU_PHONEMES.get(selectedPhoneme.toUpperCase()).equalsIgnoreCase(targetPhoneme)) {
                return SphinxResult.PhonemeScoreUnit.BEEP_PHONEME;
            } else {
                String testPhoneme;
                if (DictionaryHelper.BEEP_TO_CMU_PHONEMES.containsKey(selectedPhoneme.toUpperCase())) {
                    testPhoneme= DictionaryHelper.BEEP_TO_CMU_PHONEMES.get(selectedPhoneme.toUpperCase());
                } else {
                    testPhoneme = selectedPhoneme;
                }
                if (neighbourPhones.containsKey(testPhoneme.toLowerCase())) {
                    List<String> neighbourPhonemes = neighbourPhones.get(testPhoneme.toLowerCase());
                    for (String phoneme : neighbourPhonemes) {
                        if (phoneme.equalsIgnoreCase(targetPhoneme)) {
                            return SphinxResult.PhonemeScoreUnit.NEIGHBOR;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SphinxResult.PhonemeScoreUnit.NOT_MATCH;
    }

    private SphinxResult.PhonemeScoreUnit calculatePhonemeUnit(final List<SphinxResult.Phoneme> bestPhonemes,
                                                                int index, int cIndex,
                                                                String selectedPhoneme,
                                                                String nextPhoneme) {
        if (bestPhonemes.size() == 0) return null;
        try {
            SphinxResult.Phoneme phoneme = bestPhonemes.get(0);
            SphinxResult.PhonemeScoreUnit scoreUnit = createPhonemeScoreUnit(selectedPhoneme, phoneme, index);
            int nextPhonemeType = (nextPhoneme == null || nextPhoneme.length() == 0) ?
                    SphinxResult.PhonemeScoreUnit.NOT_MATCH :
                    getValidatePhonemeType(nextPhoneme, phoneme.getName());

            boolean isMatchedWithNextPhoneme = false;
            for (int i = cIndex + 1; i < correctPhonemes.size(); i++) {
                int type = getValidatePhonemeType(correctPhonemes.get(i), phoneme.getName());
                if (type != SphinxResult.PhonemeScoreUnit.NOT_MATCH && i - cIndex <= 3) {
                    isMatchedWithNextPhoneme = true;
                    break;
                }
            }
            if ((scoreUnit != null && scoreUnit.getType() == SphinxResult.PhonemeScoreUnit.MATCHED)
                    || (scoreUnit != null && scoreUnit.getType() == SphinxResult.PhonemeScoreUnit.NEIGHBOR)
                    || (scoreUnit != null && scoreUnit.getType() == SphinxResult.PhonemeScoreUnit.BEEP_PHONEME)
                    || !isMatchedWithNextPhoneme) {
                bestPhonemes.remove(0);
                return scoreUnit;
            } else {
                // Look like the current best score is matched with next phoneme
                // return null will move next phoneme
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private SphinxResult.PhonemeScoreUnit createPhonemeScoreUnit(String selectedPhoneme,
                                                                 SphinxResult.Phoneme phoneme,
                                                                 int index) {
        try {
            SphinxResult.PhonemeScoreUnit scoreUnit = new SphinxResult.PhonemeScoreUnit();
            scoreUnit.setIndex(index);
            if (phoneme != null) {
                int selectedPhonemeType = getValidatePhonemeType(selectedPhoneme, phoneme.getName());
                scoreUnit.setCount(phoneme.getCount());
                scoreUnit.setName(phoneme.getName());
                scoreUnit.setType(selectedPhonemeType);
            } else {
                scoreUnit.setCount(0);
                scoreUnit.setName(selectedPhoneme);
                scoreUnit.setType(SphinxResult.PhonemeScoreUnit.NOT_MATCH);
            }
            return scoreUnit;
        } catch (Exception e) {

            return null;
        }
    }

    private void calculateScore(final SphinxResult result) {
        float finalScore = 0.0f;
        List<SphinxResult.PhonemeScore> phonemeScores = new ArrayList<SphinxResult.PhonemeScore>();
        List<SphinxResult.Phoneme> bestPhonemes = new ArrayList<SphinxResult.Phoneme>(result.getBestPhonemes());
        for (int i = 0; i < correctPhonemes.size(); i++) {
            float totalScore = 0.0f;
            SphinxResult.PhonemeScore phonemeScore = new SphinxResult.PhonemeScore();
            List<SphinxResult.PhonemeScoreUnit> scoreUnits = new ArrayList<SphinxResult.PhonemeScoreUnit>();
            String selectedPhoneme = correctPhonemes.get(i);
            String nextPhoneme = "";
            if (i < correctPhonemes.size() - 1) {
                nextPhoneme = correctPhonemes.get(i+1);
            }
            logger.info("--------------------------------------------");
            logger.info("Phoneme index " + i + ". Name: " + selectedPhoneme );
            logger.info("Next phoneme: " + nextPhoneme);
            logger.info("Best phonemes size: " + bestPhonemes.size());
            int count = 0;
            if (bestPhonemes.size() > 0) {
                SphinxResult.PhonemeScoreUnit scoreUnit = null;
                while ((scoreUnit = calculatePhonemeUnit(bestPhonemes, count++,i, selectedPhoneme, nextPhoneme)) != null) {
                    scoreUnits.add(scoreUnit);
                }

                // Add all left best phoneme to last selected phoneme if exist
                if ((i == correctPhonemes.size() - 1) && bestPhonemes.size() > 0) {
                    logger.info("Add all left best phoneme to the last selected phoneme");
                    for (int j = 0; j < bestPhonemes.size(); j++) {
                        scoreUnits.add(createPhonemeScoreUnit(selectedPhoneme, bestPhonemes.get(j), j + count + 1));
                    }
                }

            } else {
                // No phoneme left
                logger.info("No best phoneme left");
            }

            int tokenCount = 0;
            for (SphinxResult.PhonemeScoreUnit scoreUnit : scoreUnits) {
                tokenCount += scoreUnit.getCount();
                if (scoreUnit.getType() == SphinxResult.PhonemeScoreUnit.MATCHED) {
                    // 100% score
                    totalScore += scoreUnit.getCount();
                } else if (scoreUnit.getType() == SphinxResult.PhonemeScoreUnit.BEEP_PHONEME){
                    // 90% score
                    totalScore += (float) scoreUnit.getCount() * 0.9f;
                } else if (scoreUnit.getType() == SphinxResult.PhonemeScoreUnit.NEIGHBOR) {
                    // 50% score
                    totalScore += (float) scoreUnit.getCount() / 2;
                }
            }
            totalScore = tokenCount == 0 ? 0.0f : (totalScore / tokenCount) * 100;
            logger.info("Total score: " + totalScore);
            phonemeScore.setIndex(i);
            phonemeScore.setName(selectedPhoneme);
            phonemeScore.setTotalScore(totalScore);
            phonemeScore.setPhonemes(scoreUnits);
            phonemeScores.add(phonemeScore);
        }

        for (SphinxResult.PhonemeScore phonemeScore : phonemeScores) {
            finalScore += phonemeScore.getTotalScore();
        }

        finalScore = finalScore / phonemeScores.size();
        logger.info("===========================");
        logger.info("Final score: " + finalScore);
        result.setScore(finalScore);
        result.setPhonemeScores(phonemeScores);
    }

    public static void main(String[] args) {
        AnalyzingRequest request = new AnalyzingRequest();
        request.setId(UUIDGenerator.generateUUID());
        request.setBucketName("com-accenteasy-bbc-accent-dev");
        request.setElasticCacheAddress("vrc-worker-cache.qvhoby.cfg.apse1.cache.amazonaws.com:11211");
        request.setKeyName("voices/hai.lu@c-mg.com/able_cb35e4f3-90da-4b62-96ad-09076f66ff27_raw.wav");
        request.setRegionName("ap-southeast-1");
        request.setWord("able");
        request.setDictionary(new AnalyzingRequest.S3File("dictionary-v1.dict", "sphinx-data/dict/beep-1.0"));
        request.setNeighbourPhones(new AnalyzingRequest.S3File("cmubet_neighbour_phones.txt", "sphinx-data/extra/cmubet_neighbour_phones.txt"));
        request.setPhoneDictionary(new AnalyzingRequest.S3File("cmuphonemedict", "sphinx-data/dict/cmuphonemedict"));
        request.setAcousticModel(new AnalyzingRequest.S3File("wsj-en-us", "sphinx-data/wsj-en-us.zip"));
        System.out.println("=============================");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println("Request: " + gson.toJson(request));
        PhonemesDetector detector = new PhonemesDetector(request);
        try {
            System.out.println("=============================");
            System.out.println("Result: " + gson.toJson(detector.analyze()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

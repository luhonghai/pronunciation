package com.cmg.vrc.sphinx;

import be.tarsos.dsp.util.fft.FFT;
import com.cmg.vrc.util.AWSHelper;
import com.cmg.vrc.util.FileHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.decoder.search.Token;
import edu.cmu.sphinx.frontend.FloatData;
import edu.cmu.sphinx.linguist.HMMSearchState;
import edu.cmu.sphinx.linguist.SearchState;
import edu.cmu.sphinx.linguist.acoustic.HMMState;
import edu.cmu.sphinx.linguist.g2p.G2PConverter;
import edu.cmu.sphinx.linguist.g2p.Path;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by luhonghai on 10/7/14.
 */

public class PhonemesDetector {

    private static final Logger logger = Logger.getLogger(PhonemesDetector.class.getName());

    private final File target;

    private final String word;

    private StreamSpeechRecognizer recognizer;

    private List<String> correctPhonemes;

    private static Map<String, List<String>> neighbourPhones;

    private final File sphinx4DataTmpDir = FileHelper.getTmpSphinx4DataDir();

    private File grammarFile;

    private String grammarName;

    private boolean allowAdditionalData;

    private float sampleRate = 16000;

    public PhonemesDetector(File target, String word) {
        this.target = target;
        this.word = word;
    }


    public void init() {
        generatePhonemes();
        initNeighbourPhonemes();

        if (recognizer == null) {
            AWSHelper awsHelper = new AWSHelper();

            Configuration conf = new Configuration();

            File tmpAcousticModelDir = new File(sphinx4DataTmpDir, "wsj-en-us");
            if (!tmpAcousticModelDir.exists()) {
                awsHelper.downloadAndUnzip("sphinx-data/wsj-en-us.zip", sphinx4DataTmpDir);
            }
//            File tmpAcousticModelDir = new File(sphinx4DataTmpDir, "cmusphinx-en-us-5.2");
//            if (!tmpAcousticModelDir.exists()) {
//                awsHelper.downloadAndUnzip("sphinx-data/cmusphinx-en-us-5.2.zip", sphinx4DataTmpDir);
//            }

            if (tmpAcousticModelDir.exists()) {
                conf.setAcousticModelPath(tmpAcousticModelDir.toURI().toString());
            } else {
                conf.setAcousticModelPath(com.cmg.vrc.properties.Configuration.getValue(com.cmg.vrc.properties.Configuration.ACOUSTIC_MODEL_PATH));
            }
            File tmpPhonemesDictFile = new File(sphinx4DataTmpDir, "cmuphonemedict");
            if (!tmpPhonemesDictFile.exists()) {
                awsHelper.download("sphinx-data/dict/cmuphonemedict", tmpPhonemesDictFile);
            }
            if (tmpPhonemesDictFile.exists()) {
                conf.setDictionaryPath(tmpPhonemesDictFile.toURI().toString());
            } else {
                conf.setDictionaryPath(com.cmg.vrc.properties.Configuration.getValue(com.cmg.vrc.properties.Configuration.DICTIONARY_PATH));
            }
            //conf.setLanguageModelPath(com.cmg.vrc.properties.Configuration.getValue(com.cmg.vrc.properties.Configuration.LANGUAGE_MODEL_PATH));
            conf.setGrammarPath(getGrammarPath());
            conf.setUseGrammar(true);
            conf.setGrammarName(grammarName);
            try {
                recognizer =
                        new StreamSpeechRecognizer(conf);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Could not init recognizer", e);
            }
        }
    }

    private String getGrammarPath() {
        //String grammarPath = com.cmg.vrc.properties.Configuration.getValue(com.cmg.vrc.properties.Configuration.GRAMMAR_PATH);
        File grammarDir = new File(sphinx4DataTmpDir, "grammar");
        if (!grammarDir.exists()) {
            grammarDir.mkdirs();
        }

        StringBuffer sb = new StringBuffer();
        sb.append("#JSGF V1.0;\n\n").append("grammar phonelist;\n\n");
        //addGrammar("withNeighbour", sb, false, false, false);
        //sb.append("\n");
        //addGrammar("withNeighbourAndFirstLast",sb, false, false, false, true);
        addGrammar("withNeighbourAndFirstLast",sb, false, false, false, false);
        grammarName = UUID.randomUUID().toString();
            grammarFile = new File(grammarDir, grammarName+ ".gram");
        logger.info(" The name of the JSGF file " +  grammarFile);
        try {
            String grammarContent = sb.toString();
            logger.info("Use grammar: \n" + grammarContent);
            FileUtils.write(grammarFile, grammarContent, "UTF-8");
        } catch (IOException e) {
                logger.log(Level.SEVERE, "could not generate grammar file",e);
        }
        return grammarDir.toURI().toString();
    }

    private void addGrammar(String name, final StringBuffer sb, boolean addFirst, boolean addLast, boolean addExtra, boolean addExtraNeighbour) {
        sb.append("public <").append(name).append("> = SIL ");
        int count = 0;
        for (String p : correctPhonemes) {
            String phoneme;
            if (DictionaryHelper.BEEP_TO_CMU_PHONEMES.containsKey(p.toUpperCase())) {
                phoneme = DictionaryHelper.BEEP_TO_CMU_PHONEMES.get(p.toUpperCase());
            } else {
                phoneme = p;
            }
            count++;
            if (addFirst && DictionaryHelper.getPhonemeList().size() > 0 && count == 1) {
                sb.append("(").append(StringUtils.join(DictionaryHelper.getPhonemeList(), "|")).append("|SIL) ");
            } else if (addLast && DictionaryHelper.getPhonemeList().size() > 0 && count == correctPhonemes.size()) {
                sb.append("(").append(StringUtils.join(DictionaryHelper.getPhonemeList(), "|")).append("|SIL) ");
            } else if (count > 0 && count % 3 == 0 && count < correctPhonemes.size() - 2 && addExtra) {
                sb.append("(").append(StringUtils.join(DictionaryHelper.getPhonemeList(), "|")).append("|SIL) ");
            } else {
                List<String> neighbours = new ArrayList<>(neighbourPhones.get(phoneme.toUpperCase()));
                sb.append("(");
                if (neighbours.size() > 0) {
                    neighbours.add("SIL");
                    neighbours.add(phoneme);
                    if (addExtraNeighbour) {
                        for (String np : neighbourPhones.get(phoneme.toUpperCase())) {
                            List<String> listNp = neighbourPhones.get(np.toUpperCase());
                            if (listNp != null && listNp.size() > 0) {
                                for (String snp : listNp) {
                                    if (!neighbours.contains(snp)) {
                                        neighbours.add(snp);
                                    }
                                }
                            }
                        }
                    }
                    sb.append(StringUtils.join(neighbours, "|"));
                } else {
                    sb.append(phoneme);
                }
                sb.append(") ");
            }
        }
        sb.append("SIL;");
    }

    private void initNeighbourPhonemes() {
        if (neighbourPhones == null || neighbourPhones.size() == 0) {
            neighbourPhones = new HashMap<String, List<String>>();
            synchronized (neighbourPhones) {
                InputStream is = null;
                try {
                    is = this.getClass().getClassLoader().getResourceAsStream("cmubet_neighbour_phones.txt");
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    try {
                        String line;
                        while ((line = br.readLine()) != null) {
                            String[] raw = line.split("neighbors:");
                            String phones = raw[0].trim().toUpperCase();
                            if (!DictionaryHelper.BEEP_TO_CMU_PHONEMES.containsKey(phones)) {
                                System.out.println("=======");
                                System.out.println("Found phones " + phones);
                                List<String> neighbours = null;
                                if (neighbourPhones.containsKey(phones.toUpperCase())) {
                                    neighbours = neighbourPhones.get(phones.toUpperCase());
                                    neighbourPhones.remove(phones.toUpperCase());
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
                                neighbourPhones.put(phones.toUpperCase(), neighbours);
                            }
                        }
                    } finally {
                        IOUtils.closeQuietly(br);
                    }
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, "Could not get neighbour phonemes list", ex);
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException ex) {
                            logger.log(Level.SEVERE, "Could not close input stream", ex);
                        }
                    }
                }
            }
        }
    }

    private void generatePhonemes() {
        try {
            DictionaryHelper helper = new DictionaryHelper(DictionaryHelper.Type.DATABASE);
            correctPhonemes = helper.getCorrectPhonemes(word);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Could not generate phonemes from word " + word, ex);
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
        if (target.exists() && !target.isDirectory()) {
            logger.info("Start analyze WAV record: " + target);
            try {
                stream = new FileInputStream(target);
                recognizer.startRecognition(stream);
                SpeechResult result;
                List<SphinxResult.Phoneme> bestTokenPhonemes = new ArrayList<SphinxResult.Phoneme>();
                List<String> rawBestPhonemes = new ArrayList<>();
                while ((result = recognizer.getResult()) != null) {
                    edu.cmu.sphinx.result.Result rs = result.getResult();
                    List<Token> bestTokens = rs.getBestTokens();
                    if (bestTokens != null && bestTokens.size() > 0) {
                        SphinxResult.Phoneme phoneme = null;
                        for (Token token : bestTokens) {
                            SearchState searchState = token.getSearchState();
                            if (searchState != null && searchState instanceof HMMSearchState) {
                                HMMSearchState hmmSearchState = (HMMSearchState) searchState;
                                if (hmmSearchState.isEmitting()) {
                                    HMMState hmmState = hmmSearchState.getHMMState();
                                    String baseUnit = hmmState.getHMM().getBaseUnit().getName();
                                    if (baseUnit != null && baseUnit.length() > 0 && !baseUnit.toLowerCase().contains("sil")) {
                                        SphinxResult.PhonemeExtra phonemeExtra = new SphinxResult.PhonemeExtra();
                                        if (allowAdditionalData) {
                                            FloatData data = (FloatData) token.getData();
                                            int bufferSize = data.getValues().length;
                                            logger.info("buffer size: " + bufferSize);
                                            FFT fft = new FFT(bufferSize);
                                            float[] amplitudes = new float[bufferSize / 2];
                                            float[] audioFloatBuffer = data.getValues();
                                            float[] transformbuffer = new float[bufferSize * 2];
                                            float[] transformbuffer2 = new float[bufferSize * 2];

                                            float[] magnitude = new float[bufferSize / 2];
                                            float[] phases = new float[bufferSize / 2];

                                            System.arraycopy(audioFloatBuffer, 0, transformbuffer, 0, audioFloatBuffer.length);
                                            System.arraycopy(audioFloatBuffer, 0, transformbuffer2, 0, audioFloatBuffer.length);
                                            fft.forwardTransform(transformbuffer);
                                            fft.modulus(transformbuffer, amplitudes);

                                            FFT fft2 = new FFT(bufferSize);
                                            fft2.forwardTransform(transformbuffer2);
                                            fft2.powerAndPhaseFromFFT(transformbuffer2, magnitude, phases);
                                            //TODO: get frequency from fft http://stackoverflow.com/questions/7674877/how-to-get-frequency-from-fft-result
                                            Float maxAmp = Collections.max(Arrays.asList(ArrayUtils.toObject(amplitudes)));
                                            Float minAmp = Collections.min(Arrays.asList(ArrayUtils.toObject(amplitudes)));
                                            Float maxMagnitude = Collections.max(Arrays.asList(ArrayUtils.toObject(magnitude)));
                                            double freq = fft.binToHz(Math.round(maxMagnitude), sampleRate) / 1000;
                                            logger.info("Found phone " + baseUnit
                                                    + ". Time frame: " + data.getCollectTime() + "ms"
                                                    + ". Max amp: " + maxAmp
                                                    + ". Min amp: " + minAmp
                                                    + ". Max magnitude: " + maxMagnitude
                                                    + ". Frequency: " + freq);

                                            phonemeExtra.setMaxAmp(maxAmp);
                                            phonemeExtra.setCollectTime(data.getCollectTime());
                                            phonemeExtra.setMaxMagnitude(maxMagnitude);
                                            phonemeExtra.setFrequency(freq);
                                            rawBestPhonemes.add(baseUnit.toUpperCase());
                                        }
                                        if (phoneme == null) {
                                            phoneme = new SphinxResult.Phoneme();
                                            phoneme.setName(baseUnit);
                                            phoneme.setCount(1);
                                            if (allowAdditionalData) {
                                                phoneme.getExtras().add(phonemeExtra);
                                            }
                                        } else if (phoneme.getName().equalsIgnoreCase(baseUnit)) {
                                            phoneme.setCount(phoneme.getCount() + 1);
                                            if (allowAdditionalData) {
                                                phoneme.getExtras().add(phonemeExtra);
                                            }
                                        } else {
                                            bestTokenPhonemes.add(phoneme);
                                            phoneme = new SphinxResult.Phoneme();
                                            phoneme.setName(baseUnit);
                                            phoneme.setCount(1);
                                            if (allowAdditionalData) {
                                                phoneme.getExtras().add(phonemeExtra);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (phoneme!= null) {
                            bestTokenPhonemes.add(phoneme);
                        }
                    }
                }
                //recognizer.stopRecognition();
                if (allowAdditionalData) {
                    r.setRawBestPhonemes(rawBestPhonemes);
                }
                r.setBestPhonemes(bestTokenPhonemes);
                calculateScore(r);
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
                if (grammarFile != null && grammarFile.exists()) {
                    FileUtils.forceDelete(grammarFile);
                }
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
                if (neighbourPhones.containsKey(testPhoneme.toUpperCase())) {
                    List<String> neighbourPhonemes = neighbourPhones.get(testPhoneme.toUpperCase());
                    if (neighbourPhonemes != null && neighbourPhonemes.size() > 0) {
                        for (String phoneme : neighbourPhonemes) {
                            if (phoneme.equalsIgnoreCase(targetPhoneme)) {
                                return SphinxResult.PhonemeScoreUnit.NEIGHBOR;
                            }
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
            int suitablePhonemeCount = 2;
            boolean goodToAdd = true;
            SphinxResult.Phoneme phoneme = bestPhonemes.get(0);
            SphinxResult.PhonemeScoreUnit scoreUnit = createPhonemeScoreUnit(selectedPhoneme, phoneme, index);
            int scoreType = SphinxResult.PhonemeScoreUnit.NOT_MATCH;
            if (scoreUnit != null) scoreType = scoreUnit.getType();
            if (bestPhonemes.size() > 1) {
                for (int i = 1; i < bestPhonemes.size(); i++) {
                    String mPhoneme = bestPhonemes.get(i).getName();
                    int typeSP = getValidatePhonemeType(mPhoneme, selectedPhoneme);
                    int typeNP = getValidatePhonemeType(mPhoneme, nextPhoneme);
                    if (typeSP == SphinxResult.PhonemeScoreUnit.MATCHED
                            || typeSP == SphinxResult.PhonemeScoreUnit.BEEP_PHONEME) {
                        break;
                    }
                    if (typeNP == SphinxResult.PhonemeScoreUnit.MATCHED
                            || typeNP == SphinxResult.PhonemeScoreUnit.BEEP_PHONEME
                            || (typeNP == SphinxResult.PhonemeScoreUnit.NEIGHBOR && bestPhonemes.get(i).getCount() > suitablePhonemeCount)) {
                        // The next best phonemes is suitable for the next phoneme. Skip the adding now
                        goodToAdd = false;
                        break;
                    }
                    if (typeSP != SphinxResult.PhonemeScoreUnit.NOT_MATCH) {
                        // The next best phonemes is suitable for the selected phoneme. Good to add
                        break;
                    }
                }
            }
            for (int i = cIndex + 1; i < correctPhonemes.size(); i++) {
                int type = getValidatePhonemeType(correctPhonemes.get(i), phoneme.getName());
                if ((type == SphinxResult.PhonemeScoreUnit.MATCHED
                        || type == SphinxResult.PhonemeScoreUnit.BEEP_PHONEME)
                        || (type == SphinxResult.PhonemeScoreUnit.NEIGHBOR && phoneme.getCount() > suitablePhonemeCount)
                        && i - cIndex < 3) {
                    goodToAdd = false;
                    break;
                }
            }
            if (scoreType != SphinxResult.PhonemeScoreUnit.NOT_MATCH
                    || goodToAdd) {
                bestPhonemes.remove(0);
                return scoreUnit;
            } else {
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
                scoreUnit.setExtras(phoneme.getExtras());
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

    public Map<String, List<String>> getNeighbourPhones() {
        return new HashMap<>(neighbourPhones);
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
                SphinxResult.PhonemeScoreUnit scoreUnit;
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
                    totalScore += scoreUnit.getCount() * 0.9f;
                } else if (scoreUnit.getType() == SphinxResult.PhonemeScoreUnit.NEIGHBOR) {
                    // 50% score
                    totalScore += scoreUnit.getCount() * 0.5f;
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
        String fVariable = "/Volumes/DATA/Development/voice-sample/dominic_1_1_2 2/variable_0ec2d743-2d6b-4723-9762-fffb8f41df06_raw.wav";
        String fSeashell = "/Volumes/DATA/Development/voice-sample/dominic_1_1_2 2/seashell_26207c0c-4810-4ce4-9feb-ffcc37ad92b7_raw.wav";
        String fQuarter = "/Volumes/DATA/Development/voice-sample/dominic_1_1_2 2/quarter_a4b3dffa-6295-4614-bb19-cfd668cec6f5_raw.wav";
        String fParticularly = "/Volumes/DATA/Development/voice-sample/dominic_1_1_2 2/particularly_40bbdad9-b5df-4fdc-aad2-c4a574b48288_raw.wav";
        String fNecessarily = "/Volumes/DATA/Development/voice-sample/dominic_1_1_2 2/necessarily_a5774a26-122a-4d33-b7bf-4111416e59d6_raw.wav";
        String fHaiNecessarily = "/Volumes/DATA/Development/voice-sample/necessarily-hai.wav";
        String fHaiSeashell = "/Volumes/DATA/Development/voice-sample/seashell-hai.wav";
        String fHaiVariable = "/Volumes/DATA/Development/voice-sample/variable-hai.wav";
        String fLanNecessarily = "/Volumes/DATA/Development/voice-sample/necessarily-lan.wav";
        String fLan2Necessarily = "/Volumes/DATA/Development/voice-sample/necessarily-lan-2.wav";
        String fAnhNecessarily = "/Volumes/DATA/Development/voice-sample/necessarily-anh.wav";
        String fAnhSeashell = "/Volumes/DATA/Development/voice-sample/seashell-anh.wav";
        String fAnh2Seashell = "/Volumes/DATA/Development/voice-sample/seashell-anh-2.wav";
        String fAnhVariable = "/Volumes/DATA/Development/voice-sample/variable-anh.wav";
        String fAnhVariableCleanedNoised = "/Volumes/DATA/Development/voice-sample/variable-anh-c-n.wav";
        String fAnh2Variable = "/Volumes/DATA/Development/voice-sample/variable-anh-2.wav";
        String fBarter = "/Volumes/DATA/Development/voice-sample/dominic_1_1_2 2/barter_a1f7b6cd-4969-45df-bcd0-cea2d76d8542_raw.wav";
        String fBorrower = "/Volumes/DATA/Development/voice-sample/dominic_1_1_2 2/borrower_8c830b69-4068-45b2-b549-fefed76a2726_raw.wav";


        String buttomUk = "/Volumes/DATA/Development/voice-sample/bottom/bottom-uk.wav";
        String buttomUs = "/Volumes/DATA/Development/voice-sample/bottom/bottom-us.wav";

        Map<String, String> testData = new HashMap<String, String>();
       // testData.put("/Users/cmg/Desktop/voice_example/credit_1e80b112-0f75-489b-9b94-f6e12df88ca9_raw.wav", "credit");
       // testData.put("/Users/cmg/Desktop/voice_example/bullfinch_affef66a-155f-4578-802e-2d776b792d31_raw.wav", "bullfinch");
       // testData.put("/Users/cmg/Desktop/voice_example/finance_3d329c5d-38ab-4cbf-9ac2-f22f4684aa00_raw.wav", "finance");
      // testData.put("/Users/cmg/Desktop/voice_example/rabbit_5866cf48-12a4-48dd-87ee-5a5a432e792a_raw.wav", "rabbit");
      //  testData.put("/Users/cmg/Desktop/voice_example/welcome_b709aadc-6445-4702-8879-73294aa66c17_raw.wav", "welcome");
        //testData.put("/Users/luhonghai/Desktop/audio/pronunciation.wav", "pronunciation");
//        testData.put("/Users/luhonghai/Desktop/audio/speaker2_1.wav", "trivial");
//        Iterator<String> keys = testData.keySet().iterator();
//        while (keys.hasNext()) {
//            long start = System.currentTimeMillis();
//            System.out.println("===========================================");
//            String filePath = keys.next();
//            String word = testData.get(filePath);
//            PhonemesDetector detector = new PhonemesDetector(new File(
//                    filePath)
//                    , word.toLowerCase());
//            SphinxResult rs = null;
//            try {
//                rs = detector.analyze();
//                Gson gson = new GsonBuilder().setPrettyPrinting().create();
//              //  System.out.println(gson.toJson(rs));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            System.out.println("### Analyze word " + word + ". File path: " + filePath
//               + ". Execution time: " + (System.currentTimeMillis() - start) + "ms"
//                + ". Score: " + ((rs == null) ? -1 : rs.getScore()));
//        }
        int value = 0;
        for (int i = 0; i < 100; i++) {
            boolean t = (i & 3) == 2;
            System.out.println(i + " " + t);
        }
    }

    public boolean isAllowAdditionalData() {
        return allowAdditionalData;
    }

    public void setAllowAdditionalData(boolean allowAdditionalData) {
        this.allowAdditionalData = allowAdditionalData;
    }
}

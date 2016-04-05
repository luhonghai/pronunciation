package com.cmg.vrc.util;

import com.cmg.vrc.processor.CommandExecutor;
import com.cmg.vrc.sphinx.DictionaryHelper;
import com.cmg.vrc.sphinx.SphinxResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;
import sun.misc.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by cmg on 30/03/2016.
 */
public class TrainingDataPrepare {

    class VoiceSample {
        String recordFile;

        String username;

        String word;

        float score;

        boolean gender;
    }

    static class PhonemeSample {
        String name;
        double freq;

        PhonemeSample(String name, double freq) {
            this.name = name;
            this.freq = freq;
        }
    }

    static class TrainingSamples {
        Map<String, Float> phonemeSamples;
        float avgGender;
    }

    static Map<String, Integer> phonemeIndex = new HashMap<>();
    static File trainingWordCSV = new File("/Users/luhonghai/Desktop/audio/training-single-word.csv");
    static File trainingPhonemeCSV = new File("/Users/luhonghai/Desktop/audio/training-single-phoneme.csv");
    static File trainingBothCSV = new File("/Users/luhonghai/Desktop/audio/training-word-phoneme.csv");

    static File trainingCSV = new File("/Users/luhonghai/Desktop/audio/training-data.csv");

    static File phoneSamples = new File("/Users/luhonghai/Desktop/audio/phoneme-samples.json");

    static List<Float> totalFrequency = new ArrayList<>();

    static Map<String, List<Float>> femalePhonemeFreq = new HashMap<>();
    static Map<String, List<Float>> malePhonemeFreq = new HashMap<>();

    static float avgGender = 0;

    static List<String> phonemeList = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        testTrainingData();
    }

    static Map<String, Float> sampleAvg = new HashMap<>();

    static List<String> lines = new ArrayList<>();

    static void testTrainingData() throws IOException {
        preparePhoneList();
        File femaleDir = new File("/Users/luhonghai/Desktop/audio/test/female");
        File maleDir = new File("/Users/luhonghai/Desktop/audio/test/male");
        TrainingSamples trainingSamples = new Gson().fromJson(FileUtils.readFileToString(phoneSamples, "UTF-8"), TrainingSamples.class);
        testSampleData(maleDir, true, trainingSamples);
        testSampleData(femaleDir, false, trainingSamples);
        /*Collections.sort(lines);
        if (trainingCSV.exists()) {
            FileUtils.forceDelete(trainingCSV);
        }*/
        //FileUtils.writeLines(trainingCSV, lines);
        System.out.println("Total voice to predict : " + totalPredict);
        System.out.println(totalMatched + " Correct percent: "
                + ((totalMatched / totalPredict) * 100) + "%");
    }

    static void preparePhoneList() {
        phonemeList = DictionaryHelper.getPhonemeList();

        for (int i = 0;+ i< phonemeList.size(); i++) {
            phonemeIndex.put(phonemeList.get(i), i);
        }
    }

    static void generateTrainingData() throws IOException {
        File femaleDir = new File("/Users/luhonghai/Desktop/audio/female");
        preparePhoneList();
//        if (trainingWordCSV.exists()) {
//            FileUtils.forceDelete(trainingWordCSV);
//        }
//        if (trainingPhonemeCSV.exists()) {
//            FileUtils.forceDelete(trainingPhonemeCSV);
//        }
//        if (trainingBothCSV.exists()) {
//            FileUtils.forceDelete(trainingBothCSV);
//        }
        File maleDir = new File("/Users/luhonghai/Desktop/audio/male");
        totalFrequency.clear();
        generateSampleData(maleDir, true, false);
        float avgMale = avgTotal();

        float minMale = Collections.min(totalFrequency);
        float maxMale = Collections.max(totalFrequency);
        System.out.println("Avg freq male: " + avgMale + ". Max: " + maxMale + ". Min: " + minMale);
        totalFrequency.clear();
        generateSampleData(femaleDir, false, false);
        float avgFemale = avgTotal();
        float minFemale = Collections.min(totalFrequency);
        float maxFemale = Collections.max(totalFrequency);
        System.out.println("Avg freq female: " + avgFemale + ". Max: " + maxFemale + ". Min: " + minFemale);
        avgGender = (avgFemale + avgMale) / 2;
        System.out.println("Avg freq gender: " + avgGender);
        System.out.println("malePhonemeFreq size: " + malePhonemeFreq.size());
        System.out.println("femalePhonemeFreq size: " + femalePhonemeFreq.size());
        Map<String, Float> phonemeSamples = new HashMap<>();
        for (String phone : phonemeList) {
            dump(malePhonemeFreq, phone.toUpperCase(), true);
            dump(femalePhonemeFreq, phone.toUpperCase(), false);
            float pGenderAvg = (avgTotal(malePhonemeFreq.get(phone)) + avgTotal(femalePhonemeFreq.get(phone))) / 2;
            phonemeSamples.put(phone, pGenderAvg);
        }
        if (trainingCSV.exists()) {
            FileUtils.forceDelete(trainingCSV);
        }
        generateSampleData(maleDir, true, true);
        generateSampleData(femaleDir, false, true);
        TrainingSamples trainingSamples = new TrainingSamples();
        trainingSamples.phonemeSamples = phonemeSamples;
        trainingSamples.avgGender = avgGender;
        FileUtils.writeStringToFile(phoneSamples, new Gson().toJson(trainingSamples), "UTF-8");
    }

    static void dump(Map<String, List<Float>> data, String phone, boolean gender) {
        if (data.containsKey(phone)) {
            System.out.println(phone + " - "  + (gender ? "MALE" : "FEMALE") + " - avg " + avgTotal(data.get(phone))
                    + " - max " + Collections.max(data.get(phone))
                    + " - min " + Collections.min(data.get(phone)));
        }
    }

    static float maxTotal() {
        return Collections.max(totalFrequency);
    }

    static float avgTotal(List<Float> totalFrequency) {
        if (totalFrequency == null || totalFrequency.size() == 0) return 0;
        float total = 0;
        for (Float f : totalFrequency) {
            total += f;
        }
        return  total / totalFrequency.size();
    }

    static float avgTotal() {
        float total = 0;
        for (Float f : totalFrequency) {
            total += f;
        }
        return  total / totalFrequency.size();
    }

    static int getPhonemeIndex(String name) {
        if (phonemeIndex.containsKey(name)) {
            return phonemeIndex.get(name);
        } else {
            return -1;
        }
    }

    static int totalPredict = 0;

    static int totalMatched = 0;

    static float calculateScore(float avgPhone, float avgWord, final boolean gender) {
        String uuid = UUIDGenerator.generateUUID();
        File tmpScript = new File(FileUtils.getTempDirectory(),uuid + ".tmp.sh");
        File tmpPython = new File(FileUtils.getTempDirectory(),uuid + ".tmp.py");
        try {
            InputStream is = TrainingDataPrepare.class.getClassLoader().getResourceAsStream("predict.py");
            try {
                String script = org.apache.commons.io.IOUtils.toString(is, "UTF-8");
                script = script.replace("%DATA%", avgPhone + "," + avgWord);
                FileUtils.writeStringToFile(tmpPython, script, "UTF-8");
                FileUtils.writeStringToFile(tmpScript, "python " + tmpPython.getAbsolutePath(), "UTF-8");
                CommandExecutor.execute(null, new CommandExecutor.CommandListener() {
                    @Override
                    public void onMessage(String message) {
                        System.out.println(message);
                        totalPredict++;
                        float output = Float.parseFloat(message.substring(1, message.length() - 1));
                        System.out.println(output);
                        if (gender) {
                            if (output >= 0.5f) {
                                totalMatched++;
                            }
                        } else {
                            if (output < 0.5f) {
                                totalMatched++;
                            }
                        }
                    }

                    @Override
                    public void onError(String message, Throwable e) {
                        //System.out.println(message);
                        //e.printStackTrace();
                    }
                }, "sh", tmpScript.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                org.apache.commons.io.IOUtils.closeQuietly(is);
            }
        } finally {
            if (tmpScript.exists()) {
                try {
                    FileUtils.forceDelete(tmpScript);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (tmpPython.exists()) {
                try {
                    FileUtils.forceDelete(tmpPython);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    static void testSampleData(File dir, boolean gender, TrainingSamples trainingSamples) throws IOException {
        Gson gson = new Gson();

        File[] wavList = dir.listFiles();
        if (wavList != null && wavList.length > 0) {
            for (File file : wavList) {
                String name = file.getName();
                if (name.endsWith(".wav")) {
                    // System.out.println("Found wav file " + name);
                    File jsonFile = new File(dir, name + ".json");
                    SphinxResult result = gson.fromJson(FileUtils.readFileToString(jsonFile, "UTF-8"), SphinxResult.class);
                    float avgFreq = 0;
                    float totalFreq = 0;
                    float count = 0;
                    List<PhonemeSample> phonemeSamples = new ArrayList<>();
                    Map<String, List<Float>> phonemeFreq = new HashMap<>();
                    List<SphinxResult.Phoneme> phonemes = result.getBestPhonemes();
                    if (phonemes != null && phonemes.size() > 0) {
                        for (SphinxResult.Phoneme phoneme : phonemes) {
                            List<SphinxResult.PhonemeExtra> extras = phoneme.getExtras();
                            if (extras != null && extras.size() > 0) {
                                for (SphinxResult.PhonemeExtra extra : extras) {
                                    count++;
                                    double freq = extra.getFrequency();
                                    totalFreq += freq;
                                    String pName = phoneme.getName().toUpperCase();
                                    phonemeSamples.add(new PhonemeSample(phoneme.getName(), freq));
                                    if (phonemeFreq.containsKey(pName)) {
                                        phonemeFreq.get(pName).add((float)freq);
                                    } else {
                                        List<Float> data = new ArrayList<>();
                                        data.add((float)freq);
                                        phonemeFreq.put(pName, data);
                                    }
                                }
                            }
                        }
                    }
                    avgFreq = totalFreq / count;
                    Iterator<String> keys = phonemeFreq.keySet().iterator();
                    while (keys.hasNext()) {
                        String p = keys.next();
                        List<Float> data = phonemeFreq.get(p);
                        float phoneAvg = avgTotal(data);
                        float pGenderAvg = trainingSamples.avgGender;
                        if ( trainingSamples.phonemeSamples.containsKey(p)) {
                            pGenderAvg = trainingSamples.phonemeSamples.get(p);
                        }
//                        String testData = (phoneAvg - pGenderAvg) + ","
//                                + (avgFreq - trainingSamples.avgGender) + ","
//                                + (gender ? 1 : 0);
//                        lines.add(testData);
                        System.out.print((phoneAvg - pGenderAvg) + ","
                                + (avgFreq - trainingSamples.avgGender) + "," + " | Expected: " + (gender ? 1 : 0));
                        calculateScore(phoneAvg - pGenderAvg,
                                avgFreq - trainingSamples.avgGender,
                                gender);
                    }
                }
            }
        }
    }

    static void generateSampleData(File dir, boolean gender, boolean write) throws IOException {
        Gson gson = new Gson();
        File[] wavList = dir.listFiles();

        if (wavList != null && wavList.length > 0) {
            for (File file : wavList) {
                String name = file.getName();
                if (name.endsWith(".wav")) {
                   // System.out.println("Found wav file " + name);
                    File jsonFile = new File(dir, name + ".json");
                    SphinxResult result = gson.fromJson(FileUtils.readFileToString(jsonFile, "UTF-8"), SphinxResult.class);
                    float avgFreq = 0;
                    float totalFreq = 0;
                    float count = 0;
                    List<PhonemeSample> phonemeSamples = new ArrayList<>();
                    Map<String, List<Float>> phonemeFreq = new HashMap<>();
                    List<SphinxResult.Phoneme> phonemes = result.getBestPhonemes();
                    if (phonemes != null && phonemes.size() > 0) {
                        for (SphinxResult.Phoneme phoneme : phonemes) {
                            List<SphinxResult.PhonemeExtra> extras = phoneme.getExtras();
                            if (extras != null && extras.size() > 0) {
                                for (SphinxResult.PhonemeExtra extra : extras) {
                                    count++;
                                    double freq = extra.getFrequency();
                                    totalFreq += freq;
                                    String pName = phoneme.getName().toUpperCase();
                                    if (!write) {
                                        final Map<String, List<Float>> map = gender ? malePhonemeFreq : femalePhonemeFreq;
                                        if (map.containsKey(pName)) {
                                            map.get(pName).add((float) freq);
                                        } else {
                                            List<Float> data = new ArrayList<>();
                                            data.add((float) freq);
                                            map.put(pName, data);
                                        }
                                    }
                                    phonemeSamples.add(new PhonemeSample(phoneme.getName(), freq));
                                    if (phonemeFreq.containsKey(pName)) {
                                        phonemeFreq.get(pName).add((float)freq);
                                    } else {
                                        List<Float> data = new ArrayList<>();
                                        data.add((float)freq);
                                        phonemeFreq.put(pName, data);
                                    }
                                }
                            }
                        }
                    }
                    avgFreq = totalFreq / count;
                    if (!write) {
                        totalFrequency.add(avgFreq);
                    }

                    Iterator<String> keys = phonemeFreq.keySet().iterator();
                    while (keys.hasNext()) {
                        String p = keys.next();
                        List<Float> data = phonemeFreq.get(p);
                        float phoneAvg = avgTotal(data);
                        float pGenderAvg = (avgTotal(malePhonemeFreq.get(p)) + avgTotal(femalePhonemeFreq.get(p))) / 2;
                        if (write) {
                            FileUtils.writeStringToFile(trainingCSV, (phoneAvg - pGenderAvg) + ","
                                    + (avgFreq - avgGender) + ","
                                    + (gender ? 1 : 0) + "\n", true);
                        }
                    }
                    if (write) {
                        //FileUtils.writeStringToFile(trainingWordCSV, avgFreq + "," + (gender ? 1 : 0) + "\n", true);
                    }
                    for (PhonemeSample phonemeSample : phonemeSamples) {
                        if (write) {
                            //FileUtils.writeStringToFile(trainingPhonemeCSV, getPhonemeIndex(phonemeSample.name) + "," + phonemeSample.freq + "," + (gender ? 1 : 0) + "\n", true);
                            //FileUtils.writeStringToFile(trainingBothCSV, getPhonemeIndex(phonemeSample.name) + "," + phonemeSample.freq + "," + avgFreq + "," + (gender ? 1 : 0) + "\n", true);

                        }
                    }
                }
            }
        }
    }


    static void collectSample() throws IOException {
        Gson gson = new Gson();
        File raw = new File("/Users/cmg/Desktop/voice-sample/USER.json");
        File rootDir = new File("/Users/cmg/Desktop/voice-sample/voices");
        File femaleDir = new File("/Users/cmg/Desktop/voice-sample/female");
        File maleDir = new File("/Users/cmg/Desktop/voice-sample/male");
        rootDir.mkdirs();
        List<VoiceSample> users = gson.fromJson(FileUtils.readFileToString(raw, "UTF-8"), new TypeToken<List<VoiceSample>>(){}.getType());
        System.out.println("Recorded voice count " + users.size());
        AWSHelper awsHelper = new AWSHelper();
        int count =0 ;
        for (VoiceSample user: users) {
            File userDir = new File(rootDir, user.username.toLowerCase());
            if (!userDir.exists()) {
                userDir.mkdirs();
            }
            File wavFile = new File(userDir, user.recordFile);
            File jsonFile = new File(userDir, user.recordFile + ".json");
            //awsHelper.download(Constant.FOLDER_RECORDED_VOICES + "/" + user.username + "/" + user.recordFile, wavFile);
            //PhonemesDetector phonemesDetector = new PhonemesDetector(wavFile, user.word);
            //phonemesDetector.setAllowAdditionalData(true);
//            try {
//                SphinxResult result = phonemesDetector.analyze();
//                FileUtils.writeStringToFile(jsonFile, gson.toJson(result), "UTF-8");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            if (wavFile.exists() && jsonFile.exists()) {
                SphinxResult result = gson.fromJson(FileUtils.readFileToString(jsonFile, "UTF-8"), SphinxResult.class);
                if (result.getScore() >= 40) {
                    FileUtils.copyFile(wavFile, new File(user.gender ? maleDir : femaleDir, user.recordFile));
                    FileUtils.copyFile(jsonFile, new File(user.gender ? maleDir : femaleDir, user.recordFile + ".json"));
                    count++;
                }
            }
        }
        System.out.println("Number of voices " + count);
    }
}

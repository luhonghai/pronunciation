package com.cmg.vrc.util;

import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.jdo.Phoneme;
import com.cmg.vrc.sphinx.DictionaryHelper;
import com.cmg.vrc.sphinx.PhonemesDetector;
import com.cmg.vrc.sphinx.SphinxResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
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

    static Map<String, Integer> phonemeIndex = new HashMap<>();
    static File trainingWordCSV = new File("/Users/luhonghai/Desktop/audio/training-single-word.csv");
    static File trainingPhonemeCSV = new File("/Users/luhonghai/Desktop/audio/training-single-phoneme.csv");
    static File trainingBothCSV = new File("/Users/luhonghai/Desktop/audio/training-word-phoneme.csv");

    static File trainingCSV = new File("/Users/luhonghai/Desktop/audio/training-data.csv");

    static List<Float> totalFrequency = new ArrayList<>();

    static Map<String, List<Float>> femalePhonemeFreq = new HashMap<>();
    static Map<String, List<Float>> malePhonemeFreq = new HashMap<>();

    static float avgGender = 0;

    public static void main(String[] args) throws IOException {
        File femaleDir = new File("/Users/luhonghai/Desktop/audio/female");
        List<String> phonemeList = DictionaryHelper.getPhonemeList();

        for (int i = 0; i< phonemeList.size(); i++) {
            phonemeIndex.put(phonemeList.get(i), i);
        }
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
        for (String phone : phonemeList) {
            dump(malePhonemeFreq, phone.toUpperCase(), true);
            dump(femalePhonemeFreq, phone.toUpperCase(), false);
        }
        if (trainingCSV.exists()) {
            FileUtils.forceDelete(trainingCSV);
        }
        generateSampleData(maleDir, true, true);
        generateSampleData(femaleDir, false, true);
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
                                    final Map<String, List<Float>> map = gender ? malePhonemeFreq : femalePhonemeFreq;
                                    String pName = phoneme.getName().toUpperCase();

                                    if (map.containsKey(pName)) {
                                        map.get(pName).add((float)freq);
                                    } else {
                                        List<Float> data = new ArrayList<>();
                                        data.add((float)freq);
                                        map.put(pName, data);
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
                    totalFrequency.add(avgFreq);

                    Iterator<String> keys = phonemeFreq.keySet().iterator();
                    while (keys.hasNext()) {
                        String p = keys.next();
                        List<Float> data = phonemeFreq.get(p);
                        float phoneAvg = avgTotal(data);
                        float pGenderAvg = (avgTotal(malePhonemeFreq.get(p)) + avgTotal(femalePhonemeFreq.get(p))) / 2;
                        FileUtils.writeStringToFile(trainingCSV, (phoneAvg - pGenderAvg) + ","
                                                + (avgFreq - avgGender) + ","
                                                + (gender ? 1 : 0) + "\n", true);
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

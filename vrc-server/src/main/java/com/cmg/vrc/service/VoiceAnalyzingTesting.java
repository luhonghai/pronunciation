package com.cmg.vrc.service;

import com.cmg.vrc.http.FileCommon;
import com.cmg.vrc.http.FileUploader;
import com.cmg.vrc.sphinx.SphinxResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luhonghai on 4/21/15.
 */
public class VoiceAnalyzingTesting {

    public static final TestingData[] data = new TestingData[] {
//            new TestingData("necessarily-anh.wav","necessarily", "/Volumes/DATA/Development/voice-sample/necessarily-anh.wav"),
            //new TestingData("necessarily-hai.wav","necessarily", "/Volumes/DATA/Development/voice-sample/necessarily-hai.wav"),
           // new TestingData("necessarily-lan.wav","necessarily", "/Volumes/DATA/Development/voice-sample/necessarily-lan.wav"),
            new TestingData("necessarily-dominic.wav","necessarily", "/Volumes/DATA/Development/voice-sample/necessarily-dominic.wav"),
            new TestingData("seashell-anh.wav","seashell", "/Volumes/DATA/Development/voice-sample/seashell-anh.wav"),
            new TestingData("seashell-hai.wav","seashell", "/Volumes/DATA/Development/voice-sample/seashell-hai.wav"),
            new TestingData("variable-hai.wav","variable", "/Volumes/DATA/Development/voice-sample/variable-hai.wav"),
            new TestingData("variable-anh.wav","variable", "/Volumes/DATA/Development/voice-sample/variable-anh.wav")
    };

    public static class TestingData {

        public TestingData() {

        }

        public TestingData(String name, String word, String filePath) {
            this.setName(name);
            this.setWord(word);
            this.setFilePath(filePath);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        private String name;

        private String word;

        private String filePath;

        private long startTime;

        private long endTime;

        private long totalTime;

        private float score;

        private SphinxResult result;

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public long getTotalTime() {
            return totalTime;
        }

        public void setTotalTime(long totalTime) {
            this.totalTime = totalTime;
        }

        public float getScore() {
            return score;
        }

        public void setScore(float score) {
            this.score = score;
        }

        public SphinxResult getResult() {
            return result;
        }

        public void setResult(SphinxResult result) {
            this.result = result;
        }
    }


    public static void test(TestingData testingData, String targetFolder) {
        testingData.setStartTime(System.currentTimeMillis());
        System.out.println("Start analyze " + testingData.getName());
        Map<String, String> data = new HashMap<String,String>();
        data.put(FileCommon.PARA_FILE_NAME, testingData.getName());
        data.put("word", testingData.getWord());
        data.put(FileCommon.PARA_FILE_PATH, testingData.getFilePath());
        data.put("key", "AIzaSyBtW7fSwFlG9xKCS2rsYPGobKUMWN7B6vY");
        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().setPrettyPrinting().create();
        float score = 0.0f;

        try {
            String result = FileUploader.upload(data, "http://dyyazm8g35t11.cloudfront.net/vrc/VoiceAnalyzeHandler");
            //System.out.println(result);
            if (!result.trim().startsWith("{")) {
                System.out.println(result);
            }
            testingData.setResult(gson.fromJson(result, SphinxResult.class));
            if (testingData.getResult() != null) {
                score = testingData.getResult().getScore();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        testingData.setScore(score);
        testingData.setEndTime(System.currentTimeMillis());
        testingData.setTotalTime(testingData.getEndTime() - testingData.getStartTime());
        System.out.println("Complete analyze "
                + testingData.getName()
                + ". Score: " + testingData.getScore()
                + ". Total time: " + testingData.getTotalTime() + "ms");
        try {
            FileUtils.write(new File(targetFolder, testingData.getName() + ".json"), gson.toJson(testingData), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        System.out.println("========================== NONE THREAD SECTION ==================");
        String targetFolder = "/Volumes/DATA/Development/analyzing-result/none-thread";
        for (TestingData testingData : data) {
            test(testingData, targetFolder);
        }
        System.out.println("========================== MULTI THREAD SECTION ==================");
        final String targetFolderThread = "/Volumes/DATA/Development/analyzing-result/multi-thread";
        for (final TestingData testingData : data) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    test(testingData, targetFolderThread);
                }
            }).start();

        }
    }
}

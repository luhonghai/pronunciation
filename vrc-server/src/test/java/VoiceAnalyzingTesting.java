import com.cmg.vrc.http.FileCommon;
import com.cmg.vrc.http.FileUploader;
import com.cmg.vrc.sphinx.SphinxResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luhonghai on 4/21/15.
 */
public class VoiceAnalyzingTesting {

    public static final TestingData[] data = new TestingData[] {
            new TestingData("","", "/Users/cmg/Desktop/voice-sample/"),
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
        printLog("Start analyze " + testingData.getName());
        Map<String, String> data = new HashMap<String,String>();
        data.put(FileCommon.PARA_FILE_NAME, testingData.getName());
        data.put("word", testingData.getWord());
        data.put(FileCommon.PARA_FILE_PATH, testingData.getFilePath());
        data.put("key", "AIzaSyBtW7fSwFlG9xKCS2rsYPGobKUMWN7B6vY");
        Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().setPrettyPrinting().create();
        float score = 0.0f;

        try {
            String result = FileUploader.upload(data, "http://accenteasytomcat-prd.elasticbeanstalk.com/VoiceAnalyzeHandler");
            //System.out.println(result);
            if (!result.trim().startsWith("{")) {
                printLog(result);
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
        printLog("Complete analyze "
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
        int wordSize = 20;
        File sourceVoice = new File("/Users/cmg/Desktop/voice-sample/");
        File[] list = sourceVoice.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.getName().equalsIgnoreCase(".DS_Store")) return false;
                return  (pathname.isFile());
            }
        });
        if (wordSize > list.length) wordSize = list.length;
        TestingData[] data = new TestingData[wordSize];
        for (int i = 0; i < wordSize; i++) {
            TestingData td = new TestingData();
            td.setName(list[i].getName());
            td.setFilePath(list[i].getAbsolutePath());
            td.setWord(td.getName().split("_")[0]);
            data[i] = td;
        }
        printLog("Total words to analyze: " + wordSize);
        printLog("========================== NONE THREAD SECTION ==================");
        String targetFolder = "/Users/cmg/Desktop/voice-sample/result/none-thread";
        for (TestingData testingData : data) {
            test(testingData, targetFolder);
        }
        printLog("========================== MULTI THREAD SECTION ==================");
        final String targetFolderThread = "/Users/cmg/Desktop/voice-sample/result/multi-thread";
        for (final TestingData testingData : data) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    test(testingData, targetFolderThread);
                }
            }).start();

        }
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private static void printLog(String log) {
        System.out.println(sdf.format(System.currentTimeMillis()) + " | " + log);
    }
}

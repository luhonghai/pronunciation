package com.cmg.vrc.util;

import com.cmg.vrc.common.Constant;
import com.cmg.vrc.sphinx.PhonemesDetector;
import com.cmg.vrc.sphinx.SphinxResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

    public static void main(String[] args) throws IOException {
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

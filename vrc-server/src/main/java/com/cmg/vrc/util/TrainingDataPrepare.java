package com.cmg.vrc.util;

import com.cmg.vrc.common.Constant;
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
        rootDir.mkdirs();
        List<VoiceSample> users = gson.fromJson(FileUtils.readFileToString(raw, "UTF-8"), new TypeToken<List<VoiceSample>>(){}.getType());
        System.out.println("Recorded voice count " + users.size());
        AWSHelper awsHelper = new AWSHelper();
        for (VoiceSample user: users) {
            File userDir = new File(rootDir, user.username.toLowerCase());
            if (!userDir.exists()) {
                userDir.mkdirs();
            }
            awsHelper.download(Constant.FOLDER_RECORDED_VOICES + "/" + user.username + "/" + user.recordFile, new File(userDir, user.recordFile));
        }
    }
}

package com.cmg.android.voicerecorder.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;

/**
 * Created by luhonghai on 12/22/14.
 */
public class FileHelper {

    public static final String WAV_EXTENSION = ".wav";

    public static final String JSON_EXTENSION = ".json";

    private static final String PRONUNCIATION_DEFAULT_DIR = "Pronunciation";

    private static final String AUDIO_DEFAULT_DIR = "audio";

    private static final String PRONUNCIATION_SCORE_DIR = "score";

    public static File getAudioDir(Context context) {
        return new File(getApplicationDir(context), AUDIO_DEFAULT_DIR);
    }

    public static File getPronunciationScoreDir(Context context) {
        return new File(getApplicationDir(context), PRONUNCIATION_SCORE_DIR);
    }

    public static File getApplicationDir(Context context) {
        PackageManager m = context.getPackageManager();
        String s = context.getPackageName();
        try {
            PackageInfo p = m.getPackageInfo(s, 0);
            return new File(p.applicationInfo.dataDir);
        } catch (PackageManager.NameNotFoundException e) {
            return new File(Environment.getExternalStorageDirectory().getPath() + File.separator + PRONUNCIATION_DEFAULT_DIR);
        }
    }
}

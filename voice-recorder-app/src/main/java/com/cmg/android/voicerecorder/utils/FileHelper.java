package com.cmg.android.voicerecorder.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.File;

/**
 * Created by luhonghai on 12/22/14.
 */
public class FileHelper {

    public static File getAudioDir(Context context) {
        PackageManager m = context.getPackageManager();
        String s = context.getPackageName();
        try {
            PackageInfo p = m.getPackageInfo(s, 0);
            s = p.applicationInfo.dataDir;
            return new File(s, "audio");
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }
}

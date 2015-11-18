package com.cmg.android.bbcaccent.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;

import com.cmg.android.bbcaccent.MainApplication;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;

/**
 * Created by luhonghai on 12/22/14.
 */
public class FileHelper {

    public static final String WAV_EXTENSION = ".wav";

    public static final String JSON_EXTENSION = ".json";

    private static final String PRONUNCIATION_DEFAULT_DIR = "Pronunciation";

    private static final String AUDIO_DEFAULT_DIR = "audio";

    private static final String PRONUNCIATION_SCORE_DIR = "score";

    private static final String IPA_CACHE_DIR = "ipa";

    private static final String DOWNLOAD_CACHE_DIR = "downloads";

    private static final String TIPS_JSON_FILE = "tips.json" + JSON_EXTENSION;

    public static final String SCREENSHOOTS_FOLDER = "screenshoot";

    public static File getAudioDir(Context context) {
        return new File(getApplicationDir(context), AUDIO_DEFAULT_DIR);
    }

    public static File getPronunciationScoreDir(Context context) {
        return new File(getApplicationDir(context), PRONUNCIATION_SCORE_DIR);
    }

    public static File getIPACacheDir(Context context) {
        File dir = new File(getApplicationDir(context), IPA_CACHE_DIR);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File getApplicationDir() {
        return getApplicationDir(MainApplication.getContext());
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

    public static File getSavedTipFile(Context context) {
        return new File(getApplicationDir(context), TIPS_JSON_FILE);
    }

    public static String getFilePath(String folderName, String fileName,
                                     Context context) {
        File folder = new File(getApplicationDir(context), folderName);
        if (!folder.exists() || !folder.isDirectory())
            folder.mkdirs();
        return new File(folder, fileName).getAbsolutePath();
    }

    public static String getFolderPath(String folderName, Context context) {
        File folder = new File(getApplicationDir(context), folderName);
        if (!folder.exists() || !folder.isDirectory())
            folder.mkdirs();
        return folder.getAbsolutePath();
    }

    public static String getCachedFilePath(String url) {
        File file = new File(getFolderPath(DOWNLOAD_CACHE_DIR, MainApplication.getContext()), MD5Util.md5Hex(url));
        if (!file.exists()) {
            try {
                SimpleAppLog.debug("Download url " + url + " to cached file " + file.getAbsolutePath());
                FileUtils.copyURLToFile(new URL(url), file, 30000, 30000);
            } catch (Exception e) {
                SimpleAppLog.error("Could not download url " + url + " to file " + file.getAbsolutePath(),e);
            }
        }
        return file.getAbsolutePath();
    }
}

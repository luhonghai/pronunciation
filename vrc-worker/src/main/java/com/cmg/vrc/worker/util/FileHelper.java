package com.cmg.vrc.worker.util;

import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created by cmg on 16/09/15.
 */
public class FileHelper {

    private static final String SPHINX_DATA = "sphinx_data";

    public static File getTmpDir() {
        File dir = new File(FileUtils.getTempDirectory(), SPHINX_DATA);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File getTmpDir(String subfolder) {
        File dir = new File(getTmpDir(), subfolder);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
        }
        return dir;
    }
}

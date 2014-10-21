package com.cmg.vrc.processor;

import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created by luhonghai on 10/21/14.
 */
public class SoXCleaner extends AudioCleaner {

    public SoXCleaner(File targetClean, File targetRaw) {
        super(targetClean, targetRaw);
    }

    @Override
    public void clean() throws Exception {
        if (targetRaw == null || !targetRaw.exists() || targetRaw.isDirectory())
            return;
        FileUtils.copyFile(targetRaw,targetClean);
        if (targetClean.exists()) {
            SoXExecutor soXExecutor = new SoXExecutor(targetClean);
            soXExecutor
                    .noisered()
                    .silence()
                    //.highpass(".3k")
                    //.lowpass("0.08k")
                    //.compand()
                    ;
        }
    }

    public static void main(String[] args) throws Exception {
        AudioCleaner cleaner = new SoXCleaner(new File("/Volumes/DATA/Development/voice-sample/eleven_x.wav"), new File("/Volumes/DATA/OSX/luhonghai/Downloads/eleven_raw.wav"));
        cleaner.clean();
    }
}

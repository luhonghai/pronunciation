package com.cmg.vrc.processor;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.logging.Level;

/**
 * Created by luhonghai on 10/21/14.
 */
public class SoXCleaner extends AudioCleaner {

    public SoXCleaner(File targetClean, File targetRaw) {
        super(targetClean, targetRaw);
    }

    @Override
    public void clean() throws Exception {
        if (targetRaw == null || !targetRaw.exists() || targetRaw.isDirectory()) {
            logger.log(Level.SEVERE, "File not found " + targetRaw);
            return;
        }
        FileUtils.copyFile(targetRaw,targetClean);
        if (targetClean.exists()) {
            SoXExecutor soXExecutor = new SoXExecutor(targetClean);
            soXExecutor
                    .noisered()
                    //.silence()
                    //.highpass(".3k")
                    //.lowpass("0.08k")
                    //.compand()
                    ;
        }
    }

    public static void main(String[] args) throws Exception {
        AudioCleaner cleaner =
                new SoXCleaner(
                        new File("/Volumes/DATA/Development/voice-sample/variable-anh-c-n.wav"),
                        new File("/Volumes/DATA/Development/voice-sample/variable-anh.wav"));
        cleaner.clean();
    }
}

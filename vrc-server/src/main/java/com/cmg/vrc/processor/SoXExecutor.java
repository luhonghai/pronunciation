package com.cmg.vrc.processor;

import com.cmg.vrc.util.UUIDGenerator;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by luhonghai on 10/21/14.
 *  Official document http://sox.sourceforge.net/sox.html
 */
public class SoXExecutor {
    private static final Logger logger = Logger.getLogger(SoXExecutor.class.getName());

    private final File target;

    private File tmp;

    public SoXExecutor(File target) {
        this.target = target;
    }

    private void init() {
        tmp = new File(FileUtils.getTempDirectory(),UUIDGenerator.generateUUID() + ".wav");
        try {
            FileUtils.copyFile(target, tmp);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not create clone of target file", e);
        }
    }

    private void destroy() {
        if (tmp != null && tmp.exists()) {
            try {
                FileUtils.forceDelete(tmp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public SoXExecutor noisered() {
        init();
        File noiseProfile = new File(FileUtils.getTempDirectory(), UUIDGenerator.generateUUID() + ".noise-profile");
        try {
            executeCommand("sox " + tmp.getAbsolutePath() + " -n noiseprof " + noiseProfile.getAbsolutePath());
            executeCommand("sox " + tmp.getAbsolutePath() + " " + target.getAbsolutePath() + " noisered " + noiseProfile.getAbsolutePath());
        } finally {
            if (noiseProfile.exists()) {
                try {
                    FileUtils.forceDelete(noiseProfile);
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Could not delete tmp noise profile " + noiseProfile, e);
                }
            }
            destroy();
            return this;
        }
    }

    public SoXExecutor silence() {
        return silence("1 .5 0% 1 .5 0%");
    }

    public SoXExecutor silence(String params) {
        return execute("silence " + params);
    }

    /**
     * The following default params might be used to make a piece of music with both quiet and loud passages
     * suitable for listening to in a noisy environment such as a moving vehicle
     * @return
     */
    public SoXExecutor compand() {
        return  compand("0.3,1 6:−70,−60,−20 −5 −90 0.2");
    }

    public SoXExecutor compand(String params) {
        return execute("compand " + params);
    }

    public SoXExecutor lowpass(String params) {
        return execute("lowpass " + params);
    }

    public SoXExecutor highpass(String params) {
        return execute("highpass " + params);
    }

    public SoXExecutor execute(String params) {
        init();
        try {
            executeCommand("sox " + tmp.getAbsolutePath() + " " + target.getAbsolutePath() + " " + params);
        } finally {
            destroy();
            return this;
        }
    }

    private int executeCommand(String command) {
        try {
            logger.info("Execute command " + command);
            Process proc = Runtime.getRuntime().exec(command);
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
                String line = null;
                while ((line = stdInput.readLine()) != null) {
                    logger.info(line);
                }
                while ((line = stdError.readLine()) != null) {
                    logger.log(Level.WARNING, line);
                }
            return proc.waitFor();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Could not execute command " + command,e);
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Could not execute command " + command, e);
        }
        return -1;
    }
}

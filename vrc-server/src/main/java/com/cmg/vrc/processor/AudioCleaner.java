package com.cmg.vrc.processor;

import java.io.File;
import java.util.logging.Logger;

/**
 * Created by luhonghai on 10/17/14.
 */
public abstract class AudioCleaner {
    protected Logger logger;
    protected final File targetRaw;
    protected final File targetClean;
    public AudioCleaner(File targetClean, File targetRaw) {
        logger = Logger.getLogger(this.getClass().getName());
        this.targetClean = targetClean;
        this.targetRaw = targetRaw;
    }

    public abstract void clean() throws Exception;
}

package com.cmg.vrc.processor;

import it.sauronsoftware.jave.FFMPEGLocator;

/**
 * Created by CMGT400 on 9/25/2015.
 */
public class CustomFFMPEGLocator extends FFMPEGLocator {
    private static final String DEFAULT_LINUX_PATH = "/usr/bin/ffmpeg";

    private final String path;

    public CustomFFMPEGLocator() {
        this(DEFAULT_LINUX_PATH);
    }

    public CustomFFMPEGLocator(String path) {
        this.path = path;
    }

    @Override
    protected String getFFMPEGExecutablePath() {
        return path;
    }
}

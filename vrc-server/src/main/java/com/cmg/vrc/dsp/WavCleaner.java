package com.cmg.vrc.dsp;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.GainProcessor;
import be.tarsos.dsp.filters.LowPassFS;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.io.jvm.WaveformWriter;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

/**
 * Created by luhonghai on 10/17/14.
 */
public class WavCleaner {
    private final File targetRaw;
    private final File targetClean;
    public WavCleaner(File targetClean, File targetRaw) {
        this.targetClean = targetClean;
        this.targetRaw = targetRaw;
    }

    public void clean() throws IOException, UnsupportedAudioFileException {
        AudioFormat format = AudioSystem.getAudioFileFormat(targetRaw).getFormat();
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromFile(targetRaw, 1024, 0);
        dispatcher.addAudioProcessor(new LowPassFS(400f, format.getSampleRate()));
        // dispatcher.addAudioProcessor(new HighPass(50f, format.getSampleRate()));
        dispatcher.addAudioProcessor(new GainProcessor(2));
        dispatcher.addAudioProcessor(new WaveformWriter(format, targetClean.getAbsolutePath()));
        dispatcher.run();
    }
}

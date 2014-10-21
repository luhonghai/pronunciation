package com.cmg.vrc.processor;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.GainProcessor;
import be.tarsos.dsp.filters.HighPass;
import be.tarsos.dsp.filters.LowPassFS;
import be.tarsos.dsp.io.jvm.AudioDispatcherFactory;
import be.tarsos.dsp.io.jvm.WaveformWriter;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import java.io.File;

/**
 * Created by luhonghai on 10/21/14.
 */
public class TarsosDSPCleaner extends AudioCleaner {

    public TarsosDSPCleaner(File targetClean, File targetRaw) {
        super(targetClean, targetRaw);
    }

    @Override
    public void clean() throws Exception {
        AudioFormat format = AudioSystem.getAudioFileFormat(targetRaw).getFormat();
        AudioDispatcher dispatcher = AudioDispatcherFactory.fromFile(targetRaw, 1024, 0);
        dispatcher.addAudioProcessor(new LowPassFS(80f, format.getSampleRate()));
        dispatcher.addAudioProcessor(new HighPass(300f, format.getSampleRate()));
        dispatcher.addAudioProcessor(new GainProcessor(2));
        dispatcher.addAudioProcessor(new WaveformWriter(format, targetClean.getAbsolutePath()));
        dispatcher.run();
    }
}

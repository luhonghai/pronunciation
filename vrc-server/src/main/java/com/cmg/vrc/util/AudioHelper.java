package com.cmg.vrc.util;

import com.cmg.vrc.processor.CommandExecutor;
import com.cmg.vrc.processor.CustomFFMPEGLocator;
import com.cmg.vrc.properties.Configuration;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by luhonghai on 4/5/16.
 */
public class AudioHelper {

    public static boolean convertToWav(File target, File output) {
        return convertToWav(target, output, null);
    }

    public static boolean convertToWavEncoder(File target, File output) {
        AudioAttributes audio = new AudioAttributes();
        //  audio.setBitRate(128000);
        audio.setChannels(1);
        audio.setSamplingRate(16000);
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("wav");
        attrs.setAudioAttributes(audio);
        String env = Configuration.getValue(Configuration.SYSTEM_ENVIRONMENT);
        Encoder encoder;
        if (env.equalsIgnoreCase("prod") || env.equalsIgnoreCase("sat")
                || env.equalsIgnoreCase("int")
                || env.equalsIgnoreCase("aws")) {
            encoder = new Encoder(new CustomFFMPEGLocator());
        } else {
            encoder = new Encoder(new CustomFFMPEGLocator.MacFFMPEGLocator());
        }
        try {
            encoder.encode(target, output, attrs);
        } catch (Exception e) {
            // ingore
            e.printStackTrace();
        }
        return output.exists();
    }

    public static boolean convertToWav(File target, File output, CommandExecutor.CommandListener listener) {
        File tmpScript = new File(FileUtils.getTempDirectory(), UUIDGenerator.generateUUID() + ".convert.tmp.sh");
        try {
            FileUtils.writeStringToFile(tmpScript, "ffmpeg -i \"" + target.getAbsolutePath() + "\" -acodec pcm_s16le -ac 1 -ar 16000 \""  + output.getAbsolutePath() + "\"", "UTF-8");
            CommandExecutor.execute(target.getParentFile(), listener, "sh", tmpScript.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (tmpScript.exists()) {
                try {
                    FileUtils.forceDelete(tmpScript);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return output.exists();
    }
}

package com.cmg.vrc.util;

import com.cmg.vrc.processor.CommandExecutor;
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

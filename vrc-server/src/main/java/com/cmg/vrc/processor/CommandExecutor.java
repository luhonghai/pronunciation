package com.cmg.vrc.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by cmg on 09/10/2015.
 */
public class CommandExecutor {

    public interface CommandListener {

        void onMessage(String message);

        void onError(String message, Throwable e);
    }

    public static void execute(File targetDir, CommandListener listener, String... commands) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        if (targetDir != null)
            processBuilder.directory(targetDir);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        BufferedReader processOutputReader = null;
        try {
            processOutputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String readLine;
            while ((readLine = processOutputReader.readLine()) != null) {
                if (listener != null)
                    listener.onMessage(readLine);
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw e;
        } finally {
            if (processOutputReader != null)
                try {
                    processOutputReader.close();
                } catch (Exception e) {}
        }
    }
}

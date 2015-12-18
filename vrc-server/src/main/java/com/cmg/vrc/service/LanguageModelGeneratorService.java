package com.cmg.vrc.service;

import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.dao.impl.AcousticModelVersionDAO;
import com.cmg.vrc.data.dao.impl.LanguageModelVersionDAO;
import com.cmg.vrc.data.jdo.AcousticModelVersion;
import com.cmg.vrc.data.jdo.LanguageModelVersion;
import com.cmg.vrc.sphinx.training.AcousticModelTraining;
import com.cmg.vrc.sphinx.training.LanguageModelGenerator;
import com.cmg.vrc.util.AWSHelper;
import com.cmg.vrc.util.UUIDGenerator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by cmg on 07/10/2015.
 */
public class LanguageModelGeneratorService {

    private static final Logger logger = Logger.getLogger(LanguageModelGeneratorService.class.getName());

    private static ExecutorService executorService = Executors.newFixedThreadPool(1);

    private static final LanguageModelGeneratorService instance = new LanguageModelGeneratorService();

    public static synchronized LanguageModelGeneratorService getInstance() {
        return instance;
    }

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());

    private String admin;

    private boolean running;

    private boolean stopping;

    private File targetDir;

    private File currentLogFile;

    private String projectName = "empty";

    private LanguageModelGenerator generator;

    private WeakReference<Future<?>> runningFuture;

    private File tmpDir = new File(FileUtils.getTempDirectory(), "language_model_generator");

    private AWSHelper awsHelper = new AWSHelper();

    private LanguageModelGeneratorService() {
    }

    public void generate(String admin) {
        if (!isRunning()) {
            synchronized (this) {
                setRunning(true);
                this.admin = admin;
                try {
                    cleanup();
                    if (!tmpDir.exists() || !tmpDir.isDirectory())
                        tmpDir.mkdirs();
                    currentLogFile = new File(tmpDir, "current.log");
                    if (currentLogFile.exists())
                        try {
                            FileUtils.forceDelete(currentLogFile);
                        } catch (IOException e) {
                        }
                    targetDir = new File(tmpDir, UUIDGenerator.generateUUID());
                    runningFuture = new WeakReference<Future<?>>(executorService.submit(new GeneratorRunnable()));
                } catch (Exception e) {
                    appendError("Could not start training", e);
                    setRunning(false);
                }
            }
        }
    }


    private void cleanup() {
        if (tmpDir != null && tmpDir.exists()) {
            File[] files = tmpDir.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    String name = file.getName();
                    if (!(name.equalsIgnoreCase("current.log")
                            || name.equalsIgnoreCase("extra"))) {
                        try {
                            FileUtils.forceDelete(file);
                        } catch (Exception e) {}
                    }
                }
            }
        }
    }

    public void forceStop() {
        synchronized (this) {
            stopping = true;
            appendMessage("Force stop generating ...");
            if (runningFuture != null) {
                final Future future = runningFuture.get();
                if (future != null) {
                    while (!future.isDone()) {
                        future.cancel(true);
                    }
                }
                try {
                    executorService.shutdownNow();
                    executorService = Executors.newFixedThreadPool(1);
                } catch (Exception e) {}
            }
            cleanup();
//            if (currentLogFile != null && currentLogFile.exists())
//                try {
//                    FileUtils.forceDelete(currentLogFile);
//                } catch (IOException e) {}
            this.admin = null;
            running = false;
            stopping = false;
        }
    }

    public String getAdmin() {
        return admin;
    }

    public boolean isRunning() {
        return running;
    }

    private void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isStopping() {
        return stopping;
    }

    private class GeneratorRunnable implements Runnable {
        @Override
        public void run() {
            appendMessage("Preparing ... Please wait.");
            long start = System.currentTimeMillis();
            try {
                final LanguageModelVersionDAO dao = new LanguageModelVersionDAO();
                final int version = dao.getMaxVersion() + 1;
                projectName = "version-" +version +".lm";
                LanguageModelGenerator generator = new LanguageModelGenerator(new LanguageModelGenerator.TrainingListener() {
                    @Override
                    public void onMessage(String message) {
                        appendMessage(message);
                    }

                    @Override
                    public void onError(String message, Throwable e) {
                        appendError(message, e);
                    }

                    @Override
                    public void onSuccess(File languageModel) throws Exception {
                        appendMessage("Upload to language model to S3 " + projectName);
                        awsHelper.upload(Constant.FOLDER_LANGUAGE_MODEL
                                + "/" + projectName, languageModel);
                        Date now = new Date(System.currentTimeMillis());
                        LanguageModelVersion languageModelVersion = new LanguageModelVersion();
                        languageModelVersion.setVersion(version);
                        languageModelVersion.setAdmin(admin);
                        languageModelVersion.setCreatedDate(now);
                        languageModelVersion.setFileName(projectName);
                        languageModelVersion.setLogFileName(projectName + ".log");
                        languageModelVersion.setSelected(true);
                        languageModelVersion.setSelectedDate(now);
                        appendMessage("Insert to database");
                        dao.removeSelected();
                        dao.createObj(languageModelVersion);
                    }
                }, targetDir);
                generator.training();
            } catch (Exception e) {
                appendError("Could not complete generate language model",e);
            } finally {
                appendMessage("Execution time: " + (System.currentTimeMillis() - start) + "ms");
                appendMessage("Clean up ...");
                cleanup();
                appendMessage("Done.");
                if (currentLogFile != null && currentLogFile.exists()) {
                    appendMessage("Save running log " + currentLogFile + " to AWS S3 " + projectName + ".log");
                    try {
                        File targetLog = new File(targetDir, projectName + ".running.log");
                        if (targetLog.exists())
                            FileUtils.forceDelete(targetLog);
                        FileUtils.copyFile(currentLogFile, targetLog);
                        awsHelper.upload(Constant.FOLDER_LANGUAGE_MODEL
                                + "/" +projectName + ".log", targetLog);
                        awsHelper.upload(Constant.FOLDER_LANGUAGE_MODEL
                                + "/" + "latest.running.log", targetLog);
                    } catch (Exception e) {
                        appendError("Could not save running log", e);
                    }
                }
                setRunning(false);
            }
        }
    }

    private void appendMessage(String message) {
        appendLog("INFO - " + message);
    }

    private void appendError(String message) {
        appendError(message, null);
    }

    private void appendError(String message, Throwable e) {
        appendLog("ERROR - " + message);
        if (e != null)
            appendLog(ExceptionUtils.getStackTrace(e));
    }

    private void appendLog(String log) {
        if (currentLogFile != null) {
            try {
                logger.info(log);
                FileUtils.writeStringToFile(currentLogFile,
                        sdf.format(new Date(System.currentTimeMillis())) + " " + log + "\n",
                        "UTF-8", true);
            } catch (Exception e) {}
        }
    }

    public File getCurrentLogFile() {
        return currentLogFile;
    }

    public String getCurrentLog(int lines) {
        try {
            if (currentLogFile != null && currentLogFile.exists()) {
                if (lines <= 0) {
                    return FileUtils.readFileToString(currentLogFile, "UTF-8");
                } else {
                    int counter = 0;
                    ReversedLinesFileReader object = new ReversedLinesFileReader(currentLogFile, 4096, "UTF-8");
                    StringBuilder builder = new StringBuilder();
                    try {

                        String line;
                        while ((line = object.readLine()) != null && counter < lines) {
                            builder.append(line).append("\n");
                            counter++;
                        }
                    } finally {
                        IOUtils.closeQuietly(object);
                    }
                    return builder.toString();
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not fetch current log");
        }
        return "";
    }

}
